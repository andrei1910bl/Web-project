package com.bulavskiy.demo.dao;

public interface UserDao {
  boolean authenticate(String login, String password);
}
