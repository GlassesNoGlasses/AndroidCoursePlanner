package com.example.androidcourseplanner_final;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.example.androidcourseplanner_final.databinding.StudentPlanCreatorBinding;
import androidx.fragment.app.Fragment;

public class StudentPlanCreator extends Fragment {
    private StudentPlanCreatorBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = com.example.androidcourseplanner_final.databinding.StudentPlanCreatorBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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