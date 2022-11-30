package com.example.androidcourseplanner_final;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.androidcourseplanner_final.databinding.AdminCourseCreationBinding;

import Backend.Course;
import Backend.CourseManager;
import Backend.LoginModel;
import Backend.Session;

public class AdminCourseEdit extends Fragment {
    private com.example.androidcourseplanner_final.databinding.AdminCourseEditBinding binding;
    private MainActivity view;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        view = new MainActivity();
        Log.d("Course: ", view.getCourseCode());
        binding = com.example.androidcourseplanner_final.databinding.AdminCourseEditBinding
                .inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AdminCourseEdit.this)
                        .navigate(R.id.action_AdminCourseEdit_to_AdminHome);
            }
        });
        binding.editCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseCode = binding.courseCodeEntry.getText().toString();
                String courseName = binding.courseNameEntry.getText().toString();

                if (courseCode.equals("") || courseName.equals("")) {
                    Toast.makeText(getContext(),
                            "Fill in empty name or course code", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!binding.fallCheckBox.isChecked() && !binding.summerCheckBox.isChecked()
                        && !binding.winterCheckBox.isChecked()) {
                    Toast.makeText(getContext(),
                            "Fill in empty sessions", Toast.LENGTH_SHORT).show();
                    return;
                }
                //TODO check if prerequisites are empty

                Course course = new Course(courseCode, courseName);

                if (binding.fallCheckBox.isChecked())
                    course.addSession(Session.Fall);
                if (binding.summerCheckBox.isChecked())
                    course.addSession(Session.Summer);
                if (binding.winterCheckBox.isChecked())
                    course.addSession(Session.Winter);

                //TODO add prerequisites from dropdown


                CourseManager.getInstance().addCourse(course);

                NavHostFragment.findNavController(AdminCourseEdit.this)
                        .navigate(R.id.action_AdminCourseCreation_to_AdminHome);

                Toast.makeText(getContext(),
                        courseCode + " created successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
