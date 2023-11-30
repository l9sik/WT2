<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Илья
  Date: 27.11.2023
  Time: 15:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.poluectov.criticine.webapp.model.data.CinemaType" %>
<%@ page import="com.poluectov.criticine.webapp.controller.ErrorMessage" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>CritiCine</title>
    <style><%@include file="/WEB-INF/css/header.css"%></style>
    <style><%@include file="/WEB-INF/css/footer.css"%></style>
    <style><%@include file="/WEB-INF/css/form.css"%></style>
</head>
<body>
    <jsp:include page="../../includes/header.jsp" />

    <form method="post" action="" enctype="multipart/form-data">
        <c:if test="${requestScope.method == 'PUT'}">
            <input type="hidden" name="method" id="method" value="PUT">
            <input type="hidden" name="cinema-id" id="cinema-id" value="${requestScope.cinema_id}">
        </c:if>
        <label for="cinema-name">Cinema Name:</label>
        <input type="text" name="cinema-name" id="cinema-name" placeholder="cinema_name..."
               value="${not empty requestScope.cinema_name ? requestScope.cinema_name : ''}">

        <br>
        <label for="creation-year">Creation year:</label>
        <input type="text" name="creation-year" id="creation-year" placeholder="yyyy"
               value="${not empty requestScope.cinema_creation_year ? requestScope.cinema_creation_year : ''}">
        <label for="picture">Cinema picture:</label>
        <input type="file" name="picture" id="picture">
        <label for="cinema-type">CinemaType:</label>
        <select name="cinema-type" id="cinema-type">
            <!-- Options will be populated dynamically by the server -->
            <c:forEach var="cinemaType" items="${requestScope.cinemaTypes}">
                <option>${cinemaType.getTypeName()}</option>
            </c:forEach>
        </select>
        <br>

        <button type="submit" class="createFilm">
            <c:choose>
                <c:when test="${requestScope.method == 'PUT'}">Update Film</c:when>
                <c:when test="${requestScope.method == 'DELETE'}">Delete Film</c:when>
                <c:otherwise>Create Film</c:otherwise>
            </c:choose>
        </button>
    </form>

    <c:if test="${not empty errors}">
        <ul>
            <c:forEach var="error" items="${errors}">
                <li>${error.getMessage()}</li>
            </c:forEach>
        </ul>
    </c:if>

    <jsp:include page="../../includes/footer.jsp" />
</body>
</html>
