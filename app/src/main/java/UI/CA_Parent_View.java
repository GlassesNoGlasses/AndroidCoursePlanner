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
        Set<Integer> winterSessions = timeline.get(100).keySet();
        Set<Integer> summerSessions = timeline.get(200).keySet();
        Set<Integer> fallSessions = timeline.get(300).keySet();
        List<Integer> ordWinterSessions = new ArrayList<>(winterSessions);
        List<Integer> ordSummerSessions = new ArrayList<>(summerSessions);
        List<Integer> ordFallSessions = new ArrayList<>(fallSessions);

        if (position < timeline.get(100).size()) {
            holder.session.setText("Winter 20" + ordWinterSessions.get(position));
            CA_Child_View childAdapter = new CA_Child_View(context, timeline.get(100)
                    .get(ordWinterSessions.get(position)));
            holder.sessionCourses.setLayoutManager(new GridLayoutManager(context,1));
            holder.sessionCourses.setAdapter(childAdapter);
        }
        else if (position < timeline.get(100).size() + timeline.get(200).size()) {
            holder.session.setText("Summer 20" + ordSummerSessions.get(position - timeline.get(100).size() ));
            CA_Child_View childAdapter = new CA_Child_View(context, timeline.get(200)
                    .get(ordSummerSessions.get(position - timeline.get(100).size())));
            holder.sessionCourses.setLayoutManager(new GridLayoutManager(context,1));
            holder.sessionCourses.setAdapter(childAdapter);
        }
        else {
            holder.session.setText("Fall 20" + ordFallSessions.get(position - (timeline.get(100).size() + timeline.get(200).size())));
            CA_Child_View childAdapter = new CA_Child_View(context, timeline.get(300)
                    .get(ordFallSessions.get(position - (timeline.get(100).size() + timeline.get(200).size()))));
            holder.sessionCourses.setLayoutManager(new GridLayoutManager(context,1));
            holder.sessionCourses.setAdapter(childAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return timeline.get(100).size() + timeline.get(200).size() + timeline.get(300).size();
    }
}
