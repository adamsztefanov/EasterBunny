package com.sztefanov.ajaxbridge;

import java.sql.Connection;
import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSource implements DatabaseConfig {
  
  public static final Logger LOGGER = LoggerFactory
    .getLogger(DataSource.class);
  public static final BasicDataSource ds = new BasicDataSource(); 

  static {
//		String host_extension = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&characterEncoding=" + MYSQL_CHARSET;
//		String db = MYSQL_DB;
//		if (!db.equals("")) {
//			db = "/" + db;
//		}
//		String url = "jdbc:mysql://" + MYSQL_HOST + ":" + MYSQL_PORT
//			+ db + host_extension;
//		ds.setUrl(url);
//		ds.setUsername(MYSQL_USER);
//		ds.setPassword(MYSQL_PASSWORD);
//		ds.setMinIdle(0);
//		ds.setMaxIdle(0);
//		ds.setMaxOpenPreparedStatements(100);
  }

  public static Connection getConnection() {
    Connection conn;
//		try {
//			conn = ds.getConnection();
//			if (Config.getProperty("mysql.foreignkeys").equals("true")) {
//				conn.createStatement().executeUpdate("SET FOREIGN_KEY_CHECKS=0");
//			}
//		} catch (SQLException ex) {
//			Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
//			conn = null;
//		}
//    return conn;
    return null;
  }

}
