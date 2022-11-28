package com.example.androidcourseplanner_final;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.fragment.NavHostFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.androidcourseplanner_final.databinding.StudentHomeBinding;

public class StudentHome extends Fragment {

    private StudentHomeBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = StudentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(StudentHome.this)
                        .navigate(R.id.action_StudentHome_to_Login);
            }
        });

        binding.coursesTakenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(StudentHome.this)
                        .navigate(R.id.action_StudentHome_to_StudentCoursesTaken);
            }
        });

        binding.createPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(StudentHome.this)
                        .navigate(R.id.action_StudentHome_to_StudentPlanCreator);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}