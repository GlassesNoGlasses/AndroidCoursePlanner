package Backend;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    public void generateTimeline(List<Course> timelineCourses) {

    }

}
