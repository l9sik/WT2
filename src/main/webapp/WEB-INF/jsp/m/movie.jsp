<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.poluectov.criticine.webapp.ApplicationContext" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page isELIgnored="false" %>
<fmt:setBundle basename="messages"/>

<fmt:setLocale value="en"/>
<fmt:bundle basename="messages"/>
<html>
<head>
    <meta charset="UTF-8">
    <title>CritiCine - Cinema Details</title>
    <style>
        <%@include file="/WEB-INF/css/header.css" %>
    </style>
    <style>
        <%@include file="/WEB-INF/css/detailsStyles.css" %>
    </style>
    <style>
        <%@include file="/WEB-INF/css/comments.css" %>
    </style>
    <style>
        <%@include file="/WEB-INF/css/rating.css" %>
    </style>

</head>
<body>
<jsp:include page="../../includes/header.jsp"/>
<br>
<c:if test="${not empty cinema}">
    <div class="cinema-details">
        <h2>${cinema.getName()} <fmt:message key="cinema.details"/></h2>

        <div class="cinema-info">
            <span class="label"><fmt:message key="cinema.picture"/>:</span><br>
            <img src="../../uploads/${cinema.getPictureFile()}" alt="<fmt:message key="cinema.picture"/>">
        </div>

        <div class="cinema-info">
            <span class="label"><fmt:message key="cinema.name"/>:</span> ${cinema.getName()}<br>
            <fmt:formatNumber value="${cinema.getRating()}" pattern="#,##0.00" var="formattedNumber" />
            <span class="label"><fmt:message key="cinema.rating"/>:</span> <div class=" rating-container">${formattedNumber}</div><br>
            <span class="label"><fmt:message key="cinema.creation_year"/>:</span> ${cinema.getCreationYear()}<br>
            <span class="label"><fmt:message key="cinema.type"/>:</span> <fmt:message key="${cinema.getCinemaType().getTypeName()}"/><br>
        </div>

        <c:if test="${sessionScope.user_role == 2}">
            <div>
                <a href="<%=request.getContextPath()%>/app/m/create?method=PUT&cinema-id=${cinema.getId()}&cinema-name=${cinema.getName()}&cinema-creation-year=${cinema.getCreationYear()}">
                    <button><fmt:message key="createCinema.update"/></button>
                </a>
            </div>
        </c:if>

        <!-- Add more details as needed -->

        <a href="<%=request.getContextPath()%>/app/"><fmt:message key="cinema.back_to_cinema_list"/></a>
    </div>
</c:if>

<c:if test="${not empty errors}">
    <ul>
        <c:forEach items="${errors}" var="error">
            <li>${error.error}: ${error.message}</li>
        </c:forEach>
    </ul>
</c:if>
<c:if test="${not empty cinema}">
    <div class="cinema-details">
        <form action="" method="post" id="reviewForm">
            <input type="hidden" id="cinema-id" name="cinema-id" value="${cinema.getId()}">

            <span class="star-rating star-10">
              <input type="radio" name="rating" value="1"><i></i>
              <input type="radio" name="rating" value="2"><i></i>
              <input type="radio" name="rating" value="3"><i></i>
              <input type="radio" name="rating" value="4"><i></i>
              <input type="radio" name="rating" value="5"><i></i>
              <input type="radio" name="rating" value="6"><i></i>
              <input type="radio" name="rating" value="7"><i></i>
              <input type="radio" name="rating" value="8"><i></i>
              <input type="radio" name="rating" value="9"><i></i>
              <input type="radio" name="rating" value="10"><i></i>
            </span>


            <br>
            <br>

            <label for="review"><fmt:message key="cinema.review"/>:</label>
            <textarea class="review-text" id="review" name="review" rows="4" required></textarea>
            <br>
            <button type="submit" ><fmt:message key="cinema.leave_feedback"/></button>
        </form>
    </div>
</c:if>
<div class="cinema-details">
    <c:choose>
        <c:when test="${not empty reviews}">
            <c:forEach var="review" items="${reviews}">
                <div class="comment-container">
                    <div class="comment-header">
                        <span class="user">${review.getUser().getName()}</span>
                        <span class="rating"><fmt:message key="cinema.rating"/>: ${review.getRating()}</span>
                    </div>
                    <div class="comment-text">
                            ${review.getReview()}
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise><fmt:message key="cinema.no_reviews_yet"/></c:otherwise>
    </c:choose>
</div>

<jsp:include page="../../includes/footer.jsp"/>
</body>
</html>
