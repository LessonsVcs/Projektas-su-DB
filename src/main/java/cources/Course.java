package cources;

import java.util.Date;

public class Course {
    private String  name;
    private String  description;
    private String  courseID;
    private Date startDate;
    private String credits;

    public Course(){

    }
    public Course(String name, String description, String courseID, Date startDate, String credits){
        this.name = name;
        this.description = description;
        this.courseID = courseID;
        this.startDate = startDate;
        this.credits = credits;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
