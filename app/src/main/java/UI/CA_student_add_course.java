package UI;

import android.view.LayoutInflater;
import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidcourseplanner_final.R;
import com.example.androidcourseplanner_final.StudentAddTakenCourse;


//import com.google.firebase.database.core.Context;

import java.util.List;

import Backend.Course;
import Backend.CourseManager;
import Backend.GetCoursesCallback;

public class CA_student_add_course extends RecyclerView.Adapter<CVH_student_add_course> {
    private Context context;
    private int itemCount;
    private StudentAddTakenCourse parentView;
    private List<Course> courseList;

    public CA_student_add_course(Context context, int itemCount, StudentAddTakenCourse view, List<Course> courseList){
        this.context = context;
        this.itemCount = itemCount;
        this.parentView = view;
        this.courseList = courseList;
    }
    @NonNull
    @Override
    public CVH_student_add_course onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CVH_student_add_course(LayoutInflater.from(context)
                .inflate(R.layout.single_student_add_course, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull CVH_student_add_course holder, int position) {
        holder.course_code.setText(courseList.get(holder.getAdapterPosition()).getCourseCode());
        holder.course_name.setText(courseList.get(holder.getAdapterPosition()).getName());
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }
}
