package com.sztefanov.easterbunny;

import com.sztefanov.ajaxbridge.CommandAbstract;
import com.sztefanov.ajaxbridge.datastructure.Data;

import java.util.Map;

public class Command extends CommandAbstract {

  public Command(Data request) {
    super(request);
  }

  @Override
  public Data process() {
    Data response = new Data();
    response.put("status", "command ok");
    for (Map.Entry<String, String> entry : getRequest().entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      response.put(key, value);
    }
    return response;
  }

}
