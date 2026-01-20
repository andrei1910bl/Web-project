package com.bulavskiy.demo.controller;

import com.bulavskiy.demo.model.User;
import com.bulavskiy.demo.service.CocktailService;
import com.bulavskiy.demo.service.impl.CocktailServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;

@WebServlet("/main")
public class MainController extends HttpServlet {
  private static final Logger log = LogManager.getLogger();
  private final CocktailService cocktailService = new CocktailServiceImpl();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    log.info("Accessing main page");
    
    var cocktails = cocktailService.getAllCocktails();
    
    jakarta.servlet.http.HttpSession session = req.getSession(false);
    User user = (session != null) ? (User) session.getAttribute("user") : null;
    
    Map<Long, Double> ratings = cocktailService.getAllCocktailsRatings(cocktails);
    Map<Long, Integer> userRatings = cocktailService.getUserRatingsForCocktails(
        user != null ? user.getId() : null, cocktails);
    
    req.setAttribute("cocktailList", cocktails);
    req.setAttribute("ratings", ratings);
    req.setAttribute("userRatings", userRatings);
    req.getRequestDispatcher("/pages/jsp/main.jsp").forward(req, resp);
  }
}
