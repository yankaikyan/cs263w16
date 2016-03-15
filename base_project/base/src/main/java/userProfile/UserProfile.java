/*
 * Xin Liu
 * created on Mar 1, 2016
 * record user profile
 * stored both in memcache and datastore to make the authentication easier
 */

package cs263w16.userProfile;

import java.util.*;
import java.io.*;

@XmlRootElement
public class UserProfile implements Serializable {


  private String userId; 
  private String lastName;
  private String firstName;
  private String instructorID;
  private String studentID;
  private List<String> enrolledInCourseList;
  private List<String> instructCourseList;

  public UserProfile(String userId) {
	this.userId = userId;
	enrolledInCourseList = new ArrayList<String>();
	instructCourseList = new ArrayList<String>();
  }

  public String getName (String lastName, String firstName) {
	return this.firstName + " " + this.lastName;
  }

} 
