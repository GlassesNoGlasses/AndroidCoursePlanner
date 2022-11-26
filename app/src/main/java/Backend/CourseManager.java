package Backend;


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

        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getCourses(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        courses = new ArrayList<Course>();
    }

    public static CourseManager getInstance() {
        if (instance == null)
            instance = new CourseManager();
        return instance;
    }

    //for use in database reading, converting database courses to the hashset
    public void getCourses(DataSnapshot s) {
        courses.clear();
        for (DataSnapshot child : s.getChildren()) {
            courses.add(child.getValue(Course.class));
        }
    }

    public void addCourse(Course course) {
        courseRef.child(course.courseCode).setValue(course);
    }
    public void deleteCourse(Course course) {
        courseRef.child(course.courseCode).removeValue();
    }
    public void editCourse(Course original, Course edited) {
        deleteCourse(original);
        addCourse(edited);
    }

    //TODO the big one, the whole reason behind the app
    public static void planTimeline(Student s) {

    }

}
