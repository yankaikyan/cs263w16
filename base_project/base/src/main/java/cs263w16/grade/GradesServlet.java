/*
 * Xin Liu
 * Last modified on Feb 12, 2016
 * The Grades servlet should be mapped to the "/grade" URL.
 * forward to /listgrade.jsp with the list of serach resultgrades
 * check the authentication of the current user
 * whether the current user is an instructor of the course
 * accept either courseKeyStr, or courseID, or courseName as a parameter
 */

package cs263w16.grade;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.CompositeFilter;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


//import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;

@SuppressWarnings("serial")
public class GradesServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
  	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  	MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
	UserService userService = UserServiceFactory.getUserService();

	User userName = userService.getCurrentUser();
	if (userName == null) {
		System.err.println( "User has not logged in, but try to add grade" );
		resp.sendRedirect("/welcome.jsp");
	}

        String courseKeyStr = req.getParameter("courseKeyStr");
        String courseID = req.getParameter("courseID");
        String courseName = req.getParameter("courseName");

        String studentID = req.getParameter("studentID");
        String name = req.getParameter("name");

	Key courseKey;

     try {
	if(courseKeyStr != null) {
	    try{
		courseKey = KeyFactory.stringToKey(courseKeyStr);
	    } catch(Exception e) {
		System.out.println( "The course is not found." );
		forwardGradeListWithWarning(req, resp, "Course not found, please try agian!");
		return;
	    }
	} else if(courseID != "") {
		courseKey = getCourseKey( "courseID", courseID, userName.toString() );
	} else if (courseName != "") {
		courseKey = getCourseKey("courseName", courseName, userName.toString() );
	} else {
		//forward to "/listgrade.jsp", ask user to specify a course
		String warningMessage = "Please specify a course!";
		forwardGradeListWithWarning(req, resp, warningMessage);
		return;
	}

	if (courseKey == null) {
		forwardGradeListWithWarning(req, resp, "Error when looking for the course, please try agian!");
		return;
	}	

	//check whether userName is an instructor of courseID
	// if not, return to listgrade.jsp
	if( !isUserAuthenticated(courseKey, userName.toString()) ) {
		System.err.println( "User " + userName.toString() + " is not authenticated to add grade for this course" );
		resp.sendRedirect("/welcome.jsp");
	}

	Query q = new Query("Grade").setAncestor(courseKey);
	
	if(studentID != "" && name == "") {
		Filter studentIDFilter =
  			new FilterPredicate("studentID",
                      	FilterOperator.EQUAL, studentID);
		q = q.setFilter(studentIDFilter);
	} else if(studentID == "" && name != "") {
		Filter nameFilter =
  			new FilterPredicate("name",
                      	FilterOperator.EQUAL, name);
		q = q.setFilter(nameFilter);
	} else if(studentID != "" && name != "") {
		Filter studentIDFilter =
  			new FilterPredicate("studentID",
                      	FilterOperator.EQUAL, studentID);
		Filter nameFilter =
  			new FilterPredicate("name",
                      	FilterOperator.EQUAL, name);
		Filter StudentIDNameFilter =
			CompositeFilterOperator.and(studentIDFilter, nameFilter);
		q = q.setFilter(StudentIDNameFilter);
	}

	  PreparedQuery pq = datastore.prepare(q);

	List<Grade> gradeList = new ArrayList<>();
	for (Entity ent : pq.asIterable()) {
		String gradeKeyStr = KeyFactory.keyToString( ent.getKey() );
		studentID = (String) ent.getProperty("studentID");
		name = (String) ent.getProperty("name"); 
		String grader = (String) ent.getProperty("grader");
		int score = ( (Long) ent.getProperty("score") ).intValue(); 
		Date date= (Date) ent.getProperty("date"); 
		String attribute = (String) ent.getProperty("attribute");
		Grade grade = new Grade (gradeKeyStr, studentID, name, score, grader, date, attribute);
		gradeList.add(grade);
	}

	forwardGradeList(req, resp, gradeList);
      } catch (Exception e) {
		System.out.println( "Error when looking for the course." );
//		forwardGradeListWithWarning(req, resp, "Course not found, please try agian!");		
      }
    }

    private void forwardGradeListWithWarning(HttpServletRequest req, HttpServletResponse resp, String warningMessage)
            throws ServletException, IOException {
        String nextJSP = "/listgrade.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        req.setAttribute("warningMessage", warningMessage);
        dispatcher.forward(req, resp);
    } 

    private void forwardGradeList(HttpServletRequest req, HttpServletResponse resp, List<Grade> gradeList)
            throws ServletException, IOException {
        String nextJSP = "/listgrade.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        req.setAttribute("gradeList", gradeList);
        dispatcher.forward(req, resp);
    } 

    private boolean isUserAuthenticated(Key courseKey, String userNameStr) {
  	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      try{
	Entity course = datastore.get(courseKey);
	ArrayList<String> instructors = (ArrayList<String>) course.getProperty("instructors");
	return ( instructors.contains( userNameStr ));
      } catch (Exception e) {
		System.out.println( "The course is not found." );
		return false;
      }
    }

    private Key getCourseKey( String propertyName, String propertyValue, String userNameStr) {
  	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	Filter courseFilter =
  			new FilterPredicate(propertyName,
                      	FilterOperator.EQUAL, propertyValue);	
	Filter instructorFilter =
  			new FilterPredicate("instructors",
                      	FilterOperator.EQUAL, userNameStr);
	courseFilter =
			CompositeFilterOperator.and(courseFilter, instructorFilter);
	Query cq = new Query("Course").setFilter(courseFilter);
	PreparedQuery cpq = datastore.prepare(cq);

	try {
		Entity courseEnt = cpq.asSingleEntity();
		if (courseEnt == null) {
			System.out.println( "No course is found for " + propertyName + ": " + propertyValue);
			return null;
		} else {
			return courseEnt.getKey();
		}
	} catch (Exception e) {
		System.out.println( "Too many courses are found for " + propertyName + ": " + propertyValue);
		return null;
	}
	

/*	int count = 0;	
	String courseListStr = "";
	for (Entity courseEnt : cpq.asIterable()) {
		count ++;
		courseListStr = courseListStr + courseEnt.getProperty("courseID") + " ";
	}


	if(count == 0) {
		String warningMessage = "No course found for " + propertyName + " as " + propertyValue 
			+ ", please correct your search condition.";
		forwardGradeListWithWarning(req, resp, warningMessage);
		return null;

		
	}  else if (count > 1){
		//forward to "/listgrade.jsp", ask user to specify a course
		String warningMessage = "More than one course found for " + propertyName 
			+ " as " + propertyValue + ", the courseID are: " + courseListStr
			+ ", please specify a course! The ";
		forwardGradeListWithWarning(req, resp, warningMessage);
		return null;
	} else {
		return cpq.asSingleEntity().getKey();
	}

*/
    }
}
