<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Регистрация</title></head>
<body>
<h2>Регистрация нового бармена/гостя</h2>
<form action="register" method="post">
    Логин: <input type="text" name="login" required><br>
    Пароль: <input type="password" name="password" required><br>
    Повторите пароль: <input type="password" name="confirmPassword" required><br>
    <button type="submit">Зарегистрироваться</button>
</form>
<p>Уже есть аккаунт? <a href="login">Войти</a></p>
</body>
</html>