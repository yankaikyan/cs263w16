/*
 * Xin Liu
 * created on Mar 1, 2016
 * record user profile
 * stored both in memcache and datastore to make the authentication easier
 */

package cs263w16.grade;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;
import java.io.*;

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

  public String setName (String lastName, String firstName) {
	return gradeKeyStr;
  }

  public void setGradeKeyStr(String gradeKeyStr) {
	this.gradeKeyStr = gradeKeyStr;
  }

  public String getStudentID() {
	return studentID;
  }

  public void setStudentID(String studentID) {
	this.studentID = studentID;
  }

  public String getName() {
	return name;
  }

  public void setName(String name) {
	this.name = name;
  }

  public int getScore() {
	return score;
  }

  public void setScore(int attribute) {
	this.score = score;
  }

  public String getGrader() {
	return grader;
  }

  public void setGrader(String grader) {
	this.grader = grader;
  }

  public Date getDate() {
	return date;
  }

  public void setDate(Date date) {
	this.date = date;
  }

  public String getAttribute() {
	return attribute;
  }

  public void setAttribute(String attribute) {
	this.attribute = attribute;
  }

  public boolean getHasNewComment() {
	return hasNewComment;
  }

  public void setHasNewComment(boolean hasNewComment) {
	this.hasNewComment = hasNewComment;
  }



  public String toString() {
	StringBuilder sb = new StringBuilder();
	sb.append("\tstudentID:\t").append(studentID);
	sb.append("\n\tname:\t").append(name);
	sb.append("\n\tscore:\t").append(score);
	sb.append("\n\tgrader:\t").append(grader);
	sb.append("\n\tdate:\t").append(grader);
	sb.append("\n\tattribute:\t").append(attribute);

	return sb.toString();
  }
} 
