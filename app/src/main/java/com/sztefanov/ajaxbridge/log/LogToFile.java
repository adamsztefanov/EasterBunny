package com.sztefanov.ajaxbridge.log;

import com.sztefanov.ajaxbridge.helper.DateNN;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class LogToFile {

  private LogToFile() {
    // ...
  }

  public static void insertLine(String filePath, String line)
    throws IOException {
    Path path = Paths.get(filePath);
    String lineMarked = line + "\n";
    try {
      Files.write(path, lineMarked.getBytes(),
        StandardOpenOption.APPEND);
    } catch (IOException ex) {
      throw ex;
    }
  }

  public static void insertLineTime(String filePath, String line)
    throws IOException {
    String time = "[" + DateNN.now() + "]";
    String linetime = time + line;
    try {
      insertLine(filePath, linetime);
    } catch (IOException ex) {
      throw ex;
    }
  }

}
