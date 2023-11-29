<!-- JSP (представление) -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>CritiCine</title>
    <style>
        <%@include file="/WEB-INF/css/header.css"%>
        <%@include file="/WEB-INF/css/footer.css"%>
        <%@include file="/WEB-INF/css/form.css"%>
    </style>
</head>
<body>
<jsp:include page="../includes/header.jsp" />
<div class="form-container">
    <form method="post" action="register">
        <label for="name">name</label>
        <input type="text" name="name" id="name" placeholder="user_name...">
        <br>
        <label for="email">email</label>
        <input type="text" name="email" id="email" placeholder="email...">
        <br>
        <label for="password">password</label>
        <input type="password" name="password" id="password" placeholder="your_password...">
        <br>
        <button type="submit" class="signin-button">register</button>
    </form>
</div>
    <c:if test="${not empty errors}">
        <ul>
            <c:forEach items="${errors}" var="error">
                <li>${error.error}: ${error.message}</li>
            </c:forEach>
        </ul>
    </c:if>
<jsp:include page="../includes/footer.jsp" />
</body>
</html>