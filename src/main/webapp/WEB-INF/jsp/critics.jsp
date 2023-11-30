<!-- JSP (представление) -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>CritiCine</title>
    <style><%@include file="/WEB-INF/css/header.css"%></style>
    <style><%@include file="/WEB-INF/css/tableStyles.css"%></style>
    <style><%@include file="/WEB-INF/css/footer.css"%></style>
</head>
<body>
<jsp:include page="../includes/header.jsp" />


<h2>User Cinema Reviews</h2>

<c:if test="${not empty reviews}">
    <table border="1">
        <thead>
        <tr>
            <th>User</th>
            <th>Cinema</th>
            <th>Rating</th>
            <th>Review</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="review" items="${reviews}">
            <tr>
                <td>${review.getUser().getName()}</td>
                <td><a href="m/cinema?cinema=${review.getCinema().getId()}">${review.getCinema().getName()}</a></td>
                <td>${review.rating}</td>
                <td>${review.review}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</c:if>

<c:if test="${empty reviews}">
    <p>No reviews available.</p>
</c:if>

<jsp:include page="../includes/footer.jsp" />
</body>
</html>