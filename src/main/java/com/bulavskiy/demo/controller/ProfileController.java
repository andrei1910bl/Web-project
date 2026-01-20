package com.bulavskiy.demo.controller;

import com.bulavskiy.demo.model.User;
import com.bulavskiy.demo.service.ProfileService;
import com.bulavskiy.demo.service.impl.ProfileServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;

@WebServlet("/profile")
public class ProfileController extends HttpServlet {
  private static final Logger log = LogManager.getLogger();
  private final ProfileService profileService = new ProfileServiceImpl();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    jakarta.servlet.http.HttpSession session = req.getSession(false);
    User user = (session != null) ? (User) session.getAttribute("user") : null;
    
    if (user == null || user.getId() == null) {
      resp.sendRedirect(req.getContextPath() + "/main");
      return;
    }

    try {
      var myCocktails = profileService.getUserCocktails(user.getId());
      Map<Long, Double> ratings = profileService.getCocktailsRatings(myCocktails);
      
      req.setAttribute("myCocktails", myCocktails);
      req.setAttribute("ratings", ratings);
      req.getRequestDispatcher("/pages/jsp/profile.jsp").forward(req, resp);
    } catch (Exception e) {
      log.error("Error loading profile for user: {}", user.getLogin(), e);
      resp.sendRedirect(req.getContextPath() + "/main");
    }
  }
}

