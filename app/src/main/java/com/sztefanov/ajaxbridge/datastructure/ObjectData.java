package com.sztefanov.ajaxbridge.datastructure;

import org.json.JSONException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import java.util.HashMap;

public class ObjectData extends HashMap<String, Object> {

  public static final Logger LOGGER = LoggerFactory.getLogger(ObjectData.class);

  public String jsonSerialize() {
    return JsonConverter.ObjectToString(this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Entry<String, Object> entry : this.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue().toString();
      sb.append("\t[").append(key).append(" => ")
        .append(value).append("]\n");
    }
    return "ObjectData {\n" + sb.toString() + '}';
  }
}
