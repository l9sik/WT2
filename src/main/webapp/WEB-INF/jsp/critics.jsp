<!-- JSP (представление) -->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>CritiCine</title>
    <style><%@include file="/WEB-INF/css/header.css"%></style>
    <style><%@include file="/WEB-INF/css/footer.css"%></style>
</head>
<body>
<jsp:include page="../includes/header.jsp" />

<h2>CRITICS!</h2>

<jsp:include page="../includes/footer.jsp" />
</body>
</html>