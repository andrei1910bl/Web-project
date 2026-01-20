package com.bulavskiy.demo.controller;

import com.bulavskiy.demo.model.User;
import com.bulavskiy.demo.service.RatingService;
import com.bulavskiy.demo.service.impl.RatingServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@WebServlet("/rating")
public class RatingController extends HttpServlet {
  private static final Logger log = LogManager.getLogger();
  private final RatingService ratingService = new RatingServiceImpl();

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    jakarta.servlet.http.HttpSession session = req.getSession(false);
    User user = (session != null) ? (User) session.getAttribute("user") : null;
    
    if (user == null || user.getId() == null) {
      resp.sendRedirect(req.getContextPath() + "/main");
      return;
    }

    String cocktailIdParam = req.getParameter("cocktailId");
    String scoreParam = req.getParameter("score");

    if (cocktailIdParam == null || scoreParam == null) {
      log.warn("Missing parameters: cocktailId={}, score={}", cocktailIdParam, scoreParam);
      resp.sendRedirect(req.getContextPath() + "/main");
      return;
    }

    try {
      int cocktailId = Integer.parseInt(cocktailIdParam);
      int score = Integer.parseInt(scoreParam);
      
      if (ratingService.addOrUpdateRating(user.getId().intValue(), cocktailId, score)) {
        log.info("User {} rated cocktail {} with score {}", user.getLogin(), cocktailId, score);
      }
    } catch (NumberFormatException e) {
      log.error("Invalid parameters: cocktailId={}, score={}", cocktailIdParam, scoreParam, e);
    }

    resp.sendRedirect(req.getContextPath() + "/main");
  }
}

