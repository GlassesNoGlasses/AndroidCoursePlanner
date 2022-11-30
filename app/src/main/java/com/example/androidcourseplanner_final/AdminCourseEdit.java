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

import java.util.List;

import Backend.Course;
import Backend.CourseManager;
import Backend.GetCoursesCallback;
import Backend.LoginModel;
import Backend.Session;

public class AdminCourseEdit extends Fragment {
    private com.example.androidcourseplanner_final.databinding.AdminCourseEditBinding binding;
    private MainActivity view;
    private String courseCode;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        view = new MainActivity();
        courseCode = view.getCourseCode();
        binding = com.example.androidcourseplanner_final.databinding.AdminCourseEditBinding
                .inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CourseManager.getInstance().getCourse(new GetCoursesCallback() {
            @Override
            public void onCallback(List<Course> courses) {
                Course currentCourse = courses.get(0);
                binding.courseCodeEntry.setText(currentCourse.getCourseCode());
                binding.courseNameEntry.setText(currentCourse.getName());
                setSessions(currentCourse.getSessions());
            }
        }, courseCode);

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

    private void setSessions(List<Session> sessions) {
        for (Session s: sessions) {
            if(s.equals(Session.Fall)) binding.fallCheckBox.setChecked(true);
            else if(s.equals(Session.Summer)) binding.summerCheckBox.setChecked(true);
            else if(s.equals(Session.Winter)) binding.winterCheckBox.setChecked(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
