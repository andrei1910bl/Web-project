package com.bulavskiy.demo.service.impl;

import com.bulavskiy.demo.dao.impl.UserDaoImpl;
import com.bulavskiy.demo.service.UserService;

public class UserServiceImpl implements UserService {
  private static UserServiceImpl instance = new UserServiceImpl();

  private UserServiceImpl() {
  }

  public static UserServiceImpl getInstance() {
    return instance;
  }

  @Override
  public boolean authenticate(String login, String password) {
    UserDaoImpl userDao = UserDaoImpl.getInstance();
    boolean match = userDao.authenticate(login, password);
    return match;
  }
}
