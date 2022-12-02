package Backend;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Timeline {
    private static DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("Courses");
    private HashMap<String, List<String>> timelineMap;
    private static Timeline instance;

    private Timeline() {
        timelineMap = new HashMap<>();
    }

    public static Timeline getInstance() {
        if(instance == null) {
            instance = new Timeline();
        }
        return instance;
    }


    public void generateTimeline(Student s, List<String> plannedCourses, TimelineCallback callback) {

        courseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                timelineMap.clear();
                mapPrereqsToCode(plannedCourses, s.takenCourses, snapshot);
                callback.onCallback(timelineMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Timeline: ", "Unable to fetch courses data");
            }
        });
    }

    public void mapPrereqsToCode(List<String> plannedCourses, List<String> takenCourses, DataSnapshot courses) {
        if (plannedCourses == null || plannedCourses.isEmpty()) return;
        String currentCourse = plannedCourses.get(0);

        if(currentCourse == null || takenCourses.contains(currentCourse) || timelineMap.containsKey(currentCourse)) return;
        Course course = courses.child(currentCourse).getValue(Course.class);

        List<String> prereqCopy = new ArrayList<>();
        if (course.prerequisites != null) {
            for(String courseCode: course.prerequisites) {
                if(!(takenCourses.contains(courseCode) && prereqCopy.contains(courseCode)))
                        prereqCopy.add(courseCode);
            }
        }
        timelineMap.put(currentCourse, prereqCopy);
        plannedCourses.remove(0);
        mapPrereqsToCode(plannedCourses, takenCourses, courses);
        mapPrereqsToCode(course.prerequisites, takenCourses, courses);
        return;
    }

//    public List<String> checkDuplicates(List<String> studentTimelineInput) {
//        final List<String> listToReturn = new ArrayList<>();
//        for(String courseCode: studentTimelineInput) {
//            if(!listToReturn.contains(courseCode)) listToReturn.add(courseCode);
//        }
//        return listToReturn;
//    }


}
