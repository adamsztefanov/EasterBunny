package com.sztefanov.ajaxbridge.datastructure;

import junit.framework.TestCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonConverterTest extends TestCase {

  public static final Logger LOGGER = LoggerFactory
    .getLogger(JsonConverter.class);

  public void testStringToData() {
    String sampleJsonString = "{\"alma\":\"körte\",\"nums\":\"1,2,3\"}";
    Data result = JsonConverter.stringToData(sampleJsonString);
    LOGGER.info(result.toString());
  }

  public void testDataToString() {
    Data testData = new Data();
    testData.put("alma", "körte");
    testData.put("nums", "1,2,3");
    String result = JsonConverter.dataToString(testData);
    LOGGER.info(result);
  }

  public void testObjectToString() {
    ObjectData objectData = new ObjectData();
    objectData.put("first", 3.0);
    Data testData = new Data();
    testData.put("alma", "körte");
    testData.put("nums", "1,2,3");
    objectData.put("second", testData);
    String result = JsonConverter.ObjectToString(objectData);
    LOGGER.info(result);
  }

  public void testStringToObject() {
    String testString = "{\"first\":3.0,\"second\":{\"alma\":\"körte\",\"nums\":\"1,2,3\"}}";
    ObjectData result = (ObjectData) JsonConverter.stringToObject(testString, ObjectData.class);
    LOGGER.info(result.toString());
  }

}