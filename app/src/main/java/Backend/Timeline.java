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
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Timeline {
    private static DatabaseReference courseRef = FirebaseDatabase.getInstance().getReference("Courses");
    private HashMap<String, List<String>> timelineMap;
    private HashMap<Integer, List<String>> timeline;
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
                int year = Calendar.getInstance().get(Calendar.YEAR) % 1000;
                HashMap<String, Integer> courseOfferings = new HashMap<>();
                mapPrereqsToCode(plannedCourses, s.takenCourses, snapshot);
                courseOfferings.putAll(generateCourseOfferings(timelineMap, snapshot));
                Log.d("Course to sessions: ", String.valueOf(courseOfferings));
                callback.onCallback(timelineMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Timeline: ", "Unable to fetch courses data");
            }
        });
    }

    private void generateTimelineHelper(HashMap<String, List<String>> timelineMap
            ,HashMap<String, Integer> courseOfferings, int year) {
        int len = timelineMap.size();
        if(timelineMap.isEmpty() || len == 0) return;
        Set<String> requiredCourses = timelineMap.keySet();

        for(String code: requiredCourses) {
            if (timelineMap.get(code).isEmpty())
            for(String prereq: )
        }
    }

    //Creates HashMap mapping (String) Coursecodes: (List<String>) prereqs
    public void mapPrereqsToCode(List<String> plannedCourses, List<String> takenCourses, DataSnapshot courses) {
        if (plannedCourses == null || plannedCourses.isEmpty()) return;
        String currentCourse = plannedCourses.get(0);
//        Log.d("Inside Map Course: ", currentCourse);

        if(currentCourse == null || takenCourses.contains(currentCourse)
                || timelineMap.containsKey(currentCourse)) {
            plannedCourses.remove(0);
            mapPrereqsToCode(plannedCourses, takenCourses, courses);
        }
        else {
            Course course = courses.child(currentCourse).getValue(Course.class);
//        Log.d("Inside Map Course: ", currentCourse);
            List<String> prereqCopy = new ArrayList<>();
            if (course.prerequisites != null) {
                for(String courseCode: course.prerequisites) {
                Log.d("Inside Map Course: ", courseCode);
                    if(!(takenCourses.contains(courseCode) || prereqCopy.contains(courseCode))) {
                        prereqCopy.add(courseCode);
                    }
                }
            }

//            Log.d("preReqCopy: ", String.valueOf(prereqCopy));
            timelineMap.put(currentCourse, prereqCopy);
            plannedCourses.remove(0);
            mapPrereqsToCode(plannedCourses, takenCourses, courses);
            mapPrereqsToCode(course.prerequisites, takenCourses, courses);
        }
        return;
    }

    // maps courses to their respective sessions (prioritizing closest semester)
    private HashMap<String, Integer> generateCourseOfferings(HashMap<String,
            List<String>> timelineMap, DataSnapshot courses) {
        HashMap<String, Integer> courseOfferings = new HashMap<String, Integer>();
        Set<String> requiredCourses = timelineMap.keySet();

        for (String course : requiredCourses) {
            Course c = courses.child(course).getValue(Course.class);
            courseOfferings.put(course, getCourseOfferings(c.sessions));
        }
        return courseOfferings;
    }

    private	Integer getCourseOfferings(List<Session> courseSessions) {
        int offerings = 300;

        for(Session s: courseSessions) {
            switch(s) {
                case Fall:
                    offerings = 100;
                    break;
                case Winter:
                    if(offerings > 200) offerings = 200;
                    break;
            }
        }
        return new Integer(offerings);
    }

    }
