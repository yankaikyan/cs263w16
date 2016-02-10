<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
  <head>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
  </head>
  <body>
    <%
    String perm1 = (String) pageContext.getAttribute("StudentPerm", PageContext.SESSION_SCOPE);
    String password = (String) pageContext.getAttribute("StudentPassword", PageContext.SESSION_SCOPE);
    out.println("Welcome " + perm1);
    pageContext.setAttribute("perm", perm1);
    %>
    <p>Student Name:</p>
    <p>Student Perm Number:</p>
    <p>Courses:</p>
  </body>
</html>
