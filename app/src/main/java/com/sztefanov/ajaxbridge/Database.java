package com.sztefanov.ajaxbridge;

import com.sztefanov.ajaxbridge.persistence.ParameterBuilder;
import com.sztefanov.ajaxbridge.datastructure.Data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

  private Database() {
    // ...
  }

  /**
   *
   * @param sql
   * @return The generated primary value
   * @throws SQLException
   */
  public static long insert(String sql) throws SQLException {
    long lastInsertId = -1;
//    Connection conn = DataSource.getConnection();
//		Statement stmt = conn.createStatement();
//		stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
//		ResultSet rs = stmt.getGeneratedKeys();
//		if (rs.next()) {
//			lastInsertId = rs.getInt(1);
//		}
//		conn.close();
    return lastInsertId;
  }

  /**
   *
   * @param sql
   * @param conn
   * @return The generated primary value
   * @throws SQLException
   */
  public static long insert(String sql, Connection conn) throws SQLException {
    long lastInsertId = -1;
    Statement stmt = conn.createStatement();
    stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
    ResultSet rs = stmt.getGeneratedKeys();
    if (rs.next()) {
      lastInsertId = rs.getInt(1);
    }
    conn.close();
    return lastInsertId;
  }

  /**
   *
   * @param sql
   * @param pb
   * @return The generated primary value
   * @throws SQLException
   */
  public static long insert(String sql, ParameterBuilder pb)
    throws SQLException {
    long lastInsertId = -1;
//		Connection conn = DataSource.getConnection();
//		PreparedStatement stmt = conn.prepareStatement(sql,
//			Statement.RETURN_GENERATED_KEYS);
//		stmt = pb.addParamsToStmt(stmt);
//		stmt.executeUpdate();
//		ResultSet rs = stmt.getGeneratedKeys();
//		if (rs.next()) {
//			lastInsertId = rs.getInt(1);
//		}
//		conn.close();
    return lastInsertId;
  }

  /**
   *
   * @param sql
   * @param pb
   * @param conn
   * @return The generated primary value
   * @throws SQLException
   */
  public static long insert(String sql, ParameterBuilder pb, Connection conn)
    throws SQLException {
    long lastInsertId = -1;
//		PreparedStatement stmt = conn
//			.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//		stmt = pb.addParamsToStmt(stmt);
//		stmt.executeUpdate();
//		ResultSet rs = stmt.getGeneratedKeys();
//		if (rs.next()) {
//			lastInsertId = rs.getInt(1);
//		}
//		conn.close();
    return lastInsertId;
  }

  // UPDATE
  //
  /**
   *
   * @param sql SQL query
   * @return number of affected lines
   * @throws SQLException
   */
  public static long update(String sql) throws SQLException {
//		Connection conn = DataSource.getConnection();
//		Statement stmt = conn.createStatement();
//		long executeUpdate = stmt.executeUpdate(sql);
//		conn.close();
    //	return executeUpdate;
    return -1;
  }

  /**
   *
   * @param sql SQL query
   * @param conn
   * @return number of affected lines
   * @throws SQLException
   */
  public static long update(String sql, Connection conn) throws SQLException {
//    Statement stmt = conn.createStatement();
//    long executeUpdate = stmt.executeUpdate(sql);
//    // DEBUG
//    System.out.println("*****************************************************");
//    System.out.println("Database.update(sql)");
//    System.out.println("sql?");
//    System.out.println(sql);
//    // DEBUG
//    conn.close();
//    return executeUpdate;
    return -1;
  }

  /**
   *
   * @param sql SQL query
   * @param pb
   * @return number of affected lines
   * @throws SQLException
   */
  public static long update(String sql, ParameterBuilder pb)
    throws SQLException {
//    Connection conn = DataSource.getConnection();
//    PreparedStatement stmt = conn.prepareStatement(sql);
//    stmt = pb.addParamsToStmt(stmt);
//    // DEBUG
//    System.out.println("*****************************************************");
//    System.out.println("Database.update(sql, pb)");
//    System.out.println("sql?");
//    System.out.println(sql);
//    System.out.println("pb?");
//    System.out.println(pb);
//    // DEBUG
//    long executeUpdate = stmt.executeUpdate();
//    conn.close();
//    return executeUpdate;
    return -1;
  }

  /**
   *
   * @param sql SQL query
   * @param pb
   * @param conn
   * @return number of affected lines
   * @throws SQLException
   */
  public static int update(String sql, ParameterBuilder pb, Connection conn)
    throws SQLException {
    int lastInsertId = -1;
//    PreparedStatement stmt = conn
//      .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//    stmt = pb.addParamsToStmt(stmt);
//    stmt.executeUpdate();
//    ResultSet rs = stmt.getGeneratedKeys();
//    if (rs.next()) {
//      lastInsertId = rs.getInt(1);
//    }
//    // DEBUG
//    System.out.println("**********************************************");
//    System.out.println("Database.update(sql, pb)");
//    System.out.println("sql?");
//    System.out.println(sql);
//    System.out.println("pb?");
//    System.out.println(pb);
//    // DEBUG
//    conn.close();
    return lastInsertId;
  }

  // SELECT
  //
  public static ArrayList<Data> select(String sql, ParameterBuilder pb) {
    ArrayList<Data> result = new ArrayList<>();
//    try (Connection conn = DataSource.getConnection()) {
//      try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
//        ResultSetMetaData metaData = rs.getMetaData();
//        int columnsLength = metaData.getColumnCount();
//        while (rs.next()) {
//          Data row = new Data();
//          for (int i = 1; i <= columnsLength; i++) {
//            String key = metaData.getColumnName(i);
//            String value = String.valueOf(rs.getObject(i));
//            row.put(key, value);
//          }
//          result.add(row);
//        }
//      }
//    } catch (SQLException ex) {
//      Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
//    }
//    // DEBUG
//    System.out.println("*****************************************************");
//    System.out.println("Database.select(sql, pb)");
//    System.out.println("sql?");
//    System.out.println(sql);
//    if (result.size() >= 10) {
//      for (int i = 0; i < 10; i++) {
//        System.out.println(result.get(i));
//      }
//      if (result.size() > 10) {
//        System.out.println("................");
//      }
//    } else {
//      System.out.println("result?");
//      System.out.println(result);
//    }
//    // DEBUG
//    return result;
//  }
//
//  public static ArrayList<Data> select(String sql, ParameterBuilder pb)
//    throws SQLException {
//    ArrayList<Data> result = new ArrayList<>();
//    Connection conn = DataSource.getConnection();
//    PreparedStatement stmt = conn.prepareStatement(sql);
//    stmt = pb.addParamsToStmt(stmt);
//    ResultSet rs = stmt.executeQuery();
//    ResultSetMetaData metaData = rs.getMetaData();
//    int columnsLength = metaData.getColumnCount();
//    while (rs.next()) {
//      result.add(resultRow(rs, metaData, columnsLength));
//    }
//    conn.close();
//    // DEBUG
//    System.out.println("*****************************************************");
//    System.out.println("Database.select(sql, pb)");
//    System.out.println("sql?");
//    System.out.println(sql);
//    System.out.println("pb?");
//    System.out.println(pb);
//    if (result.size() >= 10) {
//      for (int i = 0; i < 10; i++) {
//        System.out.println(result.get(i));
//      }
//      if (result.size() > 10) {
//        System.out.println("................");
//      }
//    } else {
//      System.out.println("result?");
//      System.out.println(result);
//    }
//    // DEBUG
    return result;
  }

//  static Data resultRow(ResultSet rs,
//    ResultSetMetaData meta, int length) throws SQLException {
//    Data row = new Data();
//    for (int o = 1; o <= length; o++) {
//      String key = meta.getColumnName(o);
//      String type = meta.getColumnTypeName(o);
//      String value;
//      switch (type) {
//        case "BLOB":
//          value = rs.getString(o);
//          break;
//        default:
//          value = String.valueOf(rs.getObject(o));
//          break;
//      }
//      row.put(key, value);
//    }
//    return row;
//  }

  public static Data selectSingle(String sql, ParameterBuilder pb)
    throws SQLException {
    Data result = new Data();
//    ArrayList<Data> select = Database.select(sql, pb);
//    if (!select.isEmpty()) {
//      result = select.get(0);
//    }
    return result;
  }

  public static Data selectSingle(String sql) {
    Data result = new Data();
    ParameterBuilder pb = new ParameterBuilder();
    ArrayList<Data> select = Database.select(sql, pb);
    if (!select.isEmpty()) {
      result = select.get(0);
    }
    return result;
  }

  public static long count(String conditions) {
    long result = 0;
//    String sql = "SELECT COUNT(*) AS cnt " + conditions;
//    Data select = Database.selectSingle(sql);
//    System.out.println("conditions?");
//    System.out.println(conditions);
//    if (!select.isEmpty()) {
//      result = Long.parseLong(select.get("cnt"));
//    }
    return result;
  }

  public static long count(String conditions, ParameterBuilder pb)
    throws SQLException {
    String sql = "SELECT COUNT(*) AS cnt " + conditions;
    Data select = Database.selectSingle(sql, pb);
    return Long.parseLong(select.get("cnt"));
  }

  public static void truncate(String table) {
    String sql = "TRUNCATE " + table;
    try {
      Database.update(sql);
    } catch (SQLException ex) {
      Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public static boolean checkTableExist(String db, String table)
    throws SQLException {
    StringBuilder sb = new StringBuilder();
    sb.append("SELECT COUNT(*) as cnt FROM information_schema.tables")
      .append(" WHERE (TABLE_SCHEMA = ?) AND (TABLE_NAME = ?)");
    ParameterBuilder pb = new ParameterBuilder();
    pb.addParam("String", db);
    pb.addParam("String", table);
    Data row = Database.selectSingle(sb.toString(), pb);
    long count = Long.parseLong(row.get("cnt"));
    return count == 1;
  }

  public static String quotes(int number) {
    StringBuilder sb = new StringBuilder();
    int last = number - 1;
    for (int i = 1; i <= number; i++) {
      sb.append(i <= last ? "?, " : "?");
    }
    return sb.toString();
  }

}
