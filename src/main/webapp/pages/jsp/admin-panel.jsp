<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Админ-панель</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #4CAF50;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        .btn {
            background-color: #4CAF50;
            color: white;
            padding: 5px 10px;
            text-decoration: none;
            border-radius: 3px;
            border: none;
            cursor: pointer;
        }
        .btn:hover {
            background-color: #45a049;
        }
        .nav {
            margin-bottom: 20px;
            padding: 10px;
            background-color: #f1f1f1;
            border-radius: 5px;
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

<h1>Админ-панель - Управление пользователями</h1>

<c:if test="${empty usersStatistics}">
    <p>Нет зарегистрированных пользователей.</p>
</c:if>

<c:if test="${not empty usersStatistics}">
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Логин</th>
            <th>Роль</th>
            <th>Количество рецептов</th>
            <th>Средний рейтинг</th>
            <th>Действия</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="userStat" items="${usersStatistics}">
            <tr>
                <td>${userStat.user.id}</td>
                <td>${userStat.user.login}</td>
                <td>${userStat.user.role.name()}</td>
                <td>${userStat.cocktailCount}</td>
                <td>
                    <c:choose>
                        <c:when test="${userStat.averageRating > 0}">
                            ${String.format("%.2f", userStat.averageRating)}
                        </c:when>
                        <c:otherwise>
                            Нет оценок
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:if test="${user.role.name() == 'ADMIN'}">
                        <%-- Не показываем действия для самого администратора --%>
                        <c:if test="${userStat.user.role.name() != 'ADMIN'}">
                            <%-- Кнопка для назначения барменом для любого клиента --%>
                            <c:if test="${userStat.user.role.name() == 'USER'}">
                                <form method="post" action="${pageContext.request.contextPath}/admin" style="display: inline;">
                                    <input type="hidden" name="userId" value="${userStat.user.id}"/>
                                    <input type="hidden" name="newRole" value="BARTENDER"/>
                                    <c:choose>
                                        <c:when test="${userStat.cocktailCount >= 5}">
                                            <button type="submit" class="btn" title="У пользователя ${userStat.cocktailCount} рецептов">Повысить до бармена</button>
                                        </c:when>
                                        <c:otherwise>
                                            <button type="submit" class="btn" style="background-color: #2196F3;" title="Назначить пользователя барменом">Назначить барменом</button>
                                        </c:otherwise>
                                    </c:choose>
                                </form>
                            </c:if>
                            <c:if test="${userStat.user.role.name() == 'BARTENDER'}">
                                <form method="post" action="${pageContext.request.contextPath}/admin" style="display: inline;">
                                    <input type="hidden" name="userId" value="${userStat.user.id}"/>
                                    <input type="hidden" name="newRole" value="USER"/>
                                    <button type="submit" class="btn" style="background-color: #f44336;">Понизить до клиента</button>
                                </form>
                            </c:if>
                        </c:if>
                        <c:if test="${userStat.user.role.name() == 'ADMIN'}">
                            <span style="color: #999; font-style: italic;">Администратор</span>
                        </c:if>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>
</body>
</html>

