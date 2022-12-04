package UI;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidcourseplanner_final.R;

public class CVH_student_home extends RecyclerView.ViewHolder {
    public TextView course_code, course_name;
    public Button delete_button;

    public CVH_student_home(@NonNull View itemView) {
        super(itemView);
        course_code = itemView.findViewById(R.id.course_code);
        course_name = itemView.findViewById(R.id.course_name);
        delete_button = itemView.findViewById(R.id.delete_button);
    }
}
