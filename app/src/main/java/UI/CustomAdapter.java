package UI;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidcourseplanner_final.AdminHome;
import com.example.androidcourseplanner_final.R;

import java.util.List;

import Backend.Course;
import Backend.CourseManager;
import Backend.GetCoursesCallback;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    private Context context;
    private int itemCount;
    private AdminHome parentView;

    public CustomAdapter(Context context, int itemCount, AdminHome view) {
        this.context = context;
        this.itemCount = itemCount;
        this.parentView = view;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.single_course, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        CourseManager.getInstance().getCourses(new GetCoursesCallback() {
            @Override
            public void onCallback(List<Course> courses) {
                holder.course_code.setText(courses.get(holder.getAdapterPosition()).getCourseCode());
                holder.course_name.setText(courses.get(holder.getAdapterPosition()).getName());

                holder.edit_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        parentView.toEditCourses(holder.course_code.getText().toString());
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }
}
