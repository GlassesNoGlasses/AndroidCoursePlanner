package UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidcourseplanner_final.R;
import com.example.androidcourseplanner_final.StudentPlanCreator;

import java.util.HashMap;
import java.util.List;

import Backend.Course;
import Backend.CourseManager;
import Backend.GetCourseCallback;


//Class used for the child recyclerview
public class CA_Child_View extends RecyclerView.Adapter<CVH_student_plan_creator> {
    private Context context;
    private List<String> courseCodes;

    public CA_Child_View(Context context,  List<String> courses) {
        this.context = context;
        this.courseCodes = courses;
    }

    @NonNull
    @Override
    public CVH_student_plan_creator onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CVH_student_plan_creator(LayoutInflater.from(context)
                .inflate(R.layout.student_single_timeline_course, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CVH_student_plan_creator holder, int position) {
        //TODO add the course name, course code of the course taken from the generated timeline
        holder.course_code.setText(courseCodes.get(holder.getAdapterPosition()));
//        CourseManager.getInstance().getCourse(new GetCourseCallback() {
//            @Override
//            public void onCallback(Course course) {
//                holder.course_name.setText();
//            }
//        }, courseCodes.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return courseCodes.size();
    }
}
