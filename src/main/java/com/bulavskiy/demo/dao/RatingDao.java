package com.bulavskiy.demo.dao;

import java.util.Optional;

import com.bulavskiy.demo.model.Rating;

public interface RatingDao {
  boolean addOrUpdateRating(Rating rating);
  Optional<Rating> findRatingByUserAndCocktail(int userId, int cocktailId);
  int getUserRating(int userId, int cocktailId);
}

