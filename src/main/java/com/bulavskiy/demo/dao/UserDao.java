package com.bulavskiy.demo.dao;

import com.bulavskiy.demo.model.User;
import com.bulavskiy.demo.model.UserStatistics;

import java.util.List;
import java.util.Optional;

public interface UserDao{
  Optional<User> findUserByLogin(String login);
  List<UserStatistics> findAllUsersWithStatistics();
  boolean updateUserRole(Long userId, String role);
  Optional<User> findUserById(Long userId);
}
