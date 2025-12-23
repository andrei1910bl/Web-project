package com.bulavskiy.demo.dao;


import java.util.List;

public abstract class BaseDao<T> {
  public abstract boolean insert(T t);
  public abstract boolean delete(T t);
  public abstract List<T> findAll();
  public abstract T update(T t);
}
