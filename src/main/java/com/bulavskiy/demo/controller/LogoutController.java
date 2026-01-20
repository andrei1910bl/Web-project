package com.bulavskiy.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet("/logout")
public class LogoutController extends HttpServlet {
  private static final Logger log = LogManager.getLogger();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    HttpSession session = req.getSession(false);
    if (session != null) {
      String login = session.getAttribute("user") != null 
          ? ((com.bulavskiy.demo.model.User) session.getAttribute("user")).getLogin() 
          : "unknown";
      session.invalidate();
      log.info("User {} logged out", login);
    }
    resp.sendRedirect(req.getContextPath() + "/login");
  }
}

