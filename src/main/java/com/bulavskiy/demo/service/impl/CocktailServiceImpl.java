package com.bulavskiy.demo.service.impl;

import com.bulavskiy.demo.dao.CocktailDao;
import com.bulavskiy.demo.dao.impl.CocktailDaoImpl;
import com.bulavskiy.demo.model.Cocktail;
import com.bulavskiy.demo.service.CocktailService;
import com.bulavskiy.demo.service.RatingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CocktailServiceImpl implements CocktailService {
  private static final Logger log = LogManager.getLogger();
  private final CocktailDao cocktailDao = new CocktailDaoImpl();
  private final RatingService ratingService = new RatingServiceImpl();

  @Override
  public List<Cocktail> getAllCocktails() {
    try {
      return cocktailDao.findAll();
    } catch (Exception e) {
      log.error("Error getting all cocktails", e);
      return List.of();
    }
  }

  @Override
  public Map<Long, Double> getAllCocktailsRatings(List<Cocktail> cocktails) {
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

  @Override
  public Map<Long, Integer> getUserRatingsForCocktails(Long userId, List<Cocktail> cocktails) {
    Map<Long, Integer> userRatings = new HashMap<>();
    
    if (userId == null || cocktails == null || cocktails.isEmpty()) {
      return userRatings;
    }
    
    for (Cocktail cocktail : cocktails) {
      if (cocktail != null && cocktail.getId() != null) {
        try {
          int userRating = ratingService.getUserRating(userId.intValue(), cocktail.getId().intValue());
          if (userRating > 0) {
            userRatings.put(cocktail.getId(), userRating);
          }
        } catch (Exception e) {
          log.error("Error getting user rating for cocktail: userId={}, cocktailId={}", userId, cocktail.getId(), e);
        }
      }
    }
    
    return userRatings;
  }
}

