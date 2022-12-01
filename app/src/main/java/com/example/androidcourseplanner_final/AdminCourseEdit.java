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
import Backend.GetCourseCallback;
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

        CourseManager.getInstance().getCourse(new GetCourseCallback() {
            @Override
            public void onCallback(Course course) {
                binding.courseCodeEntry.setText(course.getCourseCode());
                binding.courseNameEntry.setText(course.getName());
                setSessions(course.getSessions());
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
                String newCourseCode = binding.courseCodeEntry.getText().toString();
                String newCourseName = binding.courseNameEntry.getText().toString();

                if (newCourseCode.equals("") || newCourseName.equals("")) {
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

                Course newCourse = new Course(newCourseCode, newCourseName);

                if (binding.fallCheckBox.isChecked())
                    newCourse.addSession(Session.Fall);
                if (binding.summerCheckBox.isChecked())
                    newCourse.addSession(Session.Summer);
                if (binding.winterCheckBox.isChecked())
                    newCourse.addSession(Session.Winter);

                //TODO add prerequisites from dropdown

                //check if they are changing course code to one that exists elsewhere
                CourseManager cm = CourseManager.getInstance();

                cm.getCourse(new GetCourseCallback() {
                    @Override
                    public void onCallback(Course course) {
                        Log.e("Edit", newCourseCode + " : " + course.getCourseCode());
                        //if course code does not change, you're done!
                        if (newCourseCode.equals(course.getCourseCode())) {
                            cm.editCourse(course, newCourse);

                            NavHostFragment.findNavController(AdminCourseEdit.this)
                                    .navigate(R.id.action_AdminCourseEdit_to_AdminHome);

                            Toast.makeText(getContext(),
                                    newCourseCode + " edited successfully", Toast.LENGTH_SHORT).show();

                            return;
                        }

                        //otherwise, check the database
                        cm.getCourses(new GetCoursesCallback() {
                            @Override
                            public void onCallback(List<Course> courses) {
                                for (Course c : courses)
                                    if (c.getCourseCode().equals(newCourseCode)) {
                                        Toast.makeText(getContext(),
                                                "Course: " + newCourseCode + " already exists",
                                                Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                //course code does not already exist
                                cm.editCourse(course, newCourse);

                                NavHostFragment.findNavController(AdminCourseEdit.this)
                                        .navigate(R.id.action_AdminCourseEdit_to_AdminHome);

                                Toast.makeText(getContext(),
                                        newCourseCode + " edited successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, courseCode);
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