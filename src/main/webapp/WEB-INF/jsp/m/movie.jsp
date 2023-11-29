<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.poluectov.criticine.webapp.ApplicationContext" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>CritiCine - Cinema Details</title>
    <style><%@include file="/WEB-INF/css/header.css"%></style>
    <style><%@include file="/WEB-INF/css/detailsStyles.css"%></style>
</head>
<body>
<jsp:include page="../../includes/header.jsp" />

<c:if test="${not empty cinema}">
    <div class="cinema-details">
        <h2>${cinema.getName()} Details</h2>

        <div class="cinema-info">
            <span class="label">Picture:</span><br>
            <img src="../../uploads/${cinema.getPictureFile()}" alt="Cinema Picture">
        </div>

        <div class="cinema-info">
            <span class="label">Name:</span> ${cinema.getName()}<br>
            <span class="label">Rating:</span> ${cinema.getRating()}<br>
            <span class="label">Creation Year:</span> ${cinema.getCreationYear()}<br>
            <span class="label">Type:</span> ${cinema.getCinemaType().getTypeName()}<br>
        </div>

        <!-- Add more details as needed -->

        <a href="<%=request.getContextPath()%>/app/">Back to Cinema List</a>
    </div>
</c:if>

<c:if test="${not empty errors}">
    <ul>
        <c:forEach items="${errors}" var="error">
            <li>${error.error}: ${error.message}</li>
        </c:forEach>
    </ul>
</c:if>

<jsp:include page="../../includes/footer.jsp" />
</body>
</html>
