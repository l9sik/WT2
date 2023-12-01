<%@ page import="com.poluectov.criticine.webapp.ApplicationContext" %>
<!-- JSP (представление) -->
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ page isELIgnored="false" %>
<fmt:setBundle basename="messages"/>

<fmt:setLocale value="${sessionScope.locale}"/>
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

    <c:if test="${sessionScope.user_role == 2}">
        <div>
            <a href="m/create"><button><fmt:message key="main.create_cinema"/></button></a>
            <a href="critics"><button><fmt:message key="main.critics"/></button></a>
        </div>
    </c:if>

    <c:if test="${not empty cinemas}">
        <table border="1">
            <thead>
                <tr>
                    <th><fmt:message key="main.picture"/></th>
                    <th data-filter="cinema_name"><fmt:message key="main.cinema_name"/></th>
                    <th data-filter="cinema_rating"><fmt:message key="main.cinema_rating"/></th>
                    <th data-filter="cinema_creation_year"><fmt:message key="main.cinema_creation_year"/></th>
                    <th data-filter="fk_cinema_type"><fmt:message key="main.cinema_type"/></th>
                </tr>
            </thead>
            <tbody>
            <c:forEach var="cinema" items="${cinemas}">
                <tr>
                    <td>
                        <a href="m/cinema?cinema=${cinema.getId()}"><img src="../uploads/${cinema.getPictureFile()}" alt="<fmt:message key="main.picture.placeholder"/>"></a>
                    </td>
                    <td>${cinema.getName()}</td>
                    <fmt:formatNumber value="${cinema.getRating()}" pattern="#,##0.00" var="formattedNumber" />
                    <td>${formattedNumber}</td>
                    <td>${cinema.getCreationYear()}</td>
                    <td>
                        <fmt:message key="${cinema.getCinemaType().getTypeName()}"/>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>    
    </c:if>


    <c:if test="${not empty errors}">
        <ul>
            <c:forEach items="${errors}" var="error">
                <li><fmt:message key="${error.getError()}"/></li>
            </c:forEach>
        </ul>
    </c:if>

    <jsp:include page="includes/footer.jsp" />
</body>
</html>