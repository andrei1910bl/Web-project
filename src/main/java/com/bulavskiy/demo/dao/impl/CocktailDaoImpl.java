package com.bulavskiy.demo.dao.impl;

import com.bulavskiy.demo.dao.CocktailDao;
import com.bulavskiy.demo.model.Cocktail;
import com.bulavskiy.demo.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CocktailDaoImpl implements CocktailDao {
  private static final Logger log = LogManager.getLogger();
  private static final String SELECT_ALL_COCKTAILS =
          "SELECT c.id, c.name, c.description, c.author_id, i.name as ingredient_name, ci.amount " +
          "FROM cocktails c " +
          "LEFT JOIN cocktail_ingredients ci ON c.id = ci.cocktail_id " +
          "LEFT JOIN ingredients i ON ci.ingredient_id = i.id";
  private static final String CREATE_COCKTAIL = "INSERT INTO cocktails (name, description, author_id) VALUES (?, ?, ?)";
  private static final String CREATE_COCKTAIL_ING = "INSERT INTO cocktail_ingredients (cocktail_id, ingredient_id, amount) VALUES (?, ?, ?)";
  private static final String SELECT_NAME_FROM_ING = "SELECT id FROM ingredients WHERE name = ?";
  private static final String CREATE_ING = "INSERT INTO ingredients (name) VALUES (?)";
  private static final String GET_AVERAGE_RATING = "SELECT AVG(score) FROM ratings WHERE cocktail_id = ?";
  private static final String SELECT_COCKTAILS_BY_AUTHOR =
          "SELECT c.id, c.name, c.description, c.author_id, i.name as ingredient_name, ci.amount " +
          "FROM cocktails c " +
          "LEFT JOIN cocktail_ingredients ci ON c.id = ci.cocktail_id " +
          "LEFT JOIN ingredients i ON ci.ingredient_id = i.id " +
          "WHERE c.author_id = ?";

  @Override
  public List<Cocktail> findAll() {
    Map<Long, Cocktail> cocktailMap = new LinkedHashMap<>();
    Connection connection = ConnectionPool.getInstance().getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_COCKTAILS);
         ResultSet resultSet = preparedStatement.executeQuery()) {
      while (resultSet.next()) {
        Long id = resultSet.getLong("id");
        Cocktail cocktail = cocktailMap.computeIfAbsent(id, k -> {
          try {
            Cocktail c = new Cocktail();
            c.setId(id);
            c.setName(resultSet.getString("name"));
            c.setDescription(resultSet.getString("description"));
            c.setAuthorId(Long.valueOf(resultSet.getInt("author_id")));
            return c;
          } catch (SQLException e) {
            log.error("Error create Cocktail class");
            throw new RuntimeException(e);
          }
        });

        String ingredientName = resultSet.getString("ingredient_name");
        if (ingredientName != null) {
          cocktail.getIngredients().add(ingredientName + ": " + resultSet.getString("amount"));
        }
      }
    } catch (SQLException e) {
      log.error("Error finding all cocktails", e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
    return new ArrayList<>(cocktailMap.values());
  }

  @Override
  public boolean create(Cocktail cocktail, Map<String, String> ingredients) {
    Connection connection = ConnectionPool.getInstance().getConnection();
    try {
      connection.setAutoCommit(false);
      try (PreparedStatement psCocktail = connection.prepareStatement(CREATE_COCKTAIL, Statement.RETURN_GENERATED_KEYS)) {
        psCocktail.setString(1, cocktail.getName());
        psCocktail.setString(2, cocktail.getDescription());
        psCocktail.setLong(3, cocktail.getAuthorId());
        psCocktail.executeUpdate();

        long cocktailId;
        try (ResultSet keys = psCocktail.getGeneratedKeys()) {
          if (!keys.next()) throw new SQLException("Failed to get cocktail ID");
          cocktailId = keys.getLong(1);
        }

        try (PreparedStatement psRelation = connection.prepareStatement(CREATE_COCKTAIL_ING)) {
          for (Map.Entry<String, String> entry : ingredients.entrySet()) {
            String ingName = entry.getKey();
            String amount = entry.getValue();

            long ingredientId = getOrCreateIngredientId(connection, ingName);

            psRelation.setLong(1, cocktailId);
            psRelation.setLong(2, ingredientId);
            psRelation.setString(3, amount);
            psRelation.addBatch();
          }
          psRelation.executeBatch();
        }
      }

      connection.commit();
      return true;
    } catch (SQLException e) {
      try { if (connection != null) connection.rollback(); }
      catch (SQLException ex) { log.error("Rollback failed", ex); }
      log.error("Error in transaction", e);
      return false;
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
  }

  private long getOrCreateIngredientId(Connection conn, String name) throws SQLException {
    try (PreparedStatement ps = conn.prepareStatement(SELECT_NAME_FROM_ING)) {
      ps.setString(1, name);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) return rs.getLong(1);
      }
    }
    try (PreparedStatement ps = conn.prepareStatement(CREATE_ING, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, name);
      ps.executeUpdate();
      try (ResultSet keys = ps.getGeneratedKeys()) {
        if (keys.next()) return keys.getLong(1);
      }
    }
    throw new SQLException("Could not find or create ingredient: " + name);
  }

  @Override
  public double getAverageRating(int cocktailId) {
    Connection connection = ConnectionPool.getInstance().getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(GET_AVERAGE_RATING)) {
      preparedStatement.setInt(1, cocktailId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        if (resultSet.next()) {
          double avg = resultSet.getDouble(1);
          return resultSet.wasNull() ? 0.0 : avg;
        }
      }
    } catch (SQLException e) {
      log.error("Error getting average rating for cocktail: {}", cocktailId, e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
    return 0.0;
  }

  @Override
  public List<Cocktail> findByAuthorId(Long authorId) {
    Map<Long, Cocktail> cocktailMap = new LinkedHashMap<>();
    Connection connection = ConnectionPool.getInstance().getConnection();
    try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COCKTAILS_BY_AUTHOR)) {
      preparedStatement.setLong(1, authorId);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        while (resultSet.next()) {
          Long id = resultSet.getLong("id");
          Cocktail cocktail = cocktailMap.computeIfAbsent(id, k -> {
            try {
              Cocktail c = new Cocktail();
              c.setId(id);
              c.setName(resultSet.getString("name"));
              c.setDescription(resultSet.getString("description"));
              c.setAuthorId(Long.valueOf(resultSet.getInt("author_id")));
              return c;
            } catch (SQLException e) {
              log.error("Error create Cocktail class");
              throw new RuntimeException(e);
            }
          });

          String ingredientName = resultSet.getString("ingredient_name");
          if (ingredientName != null) {
            cocktail.getIngredients().add(ingredientName + ": " + resultSet.getString("amount"));
          }
        }
      }
    } catch (SQLException e) {
      log.error("Error finding cocktails by author id: {}", authorId, e);
    } finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
    return new ArrayList<>(cocktailMap.values());
  }
}

