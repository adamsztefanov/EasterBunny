package com.sztefanov.ajaxbridge.persistence;

import com.sztefanov.ajaxbridge.Database;
import com.sztefanov.ajaxbridge.datastructure.Data;
import com.sztefanov.ajaxbridge.datastructure.JsonConverter;
import java.sql.SQLException;

import org.json.JSONException;
import org.slf4j.Logger;

public abstract class Entity {

  public abstract Logger createLogger();

  public abstract EntityManager createEntitymanager();

  public Logger logger;
  private Data data;

  protected Entity() {
    this.data = new Data();
  }

  protected Entity(Data data) {
    this.logger = createLogger();
    this.data = data;
  }

  // TODO! implement get and set to be acessed trough getters and setters
  public String get(String key) {
    return data.get(key);
  }

  public String set(String key, String value) {
    return data.put(key, value);
  }

  public void save() {
    // TODO! implement Entity.save()
    // ...
  }

  public void update(String column, String value) throws SQLException {
    EntityManager em = (EntityManager) createEntitymanager();
    String table = em.getTable();
    StringBuilder sb = new StringBuilder();
    ParameterBuilder pb = new ParameterBuilder();
    String primaryKey = em.getPrimaryKey();
    String primaryType = em.getColumnTypes().get(primaryKey);
    String primaryValue = data.get(primaryKey);
    sb.append("UPDATE ")
      .append(table)
      .append(" SET ")
      .append(column)
      .append(" = ?")
      .append(" WHERE ")
      .append(primaryKey)
      .append(" = ?");
    String colType = em.getTableColumnTypes().get(column);
    pb.addParam(colType, value);
    pb.addParam(primaryType, primaryValue);
    Database.update(sb.toString(), pb);
    data.put(column, value);
  }

  //
  // GETTERS, SETTERS
  //
  public Data getData() {
    return data;
  }

  public void setData(Data data) {
    this.data = data;
  }

  // @Overridable
  public boolean valid() {
    return true;
  }

  public String toJson() throws JSONException {
    return JsonConverter.dataToString(this.data);
  }

  @Override
  public String toString() {
    return "Entity{" + "data=" + data + '}';
  }

}
