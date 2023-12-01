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
        <%@include file="/WEB-INF/css/header.css"%>
        <%@include file="/WEB-INF/css/footer.css"%>
        <%@include file="/WEB-INF/css/form.css"%>
    </style>
</head>
<body>
<jsp:include page="../includes/header.jsp" />
<div class="form-container">
    <form method="post" action="register">
        <label for="name"><fmt:message key="register.username"/></label>
        <input type="text" name="name" id="name" placeholder="<fmt:message key="register.username.placeholder"/>...">
        <br>
        <label for="email"><fmt:message key="register.email"/></label>
        <input type="text" name="email" id="email" placeholder="<fmt:message key="register.email.placeholder"/>...">
        <br>
        <label for="password"><fmt:message key="register.password"/></label>
        <input type="password" name="password" id="password" placeholder="<fmt:message key="register.password.placeholder"/>...">
        <br>
        <button type="submit" class="signin-button"><fmt:message key="register.register"/></button>
    </form>
</div>
    <c:if test="${not empty errors}">
        <ul>
            <c:forEach items="${errors}" var="error">
                <li><fmt:message key="${error.getError()}"/></li>
            </c:forEach>
        </ul>
    </c:if>
<jsp:include page="../includes/footer.jsp" />
</body>
</html>