<!-- JSP (представление) -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>CritiCine</title>
    <style>
        <%@include file="/WEB-INF/css/header.css"%>
    </style>
</head>
<body>
<div>
    <form method="post" action="login">
        <label for="user_name">name</label>
        <input type="text" name="user_name" id="user_name" placeholder="user_name...">
        <br>
        <label for="user_password_hash">password</label>
        <input type="password" name="user_password_hash" id="user_password_hash" placeholder="your_password...">
        <br>
        <button type="submit" class="signin-button">sign in</button>
    </form>
</div>
<c:if test="${not empty errors}">
    <ul>
        <c:forEach items="${errors}" var="error">
            <li>${error.error}: ${error.message}</li>
        </c:forEach>
    </ul>
</c:if>
</body>
</html>