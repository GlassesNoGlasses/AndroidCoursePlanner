package Backend;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class Student extends Profile {
    List<String> takenCourses;

    public Student() {
        //default constructor needed for firebase
    }
    public Student(String id) {
        this.id = id;
        takenCourses = new ArrayList<String>();
    }

    //getters and setters for firebase vv
    public List<String> getTakenCourses() {
        return takenCourses;
    }

    public void setTakenCourses(List<String> takenCourses) {
        this.takenCourses = takenCourses;
    }
    //getters and setters over ^^

    public void addTakenCourse(String courseCode) {
        if (takenCourses == null) takenCourses = new ArrayList<String>();
        takenCourses.add(courseCode);
        LoginModel.getInstance().setStudentChanges(this);
    }
    public void removeTakenCourse(String courseCode) {
        takenCourses.remove(courseCode);
    }
}
