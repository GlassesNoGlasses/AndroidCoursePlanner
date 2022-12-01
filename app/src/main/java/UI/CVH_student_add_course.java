package UI;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidcourseplanner_final.R;

public class CVH_student_add_course extends RecyclerView.ViewHolder{
    public CheckBox checkBox;
    public TextView course_code, course_name;

    public CVH_student_add_course(@NonNull View itemView) {
        super(itemView);
        checkBox = itemView.findViewById(R.id.checkBox);
        course_code = itemView.findViewById(R.id.course_code);
        course_name = itemView.findViewById(R.id.course_name);

    }
}
