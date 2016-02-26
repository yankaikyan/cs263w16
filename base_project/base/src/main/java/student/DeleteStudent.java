package student;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.users.*;
import com.google.appengine.api.datastore.Query.*;
import java.util.*;
import java.util.logging.*;
import java.io.*;
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

public class DeleteStudent extends HttpServlet {
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String perm = request.getParameter("perm");
        String courseID = request.getParameter("courseID");
        ArrayList<String> newCourseID = new ArrayList<String>();
        Filter propertyFilter = new FilterPredicate("perm", FilterOperator.EQUAL, perm);
        try{
            Query q = new Query("Student").setFilter(propertyFilter);
            List<Entity> students = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
            ArrayList<String> courseIDs = null;
            for(Entity student : students){
                courseIDs = (ArrayList<String>) student.getProperty("courseID");
            }
            if(courseIDs.size()!=0){
                for(String courseid : courseIDs){
                    if(!courseid.equals(courseID))
                        newCourseID.add(courseid);
                }
            }
            if(newCourseID.size()!=0){
                for(Entity student : students){
                    student.setProperty("courseID", newCourseID);
                    datastore.put(student);
                    response.sendRedirect("/coursedetail.jsp?courseID="+courseID);
                }
            }
            else{
                String warningMessage = "This student only has this course now, you can't delete him!";
                forwardGradeListWithWarning(request, response, warningMessage, courseID);
            }
        }finally{}
    }

    private void forwardGradeListWithWarning (HttpServletRequest req, HttpServletResponse resp, String warningMessage, String courseID)
            throws ServletException, IOException {
        String nextJSP = "/coursedetail.jsp?courseID=" + courseID;
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        req.setAttribute("warningMessage", warningMessage);
        dispatcher.forward(req, resp);
    }
}