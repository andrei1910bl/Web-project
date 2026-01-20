package com.bulavskiy.demo.controller;

import com.bulavskiy.demo.dao.CocktailDao;
import com.bulavskiy.demo.dao.impl.CocktailDaoImpl;
import com.bulavskiy.demo.model.Cocktail;
import com.bulavskiy.demo.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/create-cocktail")
public class CreateCocktailController extends HttpServlet {
  private final CocktailDao cocktailDao = new CocktailDaoImpl();

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.getRequestDispatcher("/pages/jsp/create-cocktail.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String name = req.getParameter("name");
    String description = req.getParameter("description");
    User user = (User) req.getSession().getAttribute("user");

    Cocktail cocktail = new Cocktail();
    cocktail.setName(name);
    cocktail.setDescription(description);
    cocktail.setAuthorId(user.getId());

    Map<String, String> ingredients = new HashMap<>();
    if (!req.getParameter("ing_name_1").isEmpty()) {
      ingredients.put(req.getParameter("ing_name_1"), req.getParameter("ing_amount_1"));
    }
    if (!req.getParameter("ing_name_2").isEmpty()) {
      ingredients.put(req.getParameter("ing_name_2"), req.getParameter("ing_amount_2"));
    }
    if (cocktailDao.create(cocktail, ingredients)) {
      resp.sendRedirect(req.getContextPath() + "/main");
    } else {
      req.setAttribute("errorMessage", "Ошибка при сохранении коктейля");
      req.getRequestDispatcher("/pages/jsp/create-cocktail.jsp").forward(req, resp);
    }
  }
}
