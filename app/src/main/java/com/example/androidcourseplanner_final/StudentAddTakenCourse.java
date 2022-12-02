package com.example.androidcourseplanner_final;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.example.androidcourseplanner_final.databinding.StudentAddTakenCourseBinding;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Backend.Course;
import Backend.CourseManager;
import Backend.GetCoursesCallback;
import Backend.GetProfileCallback;
import Backend.LoginModel;
import Backend.LoginPresenter;
import Backend.Profile;
import Backend.Student;
import UI.CA_student_add_course;
import UI.CustomAdapter;

public class StudentAddTakenCourse extends Fragment {
    private StudentAddTakenCourseBinding binding;
    private MainActivity view;
    RecyclerView recyclerView;
    CA_student_add_course ca_student_add_course;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        view = new MainActivity();
        binding = com.example.androidcourseplanner_final.databinding.StudentAddTakenCourseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void displayItems(List<Course> courses) {
        recyclerView = binding.courseList;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        ca_student_add_course = new CA_student_add_course(getContext(), courses.size(), new StudentAddTakenCourse(), courses);
        recyclerView.setAdapter(ca_student_add_course);
    }

    public void generateMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private boolean isEligibleToTake(Course course, Student student) {
        //covers case where taken courses is null, checks to see if prereqs are needed
        if (student.getTakenCourses() == null)
            return course.getPrerequisites() == null;

        //checks if student is missing a prerequisite
        for (String pre : course.getPrerequisites())
            if (!student.getTakenCourses().contains(pre))
                return false;

        return true;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Course> eligibleCourses = new ArrayList<Course>();

        LoginModel.getInstance().getProfile(new GetProfileCallback() {
            @Override
            public void onStudent(Student student) {
                CourseManager.getInstance().getCourses(new GetCoursesCallback() {
                    @Override
                    public void onCallback(List<Course> courses) {
                        for (Course c : courses)
                            if (isEligibleToTake(c, student))
                                eligibleCourses.add(c);

                        displayItems(eligibleCourses);
                    }
                });
            }

            @Override
            public void onAdmin(Profile admin) {

            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(StudentAddTakenCourse.this)
                        .navigate(R.id.action_StudentAddTakenCourse_to_StudentHome);
            }
        });


        binding.studentAddCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateMessage("cool beans");
                NavHostFragment.findNavController(StudentAddTakenCourse.this)
                        .navigate(R.id.action_StudentAddTakenCourse_to_StudentHome);
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}