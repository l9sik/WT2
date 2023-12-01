<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Илья
  Date: 27.11.2023
  Time: 15:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page isELIgnored="false" %>
<fmt:setBundle basename="messages"/>

<fmt:setLocale value="en"/>
<fmt:bundle basename="messages"/>

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
        <label for="cinema-name"><fmt:message key="createCinema.name"/>:</label>
        <input type="text" name="cinema-name" id="cinema-name" placeholder="<fmt:message key="createCinema.name.placeholder"/>"
               value="${not empty requestScope.cinema_name ? requestScope.cinema_name : ''}">
        <br>
        <label for="creation-year"><fmt:message key="createCinema.year"/>:</label>
        <input type="text" name="creation-year" id="creation-year" placeholder="yyyy"
               value="${not empty requestScope.cinema_creation_year ? requestScope.cinema_creation_year : ''}">
        <label for="picture"><fmt:message key="createCinema.picture"/>:</label>
        <input type="file" name="picture" id="picture">
        <label for="cinema-type"><fmt:message key="createCinema.cinemaType"/>:</label>
        <select name="cinema-type" id="cinema-type">
            <!-- Options will be populated dynamically by the server -->
            <c:forEach var="cinemaType" items="${requestScope.cinemaTypes}">
                <option value="${cinemaType.getId()}"><fmt:message key="${cinemaType.getTypeName()}"/></option>
            </c:forEach>
        </select>
        <br>

        <button type="submit" class="createFilm">
            <c:choose>
                <c:when test="${requestScope.method == 'PUT'}"><fmt:message key="createCinema.update"/></c:when>
                <c:when test="${requestScope.method == 'DELETE'}"><fmt:message key="createCinema.delete"/></c:when>
                <c:otherwise><fmt:message key="createCinema.create"/></c:otherwise>
            </c:choose>
        </button>
    </form>

    <c:if test="${not empty errors}">
        <ul>
            <c:forEach var="error" items="${errors}">
                <li><fmt:message key="${error.getMessage()}"/></li>
            </c:forEach>
        </ul>
    </c:if>

    <jsp:include page="../../includes/footer.jsp" />
</body>
</html>
