package com.example.androidcourseplanner_final;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.example.androidcourseplanner_final.databinding.StudentPlanCreatorBinding;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;

import UI.CA_Parent_View;

public class StudentPlanCreator extends Fragment {
    private StudentPlanCreatorBinding binding;
    private MainActivity view = new MainActivity();

    RecyclerView recyclerView;
    HashMap<Integer, HashMap<Integer, List<String>>> timeline;
    CA_Parent_View customAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = com.example.androidcourseplanner_final.databinding.StudentPlanCreatorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void displayItems() {
        recyclerView = binding.courseList;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        customAdapter = new CA_Parent_View(getContext(), timeline);
        recyclerView.setAdapter(customAdapter);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        timeline = this.view.getTimeline();
        displayItems();

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(StudentPlanCreator.this)
                        .navigate(R.id.action_StudentPlanCreator_to_StudentHome);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}