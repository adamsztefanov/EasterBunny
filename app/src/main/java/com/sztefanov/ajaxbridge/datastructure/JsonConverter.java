package com.sztefanov.ajaxbridge.datastructure;

import com.google.gson.Gson;
public class JsonConverter {

  public static Gson GSON = new Gson();

  public static Data stringToData(String jsonString) {
    return GSON.fromJson(jsonString, Data.class);
  }

  public static String dataToString(Object obj) {
    return GSON.toJson(obj);
  }

  public static String ObjectToString(Object obj) {
    return GSON.toJson(obj);
  }

  public static Object stringToObject(String jsonString, Class<ObjectData> cls) {
    return GSON.fromJson(jsonString, cls);
  }

}