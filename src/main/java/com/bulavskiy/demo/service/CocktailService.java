package com.bulavskiy.demo.service;

import com.bulavskiy.demo.model.Cocktail;

import java.util.List;
import java.util.Map;

public interface CocktailService {
  List<Cocktail> getAllCocktails();
  Map<Long, Double> getAllCocktailsRatings(List<Cocktail> cocktails);
  Map<Long, Integer> getUserRatingsForCocktails(Long userId, List<Cocktail> cocktails);
}

