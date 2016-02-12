// The Worker servlet should be mapped to the "/grade/worker" URL.
package cs263w16.grade;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;


public class CommentWorker extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse response)
            throws ServletException, IOException {

	DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();

        String gradeKeyname = req.getParameter("gradeKeyname");
        String userID = req.getParameter("userID");
        String name = req.getParameter("name");
        String content = req.getParameter("content");
	Date date = new Date();
	
        String keyname = gradeKeyname + name;

	Comment gd = new Comment(gradeKeyname, userID, name, content, date);
	Key gradeKey = KeyFactory.createKey("Grade", keyname);

	//create an Entity of kind TaskData and put it into Datastore.
	Entity tne = new Entity("Comment", keyname, gradeKey);
	tne.setProperty("gradeKeyname", gradeKeyname);
	tne.setProperty("userID", userID);
	tne.setProperty("name", name);
	tne.setProperty("content", content);
	tne.setProperty("date", date);


	datastore.put(tne);
	syncCache.put(keyname, gd );
	System.err.println( "Stored " + gradeKeyname + " / " + name + " in Datastore and Memcache" );
    }
}
