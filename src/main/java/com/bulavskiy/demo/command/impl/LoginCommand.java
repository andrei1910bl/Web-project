package com.bulavskiy.demo.command.impl;

import com.bulavskiy.demo.command.Command;
import com.bulavskiy.demo.service.UserService;
import com.bulavskiy.demo.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;

public class LoginCommand implements Command {
  @Override
  public String execute(HttpServletRequest request) {
    String login = request.getParameter("login");
    String password = request.getParameter("pass");
    UserService userService = UserServiceImpl.getInstance();
    String page;
    if (userService.authenticate(login, password)){
      request.setAttribute("user", login);
      page = "pages/main.jsp";
    }else {
      request.setAttribute("login_msg", "incorrect login or pass");
      page = "index.jsp";
    }
    return page;
  }
}
