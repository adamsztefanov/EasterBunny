package com.sztefanov.ajaxbridge;

import com.sztefanov.ajaxbridge.datastructure.Data;

public abstract class CommandAbstract {
  private final Data request;
  public CommandAbstract(Data request) {
    this.request = request;
  }
  public abstract Data process();
  public Data getRequest() {
    return request;
  }

}
