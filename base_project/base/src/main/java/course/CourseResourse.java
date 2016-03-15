package course;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import javax.xml.bind.JAXBElement;

@Path("/course/{courseID}")
public class CourseResource {
  @Context
  UriInfo uriInfo;
  @Context
  Request request;
  @Context
  Response response;

  String courseID;

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    MemcacheService memcache = MemcacheServiceFactory.getMemcacheService();

  public CourseResource(UriInfo uriInfo, Request request, String courseID) {
    this.uriInfo = uriInfo;
    this.request = request;
    this.courseID = courseID;
  }

  // for the browser
  @GET
  @Produces(MediaType.TEXT_XML)
  public Course getCourseHTML() {
    //get course Entity from datastore using this.courseID
    // throw new RuntimeException("Get: course " + courseID +  " not found");
    //if not found

	//try to get course from memcache
	Course course = (Course)memcache.get(courseID);
	if(course != null) return course;

	//try to get course from datastore
	try{
		Key entKey = KeyFactory.createKey("Course", courseID);
		Entity courseEnt = datastore.get(entKey);
		course = new Course();
		course.setCourseID( (String) courseEnt.getProperty("courseID") );
		course.setCourseName((String) courseEnt.getProperty("courseName") );
		course.setInstructorIDList ( (List<String>)  courseEnt.getProperty("instructorID") );
		course.setStudentIDList ( (List<String>)  courseEnt.getProperty("StudentIDlist") );
		course.setGradeNameList ( (List<String>)  courseEnt.getProperty("GradeNameList") );
	} catch (Exception e) {
		course = null;
	}

	return course;
  }

  // for the application
  @GET
  @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
  public Course getCourse() {
	
  }

  @PUT
  @Consumes(MediaType.APPLICATION_XML)
  public Response putTaskData(String val) {

  }



} 