package UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidcourseplanner_final.R;
import com.example.androidcourseplanner_final.StudentHome;

import java.util.List;

import Backend.Course;
import Backend.CourseManager;
import Backend.GetCourseCallback;
import Backend.GetProfileCallback;
import Backend.LoginModel;
import Backend.Profile;
import Backend.Student;

public class CA_student_home extends RecyclerView.Adapter<CVH_student_home> {
    private Context context;
    private StudentHome studentview;
    private List<String> courseList;
    private int itemCount;

    public CA_student_home(Context context, StudentHome studentview, List<String> courseList, int itemCount) {
        this.context = context;
        this.studentview = studentview;
        this.courseList = courseList;
        this.itemCount = itemCount;
    }

    @NonNull
    @Override
    public CVH_student_home onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CVH_student_home(LayoutInflater.from(context)
                .inflate(R.layout.single_student_add_course, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CVH_student_home holder, int position) {
        CourseManager.getInstance().getCourse(new GetCourseCallback() {
            @Override
            public void onCallback(Course course) {
                holder.course_name.setText(course.getName());
            }
        }, courseList.get(holder.getAdapterPosition()));
        holder.course_code.setText(courseList.get(holder.getAdapterPosition()));

        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setMessage("Are you sure you want to delete " +
                        courseList.get(holder.getAdapterPosition()) + "?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i){
                                LoginModel.getInstance().getProfile(new GetProfileCallback() {
                                    @Override
                                    public void onStudent(Student student) {
                                        student.removeTakenCourse(courseList.get(holder.getAdapterPosition()));
                                        studentview.reload();
                                    }

                                    @Override
                                    public void onAdmin(Profile admin) {

                                    }
                                });
                            }

                        }).setNegativeButton("Cancel", null);

                        AlertDialog alert = builder.create();
                        alert.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return itemCount;
    }
}
