// The Enqueue servlet should be mapped to the "/enqueue" URL.
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

public class StudentEnqueue extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String perm = request.getParameter("sperm");
        String lastName = request.getParameter("sln");
        String firstName = request.getParameter("sfn");
        String email = request.getParameter("se");
        String courseID = request.getParameter("sc");
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String userId = user.getUserId();

        // Add the task to the default queue.
        Queue queue = QueueFactory.getDefaultQueue();
        queue.add(TaskOptions.Builder.withUrl("/studentworker").param("perm", perm).param("lastName", lastName).param("firstName", firstName).param("email", email).param("courseID", courseID).param("userId", userId));
        response.sendRedirect("/personal.jsp");
    }
}
