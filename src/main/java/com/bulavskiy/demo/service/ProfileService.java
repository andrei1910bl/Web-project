package com.bulavskiy.demo.service;

import com.bulavskiy.demo.model.Cocktail;

import java.util.List;
import java.util.Map;

public interface ProfileService {
  List<Cocktail> getUserCocktails(Long userId);
  Map<Long, Double> getCocktailsRatings(List<Cocktail> cocktails);
}

