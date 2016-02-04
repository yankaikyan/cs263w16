<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="java.util.List" %>
<%@ page import="cs263w16.grade.Grade" %>

<html>
    <head>
      <link rel="stylesheet" href="css/bootstrap.min.css"/>         
       <script src="js/bootstrap.min.js"></script>       
    </head>


  <body>
        <div class="container">
            <h2>Grade List</h2>

    <p>Enter the information for the grade you are looking for:</p>
    <!--Search Form -->
    <form action="/grade" method="get"  id="seachGradeForm" role="form">
	<div class="form-group col-xs-5">
	studentID:
	<input type="text" name="studentID"><br>
	grade name:
	<input type="text" name="name"><br>
	</div>
      <button type="submit" class="btn btn-info">
               <span class="glyphicon glyphicon-search"></span> Search
       </button>
       <br></br>
    </form>

            <!--grades List-->
            <form action="/grade" method="post" id="GradeForm" role="form" > 
               
                <c:choose>
                    <c:when test="${not empty gradeList}">
                        <table  class="table table-striped">
                            <thead>
                                <tr>
                                    <td>studentID</td>
                                    <td>grade name</td>
                                    <td>score</td>
                                    <td>grader</td>
                                    <td>date</td>
                                    <td>attribute</td>                                 
                                </tr>
                            </thead>
			    <% 
			    List<Grade> gradeList = (List<Grade>) request.getAttribute("gradeList");
			    for(Grade grade : gradeList) {
				pageContext.setAttribute("studentID", grade.getStudentID() );
				pageContext.setAttribute("name", grade.getName() );
				pageContext.setAttribute("score", grade.getScore() );
				pageContext.setAttribute("grader", grade.getGrader() );
				pageContext.setAttribute("date", grade.getDate() );
				pageContext.setAttribute("attribute", grade.getAttribute() );
				%>
                                <tr>
                                    <td>${fn:escapeXml(studentID)}</td>
                                    <td>${fn:escapeXml(name)}</td>
                                    <td>${fn:escapeXml(score)}</td>
                                    <td>${fn:escapeXml(grader)}</td>
                                    <td>${fn:escapeXml(date)}</td>
                                    <td>${fn:escapeXml(attribute)}</td>                                     
                                </tr>
			    <%
			    }
			    %>             
                        </table>  
                    </c:when>                    
                    <c:otherwise>
                        <br>           
                        <div class="alert alert-info">
                            No grade found matching your search criteria
                        </div>
                    </c:otherwise>
                </c:choose>                        
            </form>
    </div>
  </body>
</html>
