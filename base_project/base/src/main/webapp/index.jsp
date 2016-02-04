<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
  <title> Student Login Page </title>
</head>

<body>
  <form action="validation.jsp">
    Enter Student Perm: <input type="text" name="sperm"><br>
    Enter Password: <input type="text" name="spassword"><br>
    <input type="submit" value="Login">
  </form>
</body>
</html>
