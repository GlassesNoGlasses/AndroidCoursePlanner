package Backend;

import java.util.List;

public class Student extends Profile {
    List<String> takenCourses;
    List<String> plannedCourses;

    public Student() {
        //default constructor needed for firebase
    }
    public Student(String id) {
        this.id = id;
    }

    //getters and setters for firebase vv
    public String getId() {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public List<String> getTakenCourses() {
        return takenCourses;
    }

    public void setTakenCourses(List<String> takenCourses) {
        this.takenCourses = takenCourses;
    }

    public List<String> getPlannedCourses() {
        return plannedCourses;
    }

    public void setPlannedCourses(List<String> plannedCourses) {
        this.plannedCourses = plannedCourses;
    }
    //getters and setters over ^^

    public void addTakenCourse(Course course) {
        takenCourses.add(course.courseCode);
    }
    public void removeTakenCourse(Course course) {
        takenCourses.remove(course.courseCode);
    }

    public void addPlannedCourse(Course course) {
        plannedCourses.add(course.courseCode);
    }
    public void removePlannedCourse(Course course) {
        plannedCourses.remove(course.courseCode);
    }

    @Override
    void generateView() {

    }
}
