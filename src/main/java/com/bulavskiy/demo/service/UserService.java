package com.bulavskiy.demo.service;

import com.bulavskiy.demo.model.User;

import java.util.Optional;

public interface UserService {
  Optional<User> authenticate(String login, String password);
  boolean register(User user);
}
