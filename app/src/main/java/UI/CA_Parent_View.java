package UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidcourseplanner_final.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CA_Parent_View extends RecyclerView.Adapter<CVH_student_plan_session> {
    private Context context;
    private HashMap<Integer, HashMap<Integer, List<String>>> timeline;

    public CA_Parent_View(Context context, HashMap<Integer, HashMap<Integer, List<String>>> timeline) {
        this.context = context;
        this.timeline = timeline;
    }

    @NonNull
    @Override
    public CVH_student_plan_session onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CVH_student_plan_session(LayoutInflater.from(context)
                .inflate(R.layout.student_single_timeline_session, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CVH_student_plan_session holder, int position) {
        Set<String> sessions = timeline.keySet();
        List<String> orderedSessions = new ArrayList<>(sessions);
        holder.session.setText(orderedSessions.get(position));
        CA_Child_View childAdapter = new CA_Child_View(context, timeline.get(orderedSessions.get(position)));
        holder.sessionCourses.setLayoutManager(new GridLayoutManager(context,1));
        holder.sessionCourses.setAdapter(childAdapter);
    }

    @Override
    public int getItemCount() {
        return timeline.keySet().size();
    }
}
