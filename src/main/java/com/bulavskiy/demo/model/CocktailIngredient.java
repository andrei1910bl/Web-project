package com.bulavskiy.demo.model;

public class CocktailIngredient {
  private int cocktailId;
  private int ingredientId;
  private String amount;

  public CocktailIngredient(int cocktailId, int ingredientId, String amount) {
    this.cocktailId = cocktailId;
    this.ingredientId = ingredientId;
    this.amount = amount;
  }

  public int getCocktailId() {
    return cocktailId;
  }

  public void setCocktailId(int cocktailId) {
    this.cocktailId = cocktailId;
  }

  public int getIngredientId() {
    return ingredientId;
  }

  public void setIngredientId(int ingredientId) {
    this.ingredientId = ingredientId;
  }

  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }
}
