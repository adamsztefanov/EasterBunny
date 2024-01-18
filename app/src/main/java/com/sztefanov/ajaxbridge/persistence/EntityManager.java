package com.sztefanov.ajaxbridge.persistence;

import com.sztefanov.ajaxbridge.Database;
import com.sztefanov.ajaxbridge.datastructure.Data;
import com.sztefanov.ajaxbridge.helper.Random;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

public abstract class EntityManager {

  public abstract Logger createLogger();

  public EntityManager em;
  //public DB db;
  private final ColumnTypes columnTypes;
  //private Session session;
  private String table;
  private boolean autoincrement = false;
  public final Logger logger;

  protected EntityManager() {
    this.logger = createLogger();
    this.columnTypes = new ColumnTypes();
    this.table = createTable();
    //this.session = null;
    //this.db = null;
  }

//	public EntityManager(Session session) {
//		this.columnTypes = new ColumnTypes();
//		//this.session = session;
//		this.table = createTable();
//		//this.db = null;
//	}
//	public EntityManager(Session session, DB db) {
//		this.columnTypes = new ColumnTypes();
//		//this.session = session;
//		this.table = createTable();
//		//this.db = db;
//	}
  public abstract String createTable();

  public abstract String getTableColumns();

  public abstract Object createInstance(Data data);

  public abstract EntityManager createSelfManager();

  /* IMPLEMENTATION EXAMPLE:
	------------
		@Override
		public List getEntities(Data data, ParameterBuilder pb) {
			ArrayList<Data> rows = getEntitiesBase(data, pb);
			ArrayList<User> entities = new ArrayList<>();
			for (Data row : rows) {
				entities.add(new User(row));
			}
			return entities;
		}
   */
  /**
   *
   * @param data
   * @param pb
   * @return
   */
  public abstract List getEntities(Data data, ParameterBuilder pb);

  /* 
  
  Everything is not added in ct will be handled as  a String.
  
  EXAMPLE
	-----------
	@Override
	public ColumnTypes getTableColumnTypes() {
		ColumnTypes ct = new ColumnTypes();
		ct.put("id", "int");
		ct.put("county_id", "int");
		ct.put("lat", "double");
		ct.put("lng", "double");
		ct.put("town_id", "int");
		ct.put("postcode_id", "int");
		ct.put("blocked", "int");
		return ct;
	}
   */
  public abstract ColumnTypes getTableColumnTypes();

  public String getOrderBy() {
    return getPrimaryKey() + " ASC";
  }

  public String getOffset() {
    return "0";
  }

  public String getLimit() {
    return "";
  }

  public ArrayList<Data> getEntitiesBase(
    Data data, ParameterBuilder pb) throws SQLException {
    ArrayList<Data> result;
    StringBuilder sql = new StringBuilder();
    String order = data.get("order") == null
      ? getOrderBy() : data.get("order");
    String limit = data.get("limit") == null
      ? getLimit() : data.get("limit");
    String offset = data.get("offset") == null
      ? getOffset() : data.get("offset");
    String conditions = data.get("conditions") == null
      ? "" : data.get("conditions");
    sql.append("SELECT ")
      .append(getTableColumns())
      .append(" FROM ")
      .append(getTable()).append(" ")
      .append(conditions)
      .append(" ORDER BY ").append(order);
    if (!limit.equals("")) {
      sql.append(" LIMIT ").append(limit)
        .append(" OFFSET ").append(offset);
    }
    Object db = null;
    if (db == null) {
      result = Database.select(sql.toString(), pb);
    } else {
      result = (ArrayList<Data>) Database.select(sql.toString(), pb);
    }
    return result;
  }

  public Object getEntity(Data data, ParameterBuilder pb) {
    Object entity = null;
    List entities = getEntities(data, pb);
    if (!entities.isEmpty()) {
      entity = entities.get(0);
    }
    return entity;
  }

  public Object getEntityByPrimaryKey(String primaryType, String primaryValue) {
    Data data = new Data();
    data.put("conditions", "WHERE " + getPrimaryKey() + " = ?");
    ParameterBuilder pb = new ParameterBuilder();
    pb.addParam(primaryType, primaryValue);
    return getEntity(data, pb);
  }

  public long insert(Entity entity) throws SQLException {
    long result = -1;
    if (entity.valid()) {
      if (em == null) {
        em = this.createSelfManager();
      }
      StringBuilder sb = new StringBuilder();
      String tableColumns = getTableColumns();
      int quotes = tableColumns.split(",").length;
      if (autoincrement) {
        quotes--;
      }
      sb.append("INSERT INTO ")
        .append(getTable())
        .append(" ")
        .append(" (")
        .append(getInsertColumn())
        .append(") VALUES (")
        .append(Database.quotes(quotes))
        .append(")");
      Data data = entity.getData();
      String[] split = tableColumns.split(",");
      Data tableColumnTypes = em.getTableColumnTypes();
      ParameterBuilder pb = new ParameterBuilder();
      for (String key : split) {
        String key2 = key.trim();
        pb.addParam(tableColumnTypes.get(key2), data.get(key2));
      }
//			System.out.println("**************************************************");
//			System.out.println("EntityManager.insert(entity)");
//			System.out.println("entity?");
//			System.out.println(entity);
//			System.out.println("sb?");
//			System.out.println(sb);
//			System.out.println("pb?");
//			System.out.println(pb);
      Object db = null;
      // insert entity into database based on the database connection
      if (db == null) {
        result = Database.insert(sb.toString(), pb);
      }
    }
    return result;
  }

  public boolean update(Entity entity) throws SQLException {
    boolean success = true;
    if (entity.valid()) {
      if (em == null) {
        em = createSelfManager();
      }
      StringBuilder sb = new StringBuilder();
      String primary = em.getPrimaryKey();
      String value = entity.get(primary);
      String sql = "SELECT * FROM " + table + " WHERE " + primary + " = ?";
      ParameterBuilder pb = new ParameterBuilder();
      String type = em.getTableColumnTypes().get(primary);
      pb.addParam(type, value);
      Data old = (Data) Database.selectSingle(sql, pb);
      ArrayList<String> cols = new ArrayList<>();
      if (old != null) {
        for (Map.Entry<String, String> entry : old.entrySet()) {
          String key = entry.getKey();
          String val = entry.getValue();
          if (entity.getData().containsKey(key)) {
            if (!entity.get(key).equals(val)) {
              cols.add(key);
            }
          }
        }
      }
      int colLength = cols.size();
      if (colLength > 0) {
        pb = new ParameterBuilder();
        int i = 0;
        sb.append("UPDATE ")
          .append(table)
          .append(" SET ");
        for (String col : cols) {
          sb.append(col).append(" = ?");
          String colType = em.getTableColumnTypes().get(col);
          pb.addParam(colType, entity.get(col));
          if (i < colLength - 1) {
            sb.append(",");
          }
          i++;
        }
        sb.append(" WHERE ")
          .append(primary)
          .append(" = ?");
        type = em.getTableColumnTypes().get(primary);
        pb.addParam(type, value);
      }
      if (sb.length() > 0) {
        Object db = null;
        if (db == null) {
          Database.update(sb.toString(), pb);
        }
      }
    } else {
      success = false;
    }
    return success;
  }

  public boolean delete(Entity entity) throws SQLException {
    boolean success = true;
    String pk = getPrimaryKey();
    StringBuilder sb = new StringBuilder();
    sb.append("DELETE FROM ")
      .append(table)
      .append(" WHERE ")
      .append(pk)
      .append(" = ?");
    ParameterBuilder pb = new ParameterBuilder();
    pb.addParam(getColumnTypes().get(pk), getPrimaryValue(entity));
    Object db = null;
    if (db == null) {
      Database.update(sb.toString(), pb);
    }
    return success;
  }

  public String getPrimaryKey() {
    return "id";
  }

  public String getPrimaryValue(Entity entity) {
    String primaryValue;
    primaryValue = entity.get(em.getPrimaryKey());
    return primaryValue;
  }

  public ColumnTypes getColumnTypes() {
    return columnTypes;
  }

  public static String generateHash() {
    StringBuilder salt = new StringBuilder();
    String palette = "1234567890qwertzuiopasdfghjklyxcvbnm";
    int length = palette.length() - 1;
    for (int i = 0; i < 128; i++) {
      int rand = Random.randomRange(0, length);
      salt.append(palette.charAt(rand));
    }
    return salt.toString();
  }

  public ArrayList getAll() {
    Data data = new Data();
    data.put("conditions", "WHERE 1");
    data.put("limit", "5000");
    if (em == null) {
      em = createSelfManager();
    }
    ArrayList result = (ArrayList) em
      .getEntities(data, new ParameterBuilder());
    return result;
  }

  public ArrayList getAll(long offset) {
    Data data = new Data();
    data.put("conditions", "WHERE 1");
    data.put("limit", "5000");
    data.put("offset", "" + offset);
    if (em == null) {
      em = createSelfManager();
    }
    ArrayList result = (ArrayList) getEntities(data, new ParameterBuilder());
    return result;
  }

  /**
   *
   * Searches in database based on primary value using the LIKE statement.
   *
   * @param searchData
   * @return
   */
  public ArrayList search(Data searchData) {
    ArrayList<Data> result = null;
    String word = searchData.get("word");
    StringBuilder sb = new StringBuilder();
    ParameterBuilder pb = new ParameterBuilder();
    String key = getPrimaryKey();
    if (searchData.containsKey("key")) {
      key = searchData.get("key");
    }
    sb.append("SELECT ")
      .append(getTableColumns())
      .append(" FROM ")
      .append(getTable())
      .append(" WHERE ")
      .append(key)
      .append(" LIKE ?");
    pb.addParam("String", word + "%");
    Object db = null;
    if (db == null) {
      result = Database.select(sb.toString(), pb);
    }
    return result;
  }

//
// GETTERS, SETTERS
//
  public String getTable() {
    return table;
  }

  public void setTable(String table) {
    this.table = table;
  }

  public boolean isAutoincrement() {
    return autoincrement;
  }

  public void setAutoincrement(boolean autoincrement) {
    this.autoincrement = autoincrement;
  }

  public String getInsertColumn() {
    String result = "";
    if (autoincrement) {
      String[] split = getTableColumns().split(",");
      String primaryKey = getPrimaryKey();
      int i = 2;
      for (String col : split) {
        col = col.trim();
        int length = col.length();
        if (!col.equals(primaryKey)) {
          result += col;
          if (i < length - 1) {
            result += ", ";
          }
        }
        i++;
      }
    } else {
      result = getTableColumns();
    }
    return result;
  }

  @Override
  public String toString() {
    return "EntityManager{" + "columnTypes=" + columnTypes + '}';
  }

}
