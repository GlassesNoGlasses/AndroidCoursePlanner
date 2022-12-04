package Backend;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public final class CourseManager {
    private static CourseManager instance;
    DatabaseReference courseRef;
    List<Course> courses;

    private CourseManager() {
        courseRef = FirebaseDatabase.getInstance().getReference().child("Courses");
        courses = new ArrayList<Course>();
    }

    public static CourseManager getInstance() {
        if (instance == null)
            instance = new CourseManager();
        return instance;
    }

    //for use in database reading, converting database courses to the course list
    public void getCourses(GetCoursesCallback callback) {
        courses.clear();

        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    courses.add(child.getValue(Course.class));
                }
                callback.onCallback(courses);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CourseManager", "getCourses() failed");
            }
        });
    }

    public void getCourse(GetCourseCallback callback, String code) {
        if(code == null) {
            Log.d("Course Get:", "Failed to get course");
            return;
        }

        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onCallback(snapshot.child(code).getValue(Course.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CourseManager", "getCourse() failed");
            }
        });
    }

    public void addCourse(Course course) {
        courseRef.child(course.courseCode).setValue(course);
    }

    public void deleteCourse(Course course) {
        //remove course from database
        courseRef.child(course.courseCode).removeValue();

        //remove instances of the courseCode in courses
        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //for each course
                for (DataSnapshot child : snapshot.getChildren()) {
                    //for each prerequisite in the course
                    for (DataSnapshot pre : child.child("prerequisites").getChildren()) {
                        //if it contains the course code, remove the course code
                        if (pre.getValue(String.class).equals(course.courseCode))
                            courseRef.child(child.getKey()).child("prerequisites")
                                    .child(pre.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CourseManager", "deleteCourse() courses failed");
            }
        });

        DatabaseReference studentsRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child("Students");

        //remove instances of the courseCode in students
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //for each student
                for (DataSnapshot student : snapshot.getChildren()) {
                    //for each taken course
                    for (DataSnapshot taken : student.child("takenCourses").getChildren()) {
                        //if it contains the course code, remove the course code
                        if (taken.getValue(String.class).equals(course.courseCode))
                            studentsRef.child(student.getKey()).child("takenCourses")
                                    .child(taken.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CourseManager", "deleteCourse() students failed");
            }
        });
    }

    public void editCourse(Course original, Course edited) {
        //remove course from database
        courseRef.child(original.courseCode).removeValue();

        //add course to database
        addCourse(edited);

        //if course code is not changed, you're done!
        if (original.courseCode.equals(edited.courseCode)) return;

        //otherwise, update instances of the courseCode in courses
        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //for each course
                for (DataSnapshot child : snapshot.getChildren()) {
                    //for each prerequisite in the course
                    for (DataSnapshot pre : child.child("prerequisites").getChildren()) {
                        //if it contains the course code, remove the course code
                        if (pre.getValue(String.class).equals(original.courseCode))
                            courseRef.child(child.getKey()).child("prerequisites")
                                    .child(pre.getKey()).setValue(edited.courseCode);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CourseManager", "deleteCourse() courses failed");
            }
        });

        DatabaseReference studentsRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child("Students");

        //and update instances of the courseCode in students
        studentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //for each student
                for (DataSnapshot student : snapshot.getChildren()) {
                    //for each taken course
                    for (DataSnapshot taken : student.child("takenCourses").getChildren()) {
                        //if it contains the course code, remove the course code
                        if (taken.getValue(String.class).equals(original.courseCode))
                            studentsRef.child(student.getKey()).child("takenCourses")
                                    .child(taken.getKey()).setValue(edited.courseCode);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CourseManager", "deleteCourse() students failed");
            }
        });
    }

}
