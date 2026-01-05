package com.bulavskiy.demo.dao;

import com.bulavskiy.demo.model.Cocktail;

import java.util.List;
import java.util.Map;

public interface CocktailDao {

  List<Cocktail> findAll();
  boolean create(Cocktail cocktail, Map<Long, String> ingredients);
}
