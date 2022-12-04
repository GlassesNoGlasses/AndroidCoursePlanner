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
    private static HashMap<Integer, HashMap<Integer, List<String>>> timeline;
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
                int year = Calendar.getInstance().get(Calendar.YEAR) % 1000 + 1;
                HashMap<String, Integer> courseOfferings = new HashMap<>();
                timeline = new HashMap<>();
                timeline.put(100, new HashMap<>());
                timeline.put(200, new HashMap<>());
                timeline.put(300, new HashMap<>());

                if (s.takenCourses == null) s.setTakenCourses(new ArrayList<>());

                mapPrereqsToCode(plannedCourses, s.takenCourses, snapshot);
                Set<String> requiredCourses = timelineMap.keySet();

                courseOfferings.putAll(generateCourseOfferings(timelineMap, snapshot, requiredCourses));

                List<String> valuesToRemove = new ArrayList<>();
                for(String course: requiredCourses) {
                    if(timelineMap.get(course).isEmpty()) {
                        int offering = courseOfferings.get(course);
                        addToTimeline(offering, year, course);
                        courseOfferings.put(course, offering + year);
                        valuesToRemove.add(course);
                    }
                }

                for (String c: valuesToRemove) timelineMap.remove(c);
                generateTimelineHelper(timelineMap, courseOfferings, year);

                callback.onCallback(timeline);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Timeline: ", "Unable to fetch courses data");
            }
        });
    }

    public void addToTimeline(Integer semester, Integer year, String courseCode) {
        if(timeline.get(semester).containsKey(year)) {
            timeline.get(semester).get(year).add(courseCode);
        }
        else {
            List<String> courses = new ArrayList<>();
            courses.add(courseCode);
            timeline.get(semester).put(year, courses);
        }
    }

    private void generateTimelineHelper(HashMap<String, List<String>> timelineMap
            ,HashMap<String, Integer> courseOfferings, int year) {
        if(timelineMap.isEmpty()) return;
        List<String> valuesToRemove = new ArrayList<>();
        Set<String> requiredCourses = timelineMap.keySet();

        for(String currentCourse: requiredCourses) {
            int semester = courseOfferings.get(currentCourse);
            int semesterPlacement = semester + year;
            for(String preReq: timelineMap.get(currentCourse)) {
                if(timelineMap.containsKey(preReq)) {
                    semesterPlacement = 0;
                    break;
                }
                int preReqSem = courseOfferings.get(preReq);
                int semDiff = (preReqSem % 100) - (semesterPlacement % 100);
                if(semDiff > 0) {
                    semesterPlacement += semDiff;
                }
                if(preReqSem >= semesterPlacement && semDiff >= 0) semesterPlacement++;
            }
            if(semesterPlacement > 0) {
                addToTimeline(semester, semesterPlacement-semester, currentCourse);
                courseOfferings.put(currentCourse, semesterPlacement);;
                valuesToRemove.add(currentCourse);
            }
        }
        for(String c: valuesToRemove) timelineMap.remove(c);
        generateTimelineHelper(timelineMap, courseOfferings, year);

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
            List<String>> timelineMap, DataSnapshot courses, Set<String> requiredCourses) {
        HashMap<String, Integer> courseOfferings = new HashMap<String, Integer>();

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
                case Summer:
                     if(offerings > 200) offerings = 200;
                    break;
                case Winter:
                    if(offerings > 100) offerings = 100;
                    break;
            }
        }
        return new Integer(offerings);
    }

    }
