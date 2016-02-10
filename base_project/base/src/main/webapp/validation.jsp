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
    String perm = request.getParameter("sperm");
    String password = request.getParameter("spassword");
    out.println("hello" + perm);
    pageContext.setAttribute("StudentPerm", perm, PageContext.SESSION_SCOPE);
    pageContext.setAttribute("StudentPassword", password, PageContext.SESSION_SCOPE);
    %>

    <a href="display.jsp">Click to see student details</a>
  </body>
</html>
