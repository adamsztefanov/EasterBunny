package com.sztefanov.ajaxbridge.log;

import com.sztefanov.ajaxbridge.persistence.ColumnTypes;
import com.sztefanov.ajaxbridge.persistence.EntityManager;
import com.sztefanov.ajaxbridge.persistence.ParameterBuilder;
import com.sztefanov.ajaxbridge.datastructure.Data;
import java.util.List;
import org.slf4j.Logger;

public class LogManager extends EntityManager {

  @Override
  public String createTable() {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public String getTableColumns() {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public ColumnTypes getTableColumnTypes() {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }
  // TODO! implement	

  @Override
  public EntityManager createSelfManager() {
    throw new UnsupportedOperationException("Not supported yet.");
    // implement com.sztefanov.nn.log.LogManager createSelfManager
  }

  @Override
  public Logger createLogger() {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public Object createInstance(Data data) {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }

  @Override
  public List getEntities(Data data, ParameterBuilder pb) {
    throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
  }
}
