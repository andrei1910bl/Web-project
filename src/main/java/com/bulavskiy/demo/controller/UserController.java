package com.bulavskiy.demo.controller;

import com.bulavskiy.demo.model.User;
import com.bulavskiy.demo.service.UserService;
import com.bulavskiy.demo.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Optional;


@WebServlet("/login")
public class UserController extends HttpServlet {
  private static final Logger log = LogManager.getLogger();
  private static final UserService userService = new UserServiceImpl();

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.getRequestDispatcher("/pages/jsp/login.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String login = req.getParameter("login");
    String password = req.getParameter("password");
    log.info("DEBUG: Attempting login for [{}] with password [{}]", login, password);
    Optional<User> user = userService.authenticate(login, password);
    if(user.isPresent()){
      HttpSession session = req.getSession();
      session.setAttribute("user", user.get());
      session.setAttribute("role", user.get().getRole());
      log.info("User {} logged successfully", login);
      resp.sendRedirect(req.getContextPath() + "/main");
    }else {
      log.info("Failed login attempt for user: {}", login);
      req.setAttribute("errorMassage", "Неверный логин или пароль");
//     todo
      req.getRequestDispatcher("/pages/jsp/login.jsp").forward(req, resp);
    }
  }
}
