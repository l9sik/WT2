<%@ page import="com.poluectov.criticine.webapp.ApplicationContext" %>
<!-- JSP (представление) -->
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="en"/>
<fmt:bundle basename="messages"/>
<html>
<head>
    <meta charset="UTF-8">
    <title>CritiCine</title>
    <style><%@include file="/WEB-INF/css/header.css"%></style>
    <style><%@include file="/WEB-INF/css/tableStyles.css"%></style>
    <style><%@include file="/WEB-INF/css/footer.css"%></style>
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            const tableHeaders = document.querySelectorAll("table thead tr th[data-filter]");
            let filterApplied = false;

            tableHeaders.forEach(header => {
                header.addEventListener("click", function () {
                    if (!filterApplied) {
                        const filterParam = this.getAttribute("data-filter");
                        const currentUrl = window.location.href;
                        const urlSeparator = currentUrl.indexOf("?") !== -1 ? "&" : "?";
                        window.location.href = currentUrl + urlSeparator + "sortBy=" + filterParam;
                        filterApplied = true;
                    }
                });
            });
        });
    </script>

</head>
<body>
    <jsp:include page="includes/header.jsp" />

    <h2><fmt:message key="hello"/></h2>

    <c:if test="${sessionScope.role == 2}">
        <div>
            <a href="m/create"><button>Create Film</button></a>
            <a href="critics"><button>Critics</button></a>
        </div>
    </c:if>

    <c:if test="${not empty cinemas}">
        <table border="1">
            <thead>
                <tr>
                    <th>Picture</th>
                    <th data-filter="cinema_name">Name</th>
                    <th data-filter="cinema_rating">Rating</th>
                    <th data-filter="cinema_creation_year">Creation Year</th>
                    <th data-filter="fk_cinema_type">Type</th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="cinema" items="${cinemas}">
                <tr>
                    <td>
                        <a href="m/cinema?cinema=${cinema.getId()}"><img src="../uploads/${cinema.getPictureFile()}" alt="Film Picture"></a>
                    </td>
                    <td>${cinema.getName()}</td>
                    <td>${cinema.getRating()}</td>
                    <td>${cinema.getCreationYear()}</td>
                    <td>
                        ${cinema.getCinemaType().getTypeName()}
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>    
    </c:if>


    <c:if test="${not empty errors}">
        <ul>
            <c:forEach items="${errors}" var="error">
                <li>${error.error}: ${error.message}</li>
            </c:forEach>
        </ul>
    </c:if>

    <jsp:include page="includes/footer.jsp" />
</body>
</html>