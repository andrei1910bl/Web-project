package com.bulavskiy.demo.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bulavskiy.demo.dao.BaseDao;
import com.bulavskiy.demo.dao.UserDao;
import com.bulavskiy.demo.model.Role;
import com.bulavskiy.demo.model.User;
import com.bulavskiy.demo.model.UserStatistics;
import com.bulavskiy.demo.pool.ConnectionPool;

public class UserDaoImpl extends BaseDao<User> implements UserDao {
  private static final Logger log = LogManager.getLogger();

  private static final String FIND_USER_BY_LOGIN =
          "SELECT id, login, password, role FROM users WHERE login = ?";
  private static final String INSERT =
          "INSERT INTO users (login, password, role) VALUES (?, ?, ?)";
  private static final String FIND_ALL_USERS_WITH_STATISTICS =
          "SELECT u.id, u.login, u.password, u.role, " +
          "COUNT(DISTINCT c.id) as cocktail_count, " +
          "COALESCE(AVG(r.score), 0) as avg_rating " +
          "FROM users u " +
          "LEFT JOIN cocktails c ON u.id = c.author_id " +
          "LEFT JOIN ratings r ON c.id = r.cocktail_id " +
          "GROUP BY u.id, u.login, u.password, u.role";
  private static final String UPDATE_USER_ROLE = "UPDATE users SET role = ? WHERE id = ?";
  private static final String FIND_USER_BY_ID = "SELECT id, login, password, role FROM users WHERE id = ?";

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
    Connection connection = ConnectionPool.getInstance().getConnection();
    try (PreparedStatement statement = connection.prepareStatement(INSERT)) {
      statement.setString(1, user.getLogin());
      statement.setString(2, user.getPassword());
      statement.setString(3, user.getRole().name().toLowerCase());
      return statement.executeUpdate() > 0;
    } catch (SQLException e) {
      log.error("Error insert user: {}", user.getLogin(), e);
      return false;
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
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

  @Override
  public List<UserStatistics> findAllUsersWithStatistics() {
    List<UserStatistics> statisticsList = new ArrayList<>();
    Connection connection = ConnectionPool.getInstance().getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_USERS_WITH_STATISTICS);
         ResultSet resultSet = preparedStatement.executeQuery()) {
      while (resultSet.next()) {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setLogin(resultSet.getString("login"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(Role.valueOf(resultSet.getString("role").toUpperCase()));

        int cocktailCount = resultSet.getInt("cocktail_count");
        double avgRating = resultSet.getDouble("avg_rating");

        UserStatistics statistics = new UserStatistics(user, cocktailCount, avgRating);
        statisticsList.add(statistics);
      }
    } catch (SQLException e) {
      log.error("Error finding all users with statistics", e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
    return statisticsList;
  }

  @Override
  public boolean updateUserRole(Long userId, String role) {
    Connection connection = ConnectionPool.getInstance().getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_ROLE)) {
      preparedStatement.setString(1, role.toLowerCase());
      preparedStatement.setLong(2, userId);
      return preparedStatement.executeUpdate() > 0;
    } catch (SQLException e) {
      log.error("Error updating user role for userId: {}", userId, e);
      return false;
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
  }

  @Override
  public Optional<User> findUserById(Long userId) {
    Connection connection = ConnectionPool.getInstance().getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_ID)) {
      preparedStatement.setLong(1, userId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          User user = new User();
          user.setId(resultSet.getLong("id"));
          user.setLogin(resultSet.getString("login"));
          user.setPassword(resultSet.getString("password"));
          user.setRole(Role.valueOf(resultSet.getString("role").toUpperCase()));
          return Optional.of(user);
        }
      }
    } catch (SQLException e) {
      log.error("Error finding user by id: {}", userId, e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
    return Optional.empty();
  }
}
