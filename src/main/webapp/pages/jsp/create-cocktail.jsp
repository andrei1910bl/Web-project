<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head><title>Новый коктейль</title></head>
<body>
<h2>Создание нового шедевра 🍹</h2>
<form action="create-cocktail" method="post">
    Название: <input type="text" name="name" required><br><br>
    Описание: <textarea name="description"></textarea><br><br>

    <h4>Ингредиенты:</h4>
    1. Название: <input type="text" name="ing_name_1">
    Кол-во: <input type="text" name="ing_amount_1"><br>

    2. Название: <input type="text" name="ing_name_2">
    Кол-во: <input type="text" name="ing_amount_2"><br><br>

    <button type="submit">Сохранить рецепт</button>
</form>
</body>
</html>