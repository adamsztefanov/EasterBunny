package com.sztefanov.ajaxbridge.datastructure;

import org.json.JSONException;

import java.util.HashMap;

public class Data extends HashMap<String, String> implements ParseInterface {
  public String jsonSerialize() {
    return JsonConverter.dataToString(this);
  }

  public boolean isset(String key) {
    String value;
    try {
      value = this.get(key);
    } catch (Exception e) {
      value = "";
    }
    return !value.equals("");
  }

  @Override
  public String get(Object key) {
    String result = "";
    String get = super.get(key);
    if (get != null) {
      result = get;
    }
    return result;
  }

  @Override
  public long getLong(String key) {
    return Long.parseLong(get(key));
  }

  @Override
  public boolean getBoolean(String key) throws ClassCastException {
    boolean bool = Boolean.getBoolean(get(key));
    if (!bool) {
      throw new ClassCastException(key + " is not a boolean");
    }
    return bool;
  }

  @Override
  public int getInt(String key) {
    return Integer.parseInt(get(key));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Entry<String, String> entry : this.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      sb.append("\t[").append(key).append(" => ")
        .append(value).append("]\n");
    }
    return "Data {\n" + sb.toString() + '}';
  }

}
