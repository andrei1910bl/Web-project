package com.bulavskiy.demo.dao;

import com.bulavskiy.demo.model.User;

import java.util.Optional;

public interface UserDao{
  Optional<User> findUserByLogin(String login);
}
