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

//import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;

@SuppressWarnings("serial")
public class GradesServlet extends HttpServlet {
  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();


  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String studentID = req.getParameter("studentID");
        String name = req.getParameter("name");
	List<Grade> gradeList = new ArrayList<>();

	Query q;
	if(studentID == "" && name == "") {
		q = new Query("Grade");
	} else if(studentID != "" && name == "") {
		Filter studentIDFilter =
  			new FilterPredicate("studentID",
                      	FilterOperator.EQUAL, studentID);
		q = new Query("Grade").setFilter(studentIDFilter);
	} else if(studentID == "" && name != "") {
		Filter nameFilter =
  			new FilterPredicate("name",
                      	FilterOperator.EQUAL, name);
		q = new Query("Grade").setFilter(nameFilter);
	} else {
		Filter studentIDFilter =
  			new FilterPredicate("studentID",
                      	FilterOperator.EQUAL, studentID);
		Filter nameFilter =
  			new FilterPredicate("name",
                      	FilterOperator.EQUAL, name);
		Filter StudentIDNameFilter =
			CompositeFilterOperator.and(studentIDFilter, nameFilter);
		q = new Query("Grade").setFilter(StudentIDNameFilter);
	}

	  PreparedQuery pq = datastore.prepare(q);
	  for (Entity ent : pq.asIterable()) {

		studentID = (String) ent.getProperty("studentID");
		name = (String) ent.getProperty("name"); 
		String grader = (String) ent.getProperty("grader");
		int score = ( (Long) ent.getProperty("score") ).intValue(); 
		Date date= (Date) ent.getProperty("date"); 
		String attribute = (String) ent.getProperty("attribute");
		Grade grade = new Grade ( studentID, name, score, grader, date, attribute);
		gradeList.add(grade);
	  }

	forwardGradeList(req, resp, gradeList);
    }

    private void forwardGradeList(HttpServletRequest req, HttpServletResponse resp, List gradeList)
            throws ServletException, IOException {
        String nextJSP = "/listgrade.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        req.setAttribute("gradeList", gradeList);
        dispatcher.forward(req, resp);
    } 

/*      resp.setContentType("text/html");
      resp.getWriter().println("<html><body>");

        String studentID = req.getParameter("studentID");
        String name = req.getParameter("name");

//        String keyname = studentID + name;
      String keyname = req.getParameter("keyname");
      PrintWriter out = resp.getWriter();

      if(keyname == null) {
	//Display every element of kind Grade in the datastore
	  resp.getWriter().println("All elements of kind Grade in Datastore:");
	  Query q = new Query("Grade");
	  PreparedQuery pq = datastore.prepare(q);
	  List<String> keys = new LinkedList<String>();
	  for (Entity ent : pq.asIterable()) {
	    out.println( 
		"<br />studentID:\t" + ent.getProperty("studentID") 
		+ "<br />name:\t" + ent.getProperty("name") 
		+ "<br />grader:\t" + ent.getProperty("grader") 
		+ "<br />score:\t" + ent.getProperty("score") 
		+ "<br />date:\t" + ent.getProperty("date") 
		+ "<br />attribute:\t" + ent.getProperty("attribute") );
		keys.add( ent.getKey().getName() ); 
	  }
	  Map<String, Object> map = syncCache.getAll(keys);
	  Set<String> memKeys = map.keySet();
	  resp.getWriter().println("<br />All elements of kind Grade in Memcache:");
	  for(String key : memKeys) {
		Grade mv = (Grade) map.get(key);
		resp.getWriter().println( "<br />" + key + ": " + mv.toString() );
	  }
	
      } else {
	//check memcache for the keyname
	Grade mv = (Grade) syncCache.get(keyname);
	Key entKey = KeyFactory.createKey("Grade", keyname);
	try{	  
	  if(mv != null) {
		out.println(keyname + ": " + mv.toString() +" (Both)");
	  } else {
		Entity ent = datastore.get(entKey);
		out.println(
		"<br />studentID:\t" + ent.getProperty("studentID") 
		+ "<br />name:\t" + ent.getProperty("name") 
		+ "<br />score:\t" + ent.getProperty("score") 
		+ "<br />grader:\t" + ent.getProperty("grader") 
		+ "<br />date:\t" + ent.getProperty("date") 
		+ "<br />attribute:\t" + ent.getProperty("attribute")  + " (DataStore)");

		syncCache.put(keyname, new Grade( (String) ent.getProperty("studentID"), (String) ent.getProperty("name"), 
			(int) ent.getProperty("score") , (String) ent.getProperty("grader"), 
			(Date)ent.getProperty("date"), (String) ent.getProperty("attribute") ) );
	  }
	} catch(EntityNotFoundException e) {
	  resp.getWriter().println( "Neither" );
	}
      } 

      resp.getWriter().println("</body></html>");

*/


}
