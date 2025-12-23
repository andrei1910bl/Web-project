package com.bulavskiy.demo.model;

public class Cocktail {
  private Long id;
  private String name;
  private String description;
  private int authorId;

  public Cocktail(Long id, String name, String description, int authorId) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.authorId = authorId;
  }

  public Cocktail(String name, String description, int authorId) {
    this.name = name;
    this.description = description;
    this.authorId = authorId;
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

  public int getAuthorId() {
    return authorId;
  }

  public void setAuthorId(int authorId) {
    this.authorId = authorId;
  }
}
