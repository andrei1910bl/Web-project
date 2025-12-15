package com.bulavskiy.demo.pool;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPool {
  private static ConnectionPool instance;
  private BlockingQueue<Connection> free = new LinkedBlockingQueue<>(8);
  private BlockingQueue<Connection> used = new LinkedBlockingQueue<>(8);

  static {
    try {
      DriverManager.registerDriver(new Driver());
    } catch (
            SQLException e) {
      e.printStackTrace();
    }
  }

  private ConnectionPool() {
    String url = "jdbc:mysql://localhost:3306/task04";
    Properties prop = new Properties();
    prop.put("user", "root");
    prop.put("password", "mysqlpass123$");

    for (int i = 0; i < 8; i++) {
      Connection connection = null;
      try {
        connection = DriverManager.getConnection(url, prop);
      } catch (SQLException e) {
        e.printStackTrace();
      }
      free.add(connection);
    }
  }

  public static ConnectionPool getInstance() {
    return instance;
  }

  public Connection getConnection(){
    Connection connection = null;
    try {
      connection = free.take();
      used.put(connection);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return connection;
  }

  public void releaseConnection(Connection connection){
    try {
      used.remove(connection);
      free.put(connection);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
