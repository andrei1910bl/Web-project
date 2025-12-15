package com.bulavskiy.demo.command.impl;

import com.bulavskiy.demo.command.Command;
import jakarta.servlet.http.HttpServletRequest;

public class AddUserCommand implements Command {
  @Override
  public String execute(HttpServletRequest request) {
    return "";
  }
}
