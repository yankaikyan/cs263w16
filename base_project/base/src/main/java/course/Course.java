package course;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;

@XmlRootElement
public class Course implements Serializable {
  private String courseID;
  private String courseName;
  private List<String> instructorIDList;
  private List<String> studentIDList;
  private List<String> gradeNameList;

  public Course(){
  }

  public Course(String courseID, String courseName){
    this.courseID = courseID;
    this.courseName = courseName;
  }
  public String getCourseID(){
    return courseID;
  }
  public void setCourseID(String courseID){
    this.courseID = courseID;
  }
  public String getCourseName(){
    return courseName;
  }
  public void setCourseName(String courseName){
    this.courseName = courseName;
  }
  public List<String> getIntructorIDList(){
    return instructorID;
  }
  public void setInstructorIDList (List<String> instructorIDList){
    this.instructorIDList = instructorIDList;
  }

  public List<String> getStudentIDList(){
    return instructorID;
  }

  public void setStudentIDList(List<String> instructors){
    this.instructorID = new ArrayList<String>(instructorID);
  }

  public List<String> getGradeNameList(){
    return instructorID;
  }

  public void setGradeNameList(ArrayList<String> instructors){
    this.instructorID = new ArrayList<String>(instructorID);
  }

}
