package com.bulavskiy.demo.controller;

import com.bulavskiy.demo.model.Role;
import com.bulavskiy.demo.model.User;
import com.bulavskiy.demo.service.UserService;
import com.bulavskiy.demo.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/register")
public class RegistrationController extends HttpServlet {
  private final UserService userService = new UserServiceImpl();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.getRequestDispatcher("/pages/jsp/registration.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String login = req.getParameter("login");
    String password = req.getParameter("password");
    String confirmPassword = req.getParameter("confirmPassword");

    if (password == null || !password.equals(confirmPassword)) {
      req.setAttribute("errorMessage", "Пароли не совпадают!");
      req.getRequestDispatcher("/pages/jsp/registration.jsp").forward(req, resp);
      return;
    }

    User newUser = new User(login, password, Role.USER);

    if (userService.register(newUser)) {
      resp.sendRedirect(req.getContextPath() + "/login?success=true");
    } else {
      req.setAttribute("errorMessage", "Логин уже занят или ошибка БД");
      req.getRequestDispatcher("/pages/jsp/registration.jsp").forward(req, resp);
    }
  }
}
