package UI;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidcourseplanner_final.R;

public class CVH_student_plan_creator extends RecyclerView.ViewHolder {
    public TextView course_code, course_name;

    public CVH_student_plan_creator(@NonNull View itemView) {
        super(itemView);
        course_code = itemView.findViewById(R.id.course_code);
        course_name = itemView.findViewById(R.id.course_name);
    }
}
