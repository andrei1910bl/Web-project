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
          "SELECT c.id, c.name, c.description, i.name as ingredient_name, ci.amount " +
          "FROM cocktails c " +
          "LEFT JOIN cocktail_ingredients ci ON c.id = ci.cocktail_id " +
          "LEFT JOIN ingredients i ON ci.ingredient_id = i.id";
  private static final String CREATE_COCKTAIL = "INSERT INTO cocktails (name, description) VALUES (?, ?)";
  private static final String CREATE_COCKTAIL_ING = "INSERT INTO cocktail_ingredients (cocktail_id, ingredient_id, amount) VALUES (?, ?, ?)";

  @Override
  public List<Cocktail> findAll() {
    Map<Long, Cocktail> cocktailMap = new LinkedHashMap<>();
    try (Connection connection = ConnectionPool.getInstance().getConnection();
      PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_COCKTAILS);
      ResultSet resultSet = preparedStatement.executeQuery()){
      while (resultSet.next()){
        Long id = resultSet.getLong("id");
        Cocktail cocktail = cocktailMap.computeIfAbsent(id, k ->{
          try {
            Cocktail c = new Cocktail();
            c.setId(id);
            c.setName(resultSet.getString("name"));
            c.setDescription(resultSet.getString("description"));
            c.setAuthorId(resultSet.getInt("author_id"));
            return c;
          } catch (SQLException e) {
            log.error("Error create Cockctail class");
            throw new RuntimeException(e);
          }
        });

        String ingredientName = resultSet.getString("ingredient_name");
        if(ingredientName != null){
          cocktail.getIngredients().add(ingredientName + ": "+ resultSet.getString("amount"));
        }
      }
    } catch (SQLException e) {
      log.info("Error finding all cocktails", e);
    }
    return new ArrayList<>(cocktailMap.values());
  }

  @Override
  public boolean create(Cocktail cocktail, Map<Long, String> ingredients) {
    Connection connection = ConnectionPool.getInstance().getConnection();
    try  {
      connection.setAutoCommit(false);
      try(PreparedStatement preparedStatement = connection.prepareStatement(CREATE_COCKTAIL, Statement.RETURN_GENERATED_KEYS)) {
        preparedStatement.setString(1, cocktail.getName());
        preparedStatement.setString(2, cocktail.getDescription());
        preparedStatement.executeUpdate();

        try(ResultSet keys = preparedStatement.getGeneratedKeys()) {
          if(keys.next()){
            Long cocktailId = keys.getLong(1);
            try (PreparedStatement psIng = connection.prepareStatement(CREATE_COCKTAIL_ING)){
              for (Map.Entry<Long, String> entry : ingredients.entrySet()){
                psIng.setLong(1, cocktailId);
                psIng.setLong(2, entry.getKey());
                psIng.setString(3, entry.getValue());
                psIng.addBatch();
              }
              psIng.executeUpdate();
            }
          }
        }
      }
      connection.commit();
      return true;
    }catch (SQLException e){
      try {
        connection.rollback();
      }catch (SQLException ex){
        log.error("Rollback failed", ex);
      }
      log.error("Error creating full cocktail recipe", e);
      return false;
    }
    finally {
      ConnectionPool.getInstance().releaseConnection(connection);
    }
  }
}

