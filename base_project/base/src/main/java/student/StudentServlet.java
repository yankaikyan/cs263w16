package student;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;

@SuppressWarnings("serial")
public class StudentServlet extends HttpServlet {
  public StudentServlet(){

  }
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/html");
    resp.getWriter().println("<html><body>");
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
    Entity student1 = new Entity("Student", "1");
    student1.setProperty("perm", "1");
    student1.setProperty("lastName", "Yan");
    student1.setProperty("firstName", "Kai");
    student1.setProperty("email", "1@example.com");
    student1.setProperty("courseID","1");
    datastore.put(student1);
    Student student2 = new Student("1","Yan","Kai","1@example.com","1");
    syncCache.put("1",student2);
    String perm = req.getParameter("perm");
//    String  lastName = req.getParameter("lastName");
    Key entKey = KeyFactory.createKey("Student", perm);
    Student student3 = (Student)syncCache.get(perm);
    if(student3!=null){
      resp.getWriter().println("Memcache: " + "perm: " + student3.getPerm() + " lastName: " + student3.getLastName()+ "<br>");
    }
    try{
      Entity student1_1 = datastore.get(entKey);
      resp.getWriter().println("Datastore: " + "perm: " + student1_1.getProperty("perm") + "  lastName: " + student1_1.getProperty("lastName") + "<br>");
    }catch(EntityNotFoundException e){
      resp.getWriter().println("entity not found!");
    }
//    resp.getWriter().println("courseID: " + course1_1.getProperty("courseID") + "  name: " + course1_1.getProperty("name") + "<br>");
    resp.getWriter().println("</body></html>");
  }
}
