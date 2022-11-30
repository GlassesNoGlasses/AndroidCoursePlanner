package UI;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidcourseplanner_final.R;

public class CustomViewHolder extends RecyclerView.ViewHolder {
    public TextView course_code, course_name;
    public Button edit_button, delete_button;

    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);
        course_code = itemView.findViewById(R.id.course_code);
        course_name = itemView.findViewById(R.id.course_name);
        edit_button = itemView.findViewById(R.id.edit_button);
        delete_button = itemView.findViewById(R.id.delete_button);
    }
}
