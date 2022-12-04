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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import UI.CA_Child_View;
import UI.CA_Parent_View;

public class StudentPlanCreator extends Fragment {
    private StudentPlanCreatorBinding binding;

    RecyclerView recyclerView;
    HashMap<String, List<String>> timeline;
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
        //TODO need generated timeline for adapter initialization
        customAdapter = new CA_Parent_View(getContext(), timeline);
        recyclerView.setAdapter(customAdapter);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<String> winter = new ArrayList<String>();
        winter.add("CSCA08");
        winter.add("CSCB01");
        List<String> summer = new ArrayList<String>();
        summer.add("LOL201");
        summer.add("MATB41");
        summer.add("MUZA80");
        timeline = new HashMap<String, List<String>>();
        timeline.put("Winter 2023", winter);
        timeline.put("Summer 2023", summer);
        //TODO need generated timeline for displayItems call
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