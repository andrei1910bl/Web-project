package com.bulavskiy.demo.model;

public enum Role {
  USER,
  BARTENDER,
  ADMIN;

  public static Role fromString(String role){
    return Role.valueOf(role.toUpperCase());
  }
}
