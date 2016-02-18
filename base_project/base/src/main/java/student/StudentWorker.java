// The Worker servlet should be mapped to the "/worker" URL.
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

public class StudentWorker extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        MemcacheService sync = MemcacheServiceFactory.getMemcacheService();
        String perm = request.getParameter("perm");
        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");
        String email = request.getParameter("email");
        String courseID = request.getParameter("courseID");
        Filter propertyFilter = new FilterPredicate("email", FilterOperator.EQUAL, email);
        Query q = new Query("Student").setFilter(propertyFilter);
        List<Entity> students = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
        if(students.size()!=0){
            for(Entity student : students){
                ArrayList<String> courseIDs = (ArrayList<String>) student.getProperty("courseID");
                ArrayList<String> courseids = new ArrayList<String>();
                if(courseIDs.size()!=0){
                    for(String courseid : courseIDs){
                        courseids.add(courseid);
                    }
                }
                courseids.add(courseID);
                student.setProperty("courseID", courseids);
                datastore.put(student);
            }
        }
        else{
    //        String userId = request.getParameter("userId");
            // Do something with key.
            Entity student = new Entity("Student", perm);
            student.setProperty("perm", perm);
            student.setProperty("lastName", lastName);
            student.setProperty("firstName", firstName);
            student.setProperty("email", email);
            ArrayList<String> courseIDs = new ArrayList<String>();
            courseIDs.add(courseID);
            student.setProperty("courseID", courseIDs);
    //        student.setProperty("userId", userId);
            datastore.put(student);
        }
    }
}
