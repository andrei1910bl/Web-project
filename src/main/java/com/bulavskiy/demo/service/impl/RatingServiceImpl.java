package com.bulavskiy.demo.service.impl;

import com.bulavskiy.demo.dao.RatingDao;
import com.bulavskiy.demo.dao.impl.RatingDaoImpl;
import com.bulavskiy.demo.model.Rating;
import com.bulavskiy.demo.service.RatingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RatingServiceImpl implements RatingService {
  private static final Logger log = LogManager.getLogger();
  private final RatingDao ratingDao = new RatingDaoImpl();
  private static final int MIN_SCORE = 1;
  private static final int MAX_SCORE = 5;

  @Override
  public boolean validateScore(int score) {
    return score >= MIN_SCORE && score <= MAX_SCORE;
  }

  @Override
  public boolean addOrUpdateRating(int userId, int cocktailId, int score) {
    if (!validateScore(score)) {
      log.warn("Invalid score value: {} (must be between {} and {})", score, MIN_SCORE, MAX_SCORE);
      return false;
    }
    
    if (userId <= 0 || cocktailId <= 0) {
      log.warn("Invalid parameters: userId={}, cocktailId={}", userId, cocktailId);
      return false;
    }
    
    try {
      Rating rating = new Rating(userId, cocktailId, score);
      boolean success = ratingDao.addOrUpdateRating(rating);
      
      if (success) {
        log.info("Rating added/updated: userId={}, cocktailId={}, score={}", userId, cocktailId, score);
      } else {
        log.error("Failed to add/update rating: userId={}, cocktailId={}, score={}", userId, cocktailId, score);
      }
      
      return success;
    } catch (Exception e) {
      log.error("Error adding/updating rating: userId={}, cocktailId={}, score={}", userId, cocktailId, score, e);
      return false;
    }
  }

  @Override
  public int getUserRating(int userId, int cocktailId) {
    if (userId <= 0 || cocktailId <= 0) {
      return 0;
    }
    
    try {
      return ratingDao.getUserRating(userId, cocktailId);
    } catch (Exception e) {
      log.error("Error getting user rating: userId={}, cocktailId={}", userId, cocktailId, e);
      return 0;
    }
  }
}

