// The Worker servlet should be mapped to the "/worker" URL.
package course;

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

public class CourseWorker extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        MemcacheService sync = MemcacheServiceFactory.getMemcacheService();
        String courseID = request.getParameter("courseID");
        String courseName = request.getParameter("courseName");
        String instructors = request.getParameter("instructorID");
        // Do something with key.
        String[] instructorCollection = instructors.split(",");
        List<String> instructorID = new ArrayList<String>();
        for(int i=0;i<instructorCollection.length;i++){
            instructorID.add(instructorCollection[i]);
        }
        Entity course = new Entity("Course", courseID);
        course.setProperty("courseID", courseID);
        course.setProperty("courseName", courseName);
        course.setProperty("instructorID", instructorID);
        datastore.put(course);
    }
}
