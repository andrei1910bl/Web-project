package com.bulavskiy.demo.pool;

import com.mysql.cj.jdbc.Driver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPool {
  private static final Logger log = LogManager.getLogger();
  private static final String PROPERTIES_FILE = "database.properties"; //example

  private static ConnectionPool instance;
  private BlockingQueue<Connection> free = new LinkedBlockingQueue<>(8);
  private BlockingQueue<Connection> used = new LinkedBlockingQueue<>(8);
  private String url;
  private String user;
  private String password;

  static {
    try {
      DriverManager.registerDriver(new Driver());
    } catch (SQLException e) {
      log.error("Error registering MySQL driver", e);
    }
  }

  private ConnectionPool() {
    loadDatabaseProperties();
    
    Properties prop = new Properties();
    prop.put("user", user);
    prop.put("password", password);

    for (int i = 0; i < 8; i++) {
      Connection connection = null;
      try {
        connection = DriverManager.getConnection(url, prop);
      } catch (SQLException e) {
        log.error("Error creating database connection", e);
      }
      free.add(connection);
    }
  }

  private void loadDatabaseProperties() {
    Properties properties = new Properties();
    try (InputStream inputStream = ConnectionPool.class.getClassLoader()
            .getResourceAsStream(PROPERTIES_FILE)) {
      properties.load(inputStream);
      
      url = properties.getProperty("db.url");
      user = properties.getProperty("db.user");
      password = properties.getProperty("db.password");
    } catch (Exception e) {
      throw new RuntimeException("Failed to load database properties", e);
    }
  }

  public static ConnectionPool getInstance() {
    if(instance == null){
      instance = new ConnectionPool();
    }
    return instance;
  }

  public Connection getConnection(){
    Connection connection = null;
    try {
      connection = free.take();
      used.put(connection);
    } catch (InterruptedException e) {
      log.error("Interrupted while waiting for connection", e);
      Thread.currentThread().interrupt();
    }
    return connection;
  }

  public void releaseConnection(Connection connection){
    try {
      used.remove(connection);
      free.put(connection);
    } catch (InterruptedException e) {
      log.error("Error returning connection to pool", e);
      Thread.currentThread().interrupt();
    }
  }
}
