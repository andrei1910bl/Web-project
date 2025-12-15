package com.bulavskiy.demo.command.impl;

import com.bulavskiy.demo.command.Command;
import jakarta.servlet.http.HttpServletRequest;

public class DefaultCommand implements Command {
  @Override
  public String execute(HttpServletRequest request) {
    return "index.jsp";
  }
}
