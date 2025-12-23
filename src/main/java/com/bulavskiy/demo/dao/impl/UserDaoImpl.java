package com.bulavskiy.demo.dao.impl;

import com.bulavskiy.demo.dao.BaseDao;
import com.bulavskiy.demo.dao.UserDao;
import com.bulavskiy.demo.model.Role;
import com.bulavskiy.demo.model.User;
import com.bulavskiy.demo.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl extends BaseDao<User> implements UserDao {
  private static final Logger log = LogManager.getLogger();

  private static final String FIND_USER_BY_LOGIN =
          "SELECT id, login, password, role FROM users WHERE login = ?";
  private static final String INSERT =
          "INSERT INTO users (login, password, role) VALUES (?, ?, ?)";

  @Override
  public Optional<User> findUserByLogin(String login) {
    Connection connection = ConnectionPool.getInstance().getConnection();

    try(PreparedStatement statement = connection.prepareStatement(FIND_USER_BY_LOGIN)){
      statement.setString(1, login);
      try(ResultSet results = statement.executeQuery()) {
        if(results.next()){
          User user = new User();
          user.setId(results.getLong("id"));
          user.setLogin(results.getString("login"));
          user.setPassword(results.getString("password"));
          user.setRole(Role.valueOf(results.getString("role").toUpperCase()));
          return Optional.of(user);
        }
      }
    } catch (SQLException e) {
      log.error("Error auth with login: {} {}", login, e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }

    return Optional.empty();
  }

  @Override
  public boolean insert(User user) {
    try(Connection connection = ConnectionPool.getInstance().getConnection()){
      PreparedStatement statement = connection.prepareStatement(INSERT);
      statement.setString(1, user.getLogin());
      statement.setString(2, user.getPassword());
      statement.setString(3, user.getRole().name().toLowerCase());
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      log.error("Error insert user: {}", user.getLogin());
      return false;
    }
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
}
