package com.sztefanov.ajaxbridge;

import com.sztefanov.ajaxbridge.datastructure.Data;
import com.sztefanov.ajaxbridge.datastructure.JsonConverter;
import com.sztefanov.easterbunny.Command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * This is the connector to the backend application.
 * The web app calls localhost in Android
 * Command interface needs to be extended and implemented
 */
public abstract class AjaxBridge implements Runnable {

  private ServerSocket serverSocket;
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private boolean isListening = false;

  public static final Logger LOGGER = LoggerFactory.getLogger(AjaxBridge.class);

  public abstract CommandAbstract createCommand(Data request);

  private static Data requestQuery(String query) {
    String[] params = query.split("&");
    Data map = new Data();
    for (String param : params) {
      String name = param.split("=")[0];
      String value = param.split("=")[1];
      map.put(name, value);
    }
    return map;
  }

  private Data requestGet(String requestHeaders) {
    LOGGER.debug("AjaxBridge.requestGet()");
    Data request = new Data();
    String[] split = requestHeaders.split(" ");
    if (split[0].equals("request")) {
      if (split[1].charAt(0) == '/') {
        split[1] = split[1].substring(1, split[1].length());
      }
      if (split[1].charAt(0) == '?') {
        split[1] = split[1].substring(1, split[1].length());
      }
      request = requestQuery(split[1]);
    }
    return request;
  }

  void listen() {
    LOGGER.debug("AjaxBrige.listen()");
    isListening = true;
    try {
      serverSocket = new ServerSocket(50000);
      LOGGER.info("AjaxBridge: Listening");
      while (isListening) {
        socket = serverSocket.accept();
        InputStreamReader isr = new InputStreamReader(socket.getInputStream());
        in = new BufferedReader(isr);
        out = new PrintWriter(socket.getOutputStream());
        StringBuilder requestString = new StringBuilder();
        // read
        String line;
        while ((line = in.readLine()) != null) {
          if (line.length() == 0) {
            break;
          }
          requestString.append(line);
        }
        // callback
        Data request = requestGet(requestString.toString());
        LOGGER.info("requestString", requestString);
        LOGGER.info("requestGet()");
        LOGGER.info(request.toString());
        String callback = request.get("callback");
        // abstract command interface must be implemented
        Data result = new Data();
        result.put("status", "empty");
        CommandAbstract command = new Command(request);
        try {
          result = command.process();
        } catch (UnsupportedOperationException ex) {
          LOGGER.error("listen() UnsupportedOperationException", ex);
        }
        String json = null;
        json = JsonConverter.dataToString(result);
        String response = callback + "(" + json + ")";
        // print headers
        out.println("HTTP/1.1 200 OK");
        out.println("Server: AjaxBrige : 1.0");
        out.println("Date: " + new Date());
        out.println("Content-Type: application/javascript");
        out.println("Content-length: " + response.length());
        out.println(); // blank between headers and content
        // print response
        out.println(response);
        // flush and close
        out.flush();
        out.close();
        in.close();
      }
    } catch (IOException ex) {
      LOGGER.error("listen() IOException", ex);
    }
  }

  public void shutdown() {
    LOGGER.debug("AjaxBrige.shutdown()");
    isListening = false;
//    try {
//      if (out != null) {
//        out.close();
//      }
//      if (in != null) {
//        in.close();
//      }
//      if (socket != null) {
//        socket.close();
//      }
//      if (serverSocket != null) {
//        serverSocket.close();
//      }
//    } catch (IOException ex) {
//      LOGGER.error("shutdown(): IOException", ex);
//    }
  }

  @Override
  public void run() {
    listen();
  }

}
