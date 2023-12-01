<!-- JSP (представление) -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page isELIgnored="false" %>
<fmt:setBundle basename="messages"/>

<fmt:setLocale value="en"/>
<fmt:bundle basename="messages"/>
<html>
<head>
    <meta charset="UTF-8">
    <title>CritiCine</title>
    <style>
        <%@include file="/WEB-INF/css/form.css"%>
    </style>
</head>
<body>
<div>
    <form method="post" action="login">
        <label for="user_name"><fmt:message key="login.username"/></label>
        <input type="text" name="user_name" id="user_name" placeholder="<fmt:message key="login.username.placeholder"/>...">
        <br>
        <label for="user_password"><fmt:message key="login.password"/></label>
        <input type="password" name="user_password" id="user_password" placeholder="<fmt:message key="login.password.placeholder"/>...">
        <br>
        <button type="submit" class="signin-button"><fmt:message key="login.signin"/></button>
    </form>
</div>
<c:if test="${not empty errors}">
    <ul>
        <c:forEach items="${errors}" var="error">
            <li><fmt:message key="${error.getError()}"/></li>
        </c:forEach>
    </ul>
</c:if>
</body>
</html>