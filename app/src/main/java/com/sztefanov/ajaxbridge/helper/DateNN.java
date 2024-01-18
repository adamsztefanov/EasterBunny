package com.sztefanov.ajaxbridge.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateNN {

  private DateNN() {
    // ...
  }

  // TODO! the actual time is 2 hours late. set the correct timezone!!!
  public static String now() {
    Date now = new Date();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return dateFormat.format(now);
  }

  public static String ymdhis(Date d) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return dateFormat.format(d);
  }

  public static String ymd(Date d) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return dateFormat.format(d);
  }

}
