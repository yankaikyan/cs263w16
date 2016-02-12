<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.User"%>
<%@ page import="com.google.appengine.api.users.UserService"%>
<%@ page import="com.google.appengine.api.users.UserServiceFactory"%>
<%@ page import="com.google.appengine.api.datastore.*"%>
<%@ page import="com.google.appengine.api.datastore.Query.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory"%>
<%@ page import="com.google.appengine.api.datastore.DatastoreService"%>
<%@ page import="com.google.appengine.api.datastore.Query.Filter"%>
<%@ page import="com.google.appengine.api.datastore.Query.FilterPredicate"%>
<%@ page import="com.google.appengine.api.datastore.Query.FilterOperator"%>
<%@ page import="com.google.appengine.api.datastore.Query.CompositeFilter"%>
<%@ page import="com.google.appengine.api.datastore.Query.CompositeFilterOperator"%>
<%@ page import="com.google.appengine.api.datastore.Query"%>
<%@ page import="com.google.appengine.api.datastore.PreparedQuery"%>
<%@ page import="com.google.appengine.api.datastore.Entity"%>
<%@ page import="java.util.logging.*"%>

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
    <title>Personal Details</title>
    <h1>Personal Details</h1>
    <link type="text/css" rel="stylesheet" href="/stylesheets/main.css"/>
  </head>

  <body>
    <%
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if(user!=null){
      pageContext.setAttribute("user", user);
      String userId = user.getUserId();
			Filter propertyFilter = new FilterPredicate("userId", FilterOperator.EQUAL, userId);
			try{
				Query q = new Query("Instructor").setFilter(propertyFilter);
				List<Entity> instructors = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
				String instructorID, lastName, firstName, email;
				for(Entity instructor : instructors){
				  instructorID = (String) instructor.getProperty("instructorID");
          pageContext.setAttribute("instructorID", instructorID);
          lastName = (String) instructor.getProperty("lastName");
          pageContext.setAttribute("lastName", lastName);
          firstName = (String) instructor.getProperty("firstName");
          pageContext.setAttribute("firstName", firstName);
          email = (String) instructor.getProperty("email");
          pageContext.setAttribute("email", email);
        }
    %>
    <p> Welcome! ${fn:escapeXml(lastName)}</p>
    <form>
      instructorID<br>
      ${fn:escapeXml(instructorID)}<br>
      LastName:<br>
      ${fn:escapeXml(lastName)}<br>
      FirstName:<br>
      ${fn:escapeXml(firstName)}<br>
      Email:<br>
      ${fn:escapeXml(email)}<br>
    </form>
    <%
    }finally{}
    %>
    <%
    }else{
      out.println("You are not logged in!");
    }
    %>

    <a href="">Create a Course</a>
  </body>

</html>
