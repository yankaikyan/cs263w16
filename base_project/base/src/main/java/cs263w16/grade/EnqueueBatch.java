// The Enqueue servlet should be mapped to the "/enqueue_batch" URL.
package cs263w16.grade;

import java.util.Arrays;
import java.io.*;
import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.http.*;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class EnqueueBatch extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException {

        String studentID;
        String name = req.getParameter("name");
        String score;
        String grader = req.getParameter("grader");
	String attribute;
        String content = req.getParameter("content");

	String[] lines = content.split(";");
	int i = 0;
        Queue queue = QueueFactory.getDefaultQueue();

	for(String line : lines) {
		i++;
		String[] tokens = line.split(",");
		if(tokens.length < 2) {
			String errorStr = "ERROR in add_batch_grade: line " + i + " " 
				+ line + " doesn't contain studentID and score.";
			System.out.println(errorStr);
			continue;
		} else if(tokens.length == 2) {
			attribute = "";
		} else {
			attribute = tokens[2].trim();
		}
		studentID = tokens[0].trim();
		score = tokens[1].trim();
        	queue.add(TaskOptions.Builder.withUrl("/grade/worker")
			.param("studentID", studentID).param("name", name)
			.param("score", score).param("grader", grader).param("attribute", attribute) ) ;
	}
			
        response.sendRedirect("/done.html");
    }
}
