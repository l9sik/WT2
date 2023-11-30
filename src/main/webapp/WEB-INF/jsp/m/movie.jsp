<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.poluectov.criticine.webapp.ApplicationContext" %>
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
        <h2>${cinema.getName()} Details</h2>

        <div class="cinema-info">
            <span class="label">Picture:</span><br>
            <img src="../../uploads/${cinema.getPictureFile()}" alt="Cinema Picture">
        </div>

        <div class="cinema-info">
            <span class="label">Name:</span> ${cinema.getName()}<br>
            <span class="label">Rating:</span> <div class=" rating-container">${cinema.getRating()}</div><br>
            <span class="label">Creation Year:</span> ${cinema.getCreationYear()}<br>
            <span class="label">Type:</span> ${cinema.getCinemaType().getTypeName()}<br>
        </div>

        <c:if test="${sessionScope.user_role == 2}">
            <div>
                <a href="<%=request.getContextPath()%>/app/m/create?method=PUT&cinema-id=${cinema.getId()}&cinema-name=${cinema.getName()}&cinema-creation-year=${cinema.getCreationYear()}">
                    <button>Update</button>
                </a>
            </div>
        </c:if>

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

            <label for="review">Review:</label>
            <textarea class="review-text" id="review" name="review" rows="4" required></textarea>

            <button type="submit" >Leave Feedback</button>
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
                        <c:set var="decimalFormat" value="${'#.#    '}" />
                        <c:set var="formattedNumber" value="${decimalFormat.format(review.getRating())}" />
                        <span class="rating">Rating: ${formattedNumber}</span>
                    </div>
                    <div class="comment-text">
                            ${review.getReview()}
                    </div>
                </div>
            </c:forEach>
        </c:when>
        <c:otherwise>No reviews yet</c:otherwise>
    </c:choose>
</div>

<jsp:include page="../../includes/footer.jsp"/>
</body>
</html>
