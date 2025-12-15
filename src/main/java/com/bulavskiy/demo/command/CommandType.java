package com.bulavskiy.demo.command;

import com.bulavskiy.demo.command.impl.AddUserCommand;
import com.bulavskiy.demo.command.impl.DefaultCommand;
import com.bulavskiy.demo.command.impl.LoginCommand;
import com.bulavskiy.demo.command.impl.LogoutCommand;

public enum CommandType {
  ADD_USER(new AddUserCommand()),
  LOGIN(new LoginCommand()),
  LOGOUT(new LogoutCommand()),
  DEFAULT(new DefaultCommand());
  Command command;

  CommandType(Command command) {
    this.command = command;
  }

  public static Command define(String commandStr){
    CommandType current = CommandType.valueOf(commandStr.toUpperCase());
    return current.command;
  }
}
