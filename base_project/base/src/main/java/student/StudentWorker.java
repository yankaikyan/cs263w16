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
        String userId = request.getParameter("userId");
        // Do something with key.
        Entity student = new Entity("Student", perm);
        student.setProperty("perm", perm);
        student.setProperty("lastName", lastName);
        student.setProperty("firstName", firstName);
        student.setProperty("email", email);
        student.setProperty("courseID", courseID);
        student.setProperty("userId", userId);
        datastore.put(student);
    }
}
