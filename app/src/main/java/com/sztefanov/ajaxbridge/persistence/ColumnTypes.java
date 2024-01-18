package com.sztefanov.ajaxbridge.persistence;

import com.sztefanov.ajaxbridge.datastructure.Data;

public class ColumnTypes extends Data {

  @Override
  public String get(Object key) {
    String value;
    try {
      value = super.get(key);
      if (value == null) {
        value = "String";
      }
    } catch (Exception e) {
      value = "String";
    }
    return value;
  }

}
