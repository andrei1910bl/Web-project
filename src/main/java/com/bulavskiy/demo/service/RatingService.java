package com.bulavskiy.demo.service;

public interface RatingService {
  boolean addOrUpdateRating(int userId, int cocktailId, int score);
  int getUserRating(int userId, int cocktailId);
  boolean validateScore(int score);
}

