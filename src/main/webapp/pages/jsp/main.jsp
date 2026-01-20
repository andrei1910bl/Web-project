<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Главная - Коктейли</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        .nav {
            margin-bottom: 20px;
            padding: 10px;
            background-color: #f1f1f1;
            border-radius: 5px;
        }
        .card {
            border: 1px solid #ccc;
            padding: 15px;
            margin: 10px 0;
            border-radius: 5px;
            background-color: #f9f9f9;
        }
        .rating-section {
            margin-top: 10px;
            padding: 10px;
            background-color: #fff;
            border-radius: 3px;
        }
        .rating-display {
            color: #FFA500;
            font-weight: bold;
            margin-bottom: 10px;
        }
        .rating-stars {
            display: inline-block;
        }
        .rating-stars button {
            background: none;
            border: none;
            font-size: 20px;
            cursor: pointer;
            padding: 0 2px;
        }
        .rating-stars button:hover {
            color: #FFA500;
        }
        .active-star {
            color: #FFA500;
        }
    </style>
</head>
<body>
<nav class="nav">
    <c:if test="${not empty user}">
        Привет, ${user.login} (${user.role}) |
        <a href="${pageContext.request.contextPath}/main">Все коктейли</a> |
        <a href="${pageContext.request.contextPath}/profile">Мой профиль</a> |
        <c:if test="${user.role.name() == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/admin">Админ-панель</a> |
        </c:if>
        <a href="${pageContext.request.contextPath}/logout">Выйти</a>
    </c:if>
    <c:if test="${empty user}">
        <a href="${pageContext.request.contextPath}/login">Войти</a> |
        <a href="${pageContext.request.contextPath}/register">Регистрация</a>
    </c:if>
</nav>

<c:if test="${user.role.name() == 'BARTENDER' || user.role.name() == 'ADMIN'}">
    <div style="margin: 20px 0;">
        <a href="${pageContext.request.contextPath}/create-cocktail"
           style="background: green; color: white; padding: 10px; text-decoration: none; border-radius: 5px;">
            + Добавить новый коктейль
        </a>
    </div>
</c:if>

<c:if test="${sessionScope.user.role.name() == 'USER'}">
    <p><i>Вы зашли как гость. Для создания рецептов обратитесь к администратору.</i></p>
</c:if>

<h1>Доступные коктейли 🍹</h1>

<c:if test="${empty cocktailList}">
    <p>Бар пока пуст. Зайдите позже!</p>
</c:if>

<div class="cocktail-container">
    <c:forEach var="cocktail" items="${cocktailList}">
        <div class="card">
            <h3>${cocktail.name}</h3>
            <p><i>Описание:</i> ${cocktail.description}</p>

            <strong>Состав:</strong>
            <ul>
                <c:forEach var="ingredient" items="${cocktail.ingredients}">
                    <li>${ingredient}</li>
                </c:forEach>
            </ul>

            <div class="rating-section">
                <c:set var="rating" value="${ratings[cocktail.id]}"/>
                <div class="rating-display">
                    <c:choose>
                        <c:when test="${rating != null && rating > 0}">
                            ★ Средний рейтинг: <fmt:formatNumber value="${rating}" pattern="#.##"/> / 5.0
                        </c:when>
                        <c:otherwise>
                            ★ Пока нет оценок
                        </c:otherwise>
                    </c:choose>
                </div>

                <c:if test="${not empty user}">
                    <c:set var="userRating" value="${userRatings[cocktail.id]}"/>
                    <c:if test="${userRating != null && userRating > 0}">
                        <p><small>Ваша оценка: ${userRating} из 5</small></p>
                    </c:if>
                    <form method="post" action="${pageContext.request.contextPath}/rating" style="display: inline;">
                        <input type="hidden" name="cocktailId" value="${cocktail.id}"/>
                        <div class="rating-stars">
                            Оценить:
                            <button type="submit" name="score" value="1" class="${userRating == 1 ? 'active-star' : ''}">★</button>
                            <button type="submit" name="score" value="2" class="${userRating == 2 ? 'active-star' : ''}">★</button>
                            <button type="submit" name="score" value="3" class="${userRating == 3 ? 'active-star' : ''}">★</button>
                            <button type="submit" name="score" value="4" class="${userRating == 4 ? 'active-star' : ''}">★</button>
                            <button type="submit" name="score" value="5" class="${userRating == 5 ? 'active-star' : ''}">★</button>
                        </div>
                    </form>
                </c:if>
            </div>
        </div>
    </c:forEach>
</div>
</body>
</html>