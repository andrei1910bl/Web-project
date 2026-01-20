<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>Мой профиль</title>
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
        .cocktail-card {
            border: 1px solid #ccc;
            padding: 15px;
            margin: 10px 0;
            border-radius: 5px;
            background-color: #f9f9f9;
        }
        .rating {
            color: #FFA500;
            font-weight: bold;
        }
    </style>
</head>
<body>
<nav class="nav">
    Привет, ${user.login} (${user.role}) |
    <a href="${pageContext.request.contextPath}/main">Все коктейли</a> |
    <a href="${pageContext.request.contextPath}/profile">Мой профиль</a> |
    <c:if test="${user.role.name() == 'ADMIN'}">
        <a href="${pageContext.request.contextPath}/admin">Админ-панель</a> |
    </c:if>
    <a href="${pageContext.request.contextPath}/logout">Выйти</a>
</nav>

<h1>Мой профиль - Мои коктейли</h1>

<p><strong>Логин:</strong> ${user.login}</p>
<p><strong>Роль:</strong> ${user.role.name()}</p>

<h2>Мои предложенные коктейли (${fn:length(myCocktails)})</h2>

<c:if test="${empty myCocktails}">
    <p><i>Вы еще не предложили ни одного рецепта.</i></p>
</c:if>

<c:if test="${not empty myCocktails}">
    <c:forEach var="cocktail" items="${myCocktails}">
        <div class="cocktail-card">
            <h3>${cocktail.name}</h3>
            <p><i>Описание:</i> ${cocktail.description}</p>
            
            <p class="rating">
                <c:set var="rating" value="${ratings[cocktail.id]}"/>
                <c:choose>
                    <c:when test="${rating != null && rating > 0}">
                        ★ Средний рейтинг: <fmt:formatNumber value="${rating}" pattern="#.##"/> / 5.0
                    </c:when>
                    <c:otherwise>
                        ★ Пока нет оценок
                    </c:otherwise>
                </c:choose>
            </p>

            <strong>Состав:</strong>
            <ul>
                <c:forEach var="ingredient" items="${cocktail.ingredients}">
                    <li>${ingredient}</li>
                </c:forEach>
            </ul>
        </div>
    </c:forEach>
</c:if>

<c:if test="${user.role.name() == 'BARTENDER' || user.role.name() == 'ADMIN'}">
    <div style="margin-top: 20px;">
        <a href="${pageContext.request.contextPath}/create-cocktail"
           style="background: green; color: white; padding: 10px; text-decoration: none; border-radius: 5px;">
            + Добавить новый коктейль
        </a>
    </div>
</c:if>
</body>
</html>

