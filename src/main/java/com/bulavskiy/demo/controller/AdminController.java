package com.bulavskiy.demo.controller;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bulavskiy.demo.model.Role;
import com.bulavskiy.demo.model.User;
import com.bulavskiy.demo.model.UserStatistics;
import com.bulavskiy.demo.service.AdminService;
import com.bulavskiy.demo.service.impl.AdminServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin")
public class AdminController extends HttpServlet {
  private static final Logger log = LogManager.getLogger();
  private final AdminService adminService = new AdminServiceImpl();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    jakarta.servlet.http.HttpSession session = req.getSession(false);
    User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
    
    if (!isAdmin(currentUser)) {
      resp.sendRedirect(req.getContextPath() + "/main");
      return;
    }

    List<UserStatistics> usersStatistics = adminService.getAllUsersWithStatistics();
    req.setAttribute("usersStatistics", usersStatistics);
    req.getRequestDispatcher("/pages/jsp/admin-panel.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    jakarta.servlet.http.HttpSession session = req.getSession(false);
    User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
    
    if (!isAdmin(currentUser)) {
      resp.sendRedirect(req.getContextPath() + "/main");
      return;
    }

    String userIdParam = req.getParameter("userId");
    String newRoleParam = req.getParameter("newRole");

    if (userIdParam != null && newRoleParam != null) {
      try {
        Long userId = Long.parseLong(userIdParam);
        adminService.updateUserRole(userId, newRoleParam);
      } catch (NumberFormatException e) {
        log.error("Invalid user ID: {}", userIdParam, e);
      }
    }

    resp.sendRedirect(req.getContextPath() + "/admin");
  }

  private boolean isAdmin(User user) {
    return user != null && user.getRole() == Role.ADMIN;
  }
}

