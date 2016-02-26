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
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class InstructorUpload extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        String userId = user.getUserId();
        Map<String, List<BlobKey>> blobs = blobstore.getUploads(request);
        if(blobs==null||blobs.size()==0){
            response.sendRedirect("/welcome.jsp");
        }        
        else{
            List<BlobKey> blobKey = blobs.get("myFile");
            if(blobKey==null){
//                response.sendRedirect("/choose.jsp");
            }
            else{
                Queue queue =  QueueFactory.getDefaultQueue();
                for(BlobKey k : blobKey){
                    String key = k.getKeyString();
 //                   queue.add(TaskOptions.Builder.withUrl("/instructor/addphoto").param("userId", userId).param("blobKey", k.getKeyString()));
                    Filter propertyFilter = new FilterPredicate("userId", FilterOperator.EQUAL, userId);
                    Query q = new Query("Instructor").setFilter(propertyFilter);
                    List<Entity> instructors = datastore.prepare(q).asList(FetchOptions.Builder.withDefaults());
                    if(instructors!=null){
                        System.out.println("instructors is not null");
       //                 response.sendRedirect("/welcome.jsp");
                    }
                    else{
                     //   response.sendRedirect("/welcome.jsp");   
                    }
                    for(Entity instructor : instructors){
                        if(instructor.getProperty("email").equals("test@example.com"))
                            response.sendRedirect("/welcome.jsp");
                            instructor.setProperty("blobKey", key);
                            datastore.put(instructor);
                            response.sendRedirect("/choose.jsp");
    //                t.commit();
                    }
                }
             //   Thread.sleep(100);
            }
 //           response.sendRedirect("/instructorpersonal.jsp");
        }
    }
}
