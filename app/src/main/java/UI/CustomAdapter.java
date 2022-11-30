package UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
                holder.delete_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                        builder.setMessage("Are you sure you want to delete " +
                                courses.get(holder.getAdapterPosition()).getCourseCode() + "?")
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                CourseManager.getInstance().deleteCourse(
                                        courses.get(holder.getAdapterPosition()));
                                parentView.reload();
                            }
                        }).setNegativeButton("Cancel", null);

                        AlertDialog alert = builder.create();
                        alert.show();
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
