package com.sztefanov.ajaxbridge;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {

  public static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
  public static final Config config = new Config();
  private static final Properties properties = new Properties();

  private Config() {
    try (InputStream input = Config.class.getClassLoader()
      .getResourceAsStream("application.properties")) {
      properties.load(input);
    } catch (IOException ex) {
      LOGGER.error("Config() IOException", ex);
    }
  }

  public static String getProperty(String key) {
    return properties.getProperty(key);
  }

}
