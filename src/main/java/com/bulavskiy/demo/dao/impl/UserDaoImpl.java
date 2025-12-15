package com.bulavskiy.demo.dao.impl;

import com.bulavskiy.demo.dao.BaseDao;
import com.bulavskiy.demo.dao.UserDao;
import com.bulavskiy.demo.entity.User;
import com.bulavskiy.demo.pool.ConnectionPool;
import com.mysql.cj.jdbc.Driver;

import java.sql.*;
import java.util.List;
import java.util.Properties;

public class UserDaoImpl extends BaseDao<User> implements UserDao {
  private static final String QUERY = "SELECT password FROM users WHERE lastname = ?";
  private static UserDaoImpl instance = new UserDaoImpl();

  private UserDaoImpl() {
  }

  public static UserDaoImpl getInstance() {
    return instance;
  }

  @Override
  public boolean insert(User user) {
    return false;
  }

  @Override
  public boolean delete(User user) {
    return false;
  }

  @Override
  public List<User> findAll() {
    return List.of();
  }

  @Override
  public User update(User user) {
    return null;
  }

  @Override
  public boolean authenticate(String login, String password) {

    boolean match = false;

    try (Connection connection = ConnectionPool.getInstance().getConnection();
         PreparedStatement statement = connection.prepareStatement(QUERY)){

      statement.setString(1, login);
      ResultSet resultSet = statement.executeQuery();

      String passFromDb;
      if (resultSet.next()){
        passFromDb = resultSet.getString(1);
        match = password.equals(passFromDb);
      }
    }catch (SQLException throwables){
      throwables.printStackTrace();
    }
    return match;
  }
}
