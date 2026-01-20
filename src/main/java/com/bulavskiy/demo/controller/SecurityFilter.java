package com.bulavskiy.demo.controller;

import com.bulavskiy.demo.model.Role;
import com.bulavskiy.demo.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/main", "/create-cocktail", "/edit/*", "/delete/*"})
public class SecurityFilter implements Filter {
  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) servletRequest;
    HttpServletResponse resp = (HttpServletResponse) servletResponse;
    HttpSession session = req.getSession(false);
    User user = (session != null) ? (User) session.getAttribute("user") : null;

    if (user == null) {
      resp.sendRedirect(req.getContextPath() + "/login");
      return;
    }
    String path = req.getServletPath();
    Role role = user.getRole();

    if (path.equals("/create-cocktail")) {
      if (role != Role.BARTENDER && role != Role.ADMIN) {
        resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        return;
      }
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }
}

