// The Enqueue servlet should be mapped to the "/enqueue" URL.
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
import com.google.appengine.api.users.*;

public class CourseEnqueue extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String courseID = request.getParameter("courseID");
        String courseName = request.getParameter("courseName");
        String instructorID = request.getParameter("instructorID");
/*        List<String> instructorID = new ArrayList<String>();
        for(int i=0;i<instructorCollection.length;i++){
            instructorID.add(instructorCollection[i]);
        }*/
        /*UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String userId = user.getUserId();
*/
        // Add the task to the default queue.
        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(TaskOptions.Builder.withUrl("/courseworker").param("courseID", courseID).param("courseName", courseName).param("instructorID", instructorID));
        response.sendRedirect("/instructorpersonal.jsp");
    }
}
