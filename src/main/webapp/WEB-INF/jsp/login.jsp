<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
  <title>Вход | Помощник бармена</title>
</head>
<body>
<h2>Авторизация</h2>

<c:if test="${not empty errorMessage}">
  <p style="color: red;">${errorMessage}</p>
</c:if>

<form action="${pageContext.request.contextPath}/login" method="post">
  <label>Логин:</label>
  <input type="text" name="login" required>
  <br>
  <label>Пароль:</label>
  <input type="password" name="password" required>
  <br>
  <button type="submit">Войти</button>
</form>

<p>Нет аккаунта? <a href="register.jsp">Зарегистрироваться</a></p>
</body>
</html>