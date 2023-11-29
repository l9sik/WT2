<%@ page import="com.poluectov.criticine.webapp.ApplicationContext" %>
<%
    String domainAddress = ApplicationContext.DOMAIN_ADDRESS;
%>
<div class="header">
    <div class="logo">Your Logo</div>
    <nav>
        <ul>
            <li><a href="<%=domainAddress%>/app/">Home</a></li>
            <li><a href="#">About</a></li>
            <li><a href="<%=domainAddress%>/app/m/create">Create cinema</a></li>
            <li><a href="#">Contact</a></li>
        </ul>
    </nav>
    <div class="button-container">
        <div class="signin-button">
            <a href="register" class="signin-link">Sign In</a>
        </div>
        <div class="login-button">
            <a href="login" class="login-link">Log In</a>
        </div>
    </div>
</div>