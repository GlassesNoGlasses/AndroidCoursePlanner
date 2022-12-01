package Backend;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    //TODO implement using recursion
    public void generateTimeline(List<String> timelineCourses, TimelineCallback callback) {
        courseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    timelineMap.clear();
                    mapPrereqstoCode(timelineCourses, snapshot);
                    callback.onCallback(timelineMap);
                }
                else {
                    Log.d("Timeline: ", "Unable to fetch courses data");
                }
            }
        });
    }

    public void mapPrereqstoCode(List<String> timelineCourses, DataSnapshot courses) {
        if (timelineCourses == null || timelineCourses.isEmpty()) return;
        String currentCourse = timelineCourses.get(0);

        if(currentCourse == null || timelineMap.containsKey(currentCourse)) return;
        Course course = courses.child(currentCourse).getValue(Course.class);

        List<String> prereqCopy = new ArrayList<>();
        if (course.prerequisites != null) {
            prereqCopy = new ArrayList<>(course.prerequisites);
        }
        timelineMap.put(currentCourse, prereqCopy);
        timelineCourses.remove(0);
        mapPrereqstoCode(timelineCourses, courses);
        mapPrereqstoCode(course.prerequisites, courses);
        return;
    }

    public List<String> checkDuplicates(List<String> studentTimelineInput) {
        final List<String> listToReturn = new ArrayList<>();
        for(String courseCode: studentTimelineInput) {
            if(!listToReturn.contains(courseCode)) listToReturn.add(courseCode);
        }
        return listToReturn;
    }


}
