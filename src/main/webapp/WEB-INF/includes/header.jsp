<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.poluectov.criticine.webapp.ApplicationContext" %>
<%
    String domainAddress = ApplicationContext.DOMAIN_ADDRESS;
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>CritiCine</title>
    <style><%@include file="/WEB-INF/css/header.css"%></style>
</head>
<body>
<div class="header">
    <div class="logo">Your Logo</div>
    <nav>
        <ul>
            <li><a href="<%=domainAddress%>/app/">Home</a></li>
        </ul>
    </nav>
    <c:if test="${empty sessionScope.user_role}">
        <div class="button-container">
            <div class="signin-button">
                <a href="<%=domainAddress%>/app/register" class="signin-link">Sign In</a>
            </div>
            <div class="login-button">
                <a href="<%=domainAddress%>/app/login" class="login-link">Log In</a>
            </div>
        </div>
    </c:if>
    <c:if test="${not empty sessionScope.user_role}">
        <div class="button-container">
            <div class="signin-button">
                <a href="<%=domainAddress%>/app/logout" class="login-link">Log out</a>
            </div>
        </div>
    </c:if>
</div>
</body>