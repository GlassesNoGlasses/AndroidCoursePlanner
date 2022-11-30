package com.example.androidcourseplanner_final;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidcourseplanner_final.databinding.AdminHomeBinding;

import java.util.List;

import Backend.Course;
import Backend.CourseManager;
import Backend.GetCoursesCallback;
import Backend.Logout;
import UI.CustomAdapter;

public class AdminHome extends Fragment {
    private AdminHomeBinding binding;
    RecyclerView recyclerView;
    CustomAdapter customAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = com.example.androidcourseplanner_final.databinding.AdminHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void displayItems(int itemCount) {
        recyclerView = binding.courseListView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        customAdapter = new CustomAdapter(getContext(), itemCount);
        recyclerView.setAdapter(customAdapter);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CourseManager.getInstance().getCourses(new GetCoursesCallback() {
            @Override
            public void onCallback(List<Course> courses) {
                displayItems(courses.size());
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout.signOut();
                NavHostFragment.findNavController(AdminHome.this)
                        .navigate(R.id.action_Signup_to_Login);
            }
        });
        binding.createNewCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AdminHome.this)
                        .navigate(R.id.action_AdminHome_to_AdminCourseCreation);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
