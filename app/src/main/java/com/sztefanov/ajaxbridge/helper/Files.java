package com.sztefanov.ajaxbridge.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Files {

  private Files() {
    // ...
  }

  /*
	 *  Reads a file content and returns is as a String
   */
  public static String fileToString(String filePath) throws IOException {
    StringBuilder contentBuilder = new StringBuilder();
    try (BufferedReader br = new BufferedReader(
      new FileReader(filePath))) {
      String sCurrentLine;
      while ((sCurrentLine = br.readLine()) != null) {
        contentBuilder.append(sCurrentLine).append("\n");
      }
    } catch (IOException ex) {
      throw ex;
    }
    return contentBuilder.toString();
  }

  public static void stringToFile(String stringToFile, String targetFilePath,
    boolean appendToFile) throws IOException {
    File targetFile = new File(targetFilePath);
      FileWriter fw = new FileWriter(targetFile);
      if (appendToFile) {
        fw.append(stringToFile)
          .append("\n");
      } else {
        fw.write(stringToFile);
      }
  }

  /**
   *
   * @param dir The directory starting directory example: [new File(".");] that
   * points to that directory where the application is running in.
   * @param files This is the object that the method collects the list
   * recursively.
   * example:
     ArrayList<File> files = new ArrayList<File>();
     Files.scanDir(new File("."), files);
     for (File file : files) {
       LOGGER.info(file.toString());
     }
   */
  public static void scanDir(File dir, ArrayList<File> files) {
    File[] children = dir.listFiles();
    if (children != null) {
      for (File child : children) {
        files.add(child);
        scanDir(child, files);
      }
    }
  }
}
