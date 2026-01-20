package com.bulavskiy.demo.service.impl;

import com.bulavskiy.demo.dao.UserDao;
import com.bulavskiy.demo.dao.impl.UserDaoImpl;
import com.bulavskiy.demo.model.Role;
import com.bulavskiy.demo.model.User;
import com.bulavskiy.demo.service.PassHasherService;
import com.bulavskiy.demo.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UserServiceImpl implements UserService {
  private static final Logger log = LogManager.getLogger();
  private static final UserDaoImpl userDao = new UserDaoImpl();

  @Override
  public Optional<User> authenticate(String login, String password) {
    Optional<User> userDb = userDao.findUserByLogin(login);
    if(userDb.isPresent()) {
      String hashedInput = PassHasherService.hash(password);
      if(userDb.get().getPassword().equals(hashedInput)){
        return userDb;
      }
    }
    return Optional.empty();
  }

  @Override
  public boolean register(User user) {
    String hashedPassword = PassHasherService.hash(user.getPassword());
    user.setPassword(hashedPassword);
    user.setRole(Role.USER);
    return userDao.insert(user);
  }
}
