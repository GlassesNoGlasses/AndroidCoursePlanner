package com.example.androidcourseplanner_final;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.androidcourseplanner_final.databinding.AdminCourseCreationBinding;


import java.util.List;

import Backend.Course;
import Backend.CourseManager;
import Backend.GetCoursesCallback;
import Backend.Session;

public class AdminCourseCreation extends Fragment {
    private AdminCourseCreationBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = com.example.androidcourseplanner_final.databinding.AdminCourseCreationBinding
                .inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AdminCourseCreation.this)
                        .navigate(R.id.action_AdminCourseCreation_to_AdminHome);
            }
        });
        binding.createCourseButton.setOnClickListener(new View.OnClickListener() {
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


                //check if the courseCode already exists
                CourseManager.getInstance().getCourses(new GetCoursesCallback() {
                    @Override
                    public void onCallback(List<Course> courses) {
                        for (Course c : courses)
                            if (c.getCourseCode().equals(courseCode)) {
                                Toast.makeText(getContext(),
                                        "Course: " + courseCode + " already exists",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }

                        //course code does not already exist
                        CourseManager.getInstance().addCourse(course);

                        NavHostFragment.findNavController(AdminCourseCreation.this)
                                .navigate(R.id.action_AdminCourseCreation_to_AdminHome);

                        Toast.makeText(getContext(),
                                courseCode + " created successfully", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
