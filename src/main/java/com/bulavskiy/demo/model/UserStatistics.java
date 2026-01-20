package com.bulavskiy.demo.model;

public class UserStatistics {
  private User user;
  private int cocktailCount;
  private double averageRating;

  public UserStatistics(User user, int cocktailCount, double averageRating) {
    this.user = user;
    this.cocktailCount = cocktailCount;
    this.averageRating = averageRating;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public int getCocktailCount() {
    return cocktailCount;
  }

  public void setCocktailCount(int cocktailCount) {
    this.cocktailCount = cocktailCount;
  }

  public double getAverageRating() {
    return averageRating;
  }

  public void setAverageRating(double averageRating) {
    this.averageRating = averageRating;
  }
}

