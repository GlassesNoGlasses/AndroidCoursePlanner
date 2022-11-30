package UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidcourseplanner_final.R;

import java.util.List;

import Backend.Course;
import Backend.CourseManager;
import Backend.GetCoursesCallback;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private Context context;

    public CustomAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.single_course, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        CourseManager.getInstance().getCourses(new GetCoursesCallback() {
            @Override
            public void onCallback(List<Course> courses) {
                holder.course_code.setText(courses.get(holder.getAdapterPosition()).getCourseCode());
                holder.course_name.setText(courses.get(holder.getAdapterPosition()).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
        //TODO Find a way to return the value of the size of the list, perhaps through navigation arguments
    }
}
