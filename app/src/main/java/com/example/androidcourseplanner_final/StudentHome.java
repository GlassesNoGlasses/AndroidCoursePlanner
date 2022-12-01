package com.example.androidcourseplanner_final;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.fragment.NavHostFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidcourseplanner_final.databinding.StudentHomeBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Backend.Course;
import Backend.CourseManager;
import Backend.GetCoursesCallback;
import Backend.GetProfileCallback;
import Backend.LoginModel;
import Backend.LoginPresenter;
import Backend.Logout;
import Backend.Profile;
import Backend.Student;
import UI.CA_student_add_course;
import UI.CVH_student_add_course;
import UI.CustomAdapter;

import Backend.Timeline;
import Backend.TimelineCallback;

public class StudentHome extends Fragment {

    private StudentHomeBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        //Testing Timeline
        List<String> testTimeline = new ArrayList<>();
        testTimeline.add("TIME02");
        Timeline.getInstance().generateTimeline(testTimeline, new TimelineCallback() {
            @Override
            public void onCallback(HashMap<String, List<String>> callback) {
                Log.d("Timeline StudentHome:", String.valueOf(callback));
            }
        });
        //End of Test
        binding = StudentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void generateLists(){
        LoginModel.getInstance().getProfile(new GetProfileCallback() {
            @Override
            public void onStudent(Student student) {

            }

            @Override
            public void onAdmin(Profile admin) {

            }
        });
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        generateLists();

        LoginModel.getInstance().getProfile(new GetProfileCallback() {
            @Override
            public void onStudent(Student student) {
                binding.studentText.setText(student.getId());
            }

            @Override
            public void onAdmin(Profile admin) {

            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout.signOut();
                NavHostFragment.findNavController(StudentHome.this)
                        .navigate(R.id.action_StudentHome_to_Login);
            }
        });

        binding.addTakenCourseButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(StudentHome.this)
                        .navigate((R.id.action_StudentHome_to_StudentAddTakenCourse));
            }
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}