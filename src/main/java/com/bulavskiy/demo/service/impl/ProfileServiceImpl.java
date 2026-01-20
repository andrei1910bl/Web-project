package com.bulavskiy.demo.service.impl;

import com.bulavskiy.demo.dao.CocktailDao;
import com.bulavskiy.demo.dao.impl.CocktailDaoImpl;
import com.bulavskiy.demo.model.Cocktail;
import com.bulavskiy.demo.service.ProfileService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileServiceImpl implements ProfileService {
  private static final Logger log = LogManager.getLogger();
  private final CocktailDao cocktailDao = new CocktailDaoImpl();

  @Override
  public List<Cocktail> getUserCocktails(Long userId) {
    if (userId == null) {
      log.warn("Attempted to get cocktails for null userId");
      return List.of();
    }
    
    try {
      return cocktailDao.findByAuthorId(userId);
    } catch (Exception e) {
      log.error("Error getting cocktails for user: {}", userId, e);
      return List.of();
    }
  }

  @Override
  public Map<Long, Double> getCocktailsRatings(List<Cocktail> cocktails) {
    Map<Long, Double> ratings = new HashMap<>();
    
    if (cocktails == null || cocktails.isEmpty()) {
      return ratings;
    }
    
    for (Cocktail cocktail : cocktails) {
      if (cocktail != null && cocktail.getId() != null) {
        try {
          double avgRating = cocktailDao.getAverageRating(cocktail.getId().intValue());
          ratings.put(cocktail.getId(), avgRating);
        } catch (Exception e) {
          log.error("Error getting rating for cocktail: {}", cocktail.getId(), e);
        }
      }
    }
    
    return ratings;
  }
}

