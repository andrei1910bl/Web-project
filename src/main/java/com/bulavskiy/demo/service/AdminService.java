package com.bulavskiy.demo.service;

import com.bulavskiy.demo.model.UserStatistics;

import java.util.List;

public interface AdminService {
  List<UserStatistics> getAllUsersWithStatistics();
  boolean updateUserRole(Long userId, String newRole);
}

