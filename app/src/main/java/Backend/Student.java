package Backend;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class Student extends Profile {
    List<String> takenCourses;
    List<String> plannedCourses;

    public Student() {
        //default constructor needed for firebase
    }
    public Student(String id) {
        this.id = id;
        takenCourses = new ArrayList<String>();
        plannedCourses = new ArrayList<String>();
    }

    //getters and setters for firebase vv
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
