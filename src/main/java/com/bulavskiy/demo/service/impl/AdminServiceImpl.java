package com.bulavskiy.demo.service.impl;

import com.bulavskiy.demo.dao.UserDao;
import com.bulavskiy.demo.dao.impl.UserDaoImpl;
import com.bulavskiy.demo.model.UserStatistics;
import com.bulavskiy.demo.service.AdminService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class AdminServiceImpl implements AdminService {
  private static final Logger log = LogManager.getLogger();
  private final UserDao userDao = new UserDaoImpl();

  @Override
  public List<UserStatistics> getAllUsersWithStatistics() {
    return userDao.findAllUsersWithStatistics();
  }

  @Override
  public boolean updateUserRole(Long userId, String newRole) {
    if (userId == null || newRole == null || newRole.trim().isEmpty()) {
      log.warn("Invalid parameters for role update: userId={}, newRole={}", userId, newRole);
      return false;
    }
    
    String normalizedRole = newRole.toLowerCase().trim();
    boolean success = userDao.updateUserRole(userId, normalizedRole);
    
    if (success) {
      log.info("User role updated: userId={}, newRole={}", userId, normalizedRole);
    } else {
      log.error("Failed to update user role: userId={}, newRole={}", userId, normalizedRole);
    }
    
    return success;
  }
}

