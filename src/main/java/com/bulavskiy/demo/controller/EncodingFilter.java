package com.bulavskiy.demo.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import java.io.IOException;

@WebFilter(urlPatterns = "/*", initParams = {
        @WebInitParam(name = "encoding", value = "UTF-8")
})
public class EncodingFilter implements Filter {
  private String code;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    code = filterConfig.getInitParameter("encoding");
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {
    request.setCharacterEncoding(code);
    response.setCharacterEncoding(code);
    chain.doFilter(request, response);
  }

  @Override
  public void destroy() {
    code = null;
  }
}