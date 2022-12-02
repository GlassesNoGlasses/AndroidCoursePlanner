package Backend;

import java.util.ArrayList;
import java.util.List;

public class Course {
    String courseCode;
    String name;
    List<Session> sessions;
    List<String> prerequisites;

    public Course(){
        //required default constructor for firebase
    }

    public Course(String courseCode, String name) {
        this.courseCode = courseCode;
        this.name = name;
        this.sessions = new ArrayList<Session>();
        this.prerequisites = new ArrayList<String>();
    }

    //getters and setters for firebase vv
    public String getCourseCode() {
        return courseCode;
    }

    public String getName() {
        return name;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public List<String> getPrerequisites() {
        return prerequisites;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public void setPrerequisites(List<String> prerequisites) {
        this.prerequisites = prerequisites;
    }
    //end of getters and setters ^^

    //add a session
    public void addSession(Session session) {
        sessions.add(session);
    }

    public void addPrerequisite(String course) {
        prerequisites.add(course);
    }
}
