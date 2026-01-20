package com.bulavskiy.demo.dao.impl;

import com.bulavskiy.demo.dao.RatingDao;
import com.bulavskiy.demo.model.Rating;
import com.bulavskiy.demo.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class RatingDaoImpl implements RatingDao {
  private static final Logger log = LogManager.getLogger();
  private static final String INSERT_RATING = "INSERT INTO ratings (user_id, cocktail_id, score) VALUES (?, ?, ?) " +
          "ON DUPLICATE KEY UPDATE score = ?";
  private static final String FIND_RATING = "SELECT id, user_id, cocktail_id, score FROM ratings WHERE user_id = ? AND cocktail_id = ?";
  private static final String GET_USER_RATING = "SELECT score FROM ratings WHERE user_id = ? AND cocktail_id = ?";

  @Override
  public boolean addOrUpdateRating(Rating rating) {
    Connection connection = ConnectionPool.getInstance().getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_RATING)) {
      preparedStatement.setInt(1, rating.getUserId());
      preparedStatement.setInt(2, rating.getCocktailId());
      preparedStatement.setInt(3, rating.getScore());
      preparedStatement.setInt(4, rating.getScore());
      int rowsAffected = preparedStatement.executeUpdate();
      return rowsAffected > 0;
    } catch (SQLException e) {
      log.error("Error adding/updating rating", e);
      return false;
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
  }

  @Override
  public Optional<Rating> findRatingByUserAndCocktail(int userId, int cocktailId) {
    Connection connection = ConnectionPool.getInstance().getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_RATING)) {
      preparedStatement.setInt(1, userId);
      preparedStatement.setInt(2, cocktailId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          Rating rating = new Rating(
                  resultSet.getLong("id"),
                  resultSet.getInt("user_id"),
                  resultSet.getInt("cocktail_id"),
                  resultSet.getInt("score")
          );
          return Optional.of(rating);
        }
      }
    } catch (SQLException e) {
      log.error("Error finding rating", e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
    return Optional.empty();
  }

  @Override
  public int getUserRating(int userId, int cocktailId) {
    Connection connection = ConnectionPool.getInstance().getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_RATING)) {
      preparedStatement.setInt(1, userId);
      preparedStatement.setInt(2, cocktailId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          return resultSet.getInt("score");
        }
      }
    } catch (SQLException e) {
      log.error("Error getting user rating", e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
    return 0;
  }
}

