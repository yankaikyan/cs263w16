// The Enqueue servlet should be mapped to the "/enqueue" URL.
package instructor;

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

public class InstructorEnqueue extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String instructorID = request.getParameter("instructorID");
        String lastName = request.getParameter("iln");
        String firstName = request.getParameter("ifn");
        String email = request.getParameter("ie");
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String userId = user.getUserId();

        // Add the task to the default queue.
        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(TaskOptions.Builder.withUrl("/instructorworker").param("instructorID", instructorID).param("lastName", lastName).param("firstName", firstName).param("email", email).param("userId", userId));
        response.sendRedirect("/instructorpersonal.jsp");
    }
}
