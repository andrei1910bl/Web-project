package com.bulavskiy.demo.model;

public class Rating {
  private Long id;
  private int userId;
  private int cocktailId;
  private int score;

  public Rating(Long id, int userId, int cocktailId, int score) {
    this.id = id;
    this.userId = userId;
    this.cocktailId = cocktailId;
    this.score = score;
  }

  public Rating(int userId, int cocktailId, int score) {
    this.userId = userId;
    this.cocktailId = cocktailId;
    this.score = score;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getCocktailId() {
    return cocktailId;
  }

  public void setCocktailId(int cocktailId) {
    this.cocktailId = cocktailId;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }
}
