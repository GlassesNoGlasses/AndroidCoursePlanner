package UI;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidcourseplanner_final.R;

public class CVH_student_plan_session extends RecyclerView.ViewHolder {
    public TextView session;
    public RecyclerView sessionCourses;

    public CVH_student_plan_session(@NonNull View itemView) {
        super(itemView);
        session = itemView.findViewById(R.id.session_date_text);
        sessionCourses = itemView.findViewById(R.id.session_view);
    }
}
