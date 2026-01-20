package com.bulavskiy.demo.model;

import java.util.ArrayList;
import java.util.List;

public class Cocktail {
  private Long id;
  private String name;
  private String description;
  private Long authorId;
  private List<String> ingredients = new ArrayList<>();

  public Cocktail(Long id, String name, String description, Long authorId, List<String> ingredients) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.authorId = authorId;
    this.ingredients = ingredients;
  }

  public Cocktail(String name, String description, Long authorId, List<String> ingredients) {
    this.name = name;
    this.description = description;
    this.authorId = authorId;
    this.ingredients = ingredients;
  }

  public Cocktail() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Long getAuthorId() {
    return authorId;
  }

  public void setAuthorId(Long authorId) {
    this.authorId = authorId;
  }

  public List<String> getIngredients() {
    return ingredients;
  }

  public void setIngredients(List<String> ingredients) {
    this.ingredients = ingredients;
  }
}
