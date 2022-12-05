package com.example.androidcourseplanner_final;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
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
import Backend.Logout;
import Backend.Profile;
import Backend.Student;
import UI.CA_student_home;

import Backend.Timeline;
import Backend.TimelineCallback;

public class StudentHome extends Fragment {

    private StudentHomeBinding binding;
    private MainActivity view;
    RecyclerView recyclerView;
    CA_student_home ca_student_home;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        view = new MainActivity();
        binding = com.example.androidcourseplanner_final.databinding.StudentHomeBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }
    public void displayItems(List<String> courses, int itemCount){
        recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        ca_student_home = new CA_student_home(getContext(), this, courses, itemCount);
        recyclerView.setAdapter(ca_student_home);
    }

    private void createTakenDialog(Student s, List<String> courseList) {
        String[] courseArr = new String[courseList.size()];

        for (int i = 0; i < courseArr.length; i++)
            courseArr[i] = courseList.get(i);

        boolean[] isSelected = new boolean[courseArr.length];
        List<Integer> selectedItems = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Courses To Take");
        builder.setCancelable(false);

        builder.setMultiChoiceItems(courseArr, isSelected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if (b) {
                    selectedItems.add(i);
                } else {
                    selectedItems.remove(Integer.valueOf(i));
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(int l = 0; l < selectedItems.size(); l++)
                    s.addTakenCourse(courseArr[selectedItems.get(l)]);
                selectedItems.clear();
                if (s.getTakenCourses() != null) {
                    displayItems(s.getTakenCourses(), s.getTakenCourses().size());
                } else {
                    displayItems(s.getTakenCourses(), 0);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void createPlanDialog(Student s, List<String> courseList) {
        String[] courseArr = new String[courseList.size()];
        for (int i = 0; i < courseArr.length; i++)
            courseArr[i] = courseList.get(i);

        boolean[] isSelected = new boolean[courseArr.length];
        List<Integer> selectedItems = new ArrayList<>();
        List<String> plannedCourses = new ArrayList<String>();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Courses To Take");
        builder.setCancelable(false);

        builder.setMultiChoiceItems(courseArr, isSelected, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if (b) {
                    selectedItems.add(i);
                } else {
                    selectedItems.remove(Integer.valueOf(i));
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(int l = 0; l < selectedItems.size(); l++)
                    plannedCourses.add(courseArr[selectedItems.get(l)]);

                LoginModel.getInstance().getProfile(new GetProfileCallback() {
                    @Override
                    public void onStudent(Student student) {
                        Timeline.getInstance().generateTimeline(student, plannedCourses, new TimelineCallback() {
                            @Override
                            public void onCallback(HashMap<Integer, HashMap<Integer, List<String>>> callback) {
                                view.setTimeline(callback);
                                NavHostFragment.findNavController(StudentHome.this)
                                        .navigate(R.id.action_StudentHome_to_StudentPlanCreator);
                            }
                        });

                    }
                    @Override
                    public void onAdmin(Profile admin) {

                    }
                });

                selectedItems.clear();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private boolean isEligibleToTake(Course course, Student student) {
        //covers case where taken courses is null, checks to see if prereqs are needed
        if (student.getTakenCourses() == null)
            return course.getPrerequisites() == null;

        //if student has already taken the course
        if (student.getTakenCourses().contains(course.getCourseCode()))
            return false;

        //if course has no prerequisites, its good!
        if (course.getPrerequisites() == null)
            return true;

        //checks if student is missing a prerequisite
        for (String pre : course.getPrerequisites())
            if (!student.getTakenCourses().contains(pre))
                return false;

        return true;
    }

    private boolean isEligibleToPlan(Course course, Student student) {
        //covers when student has not taken any courses
        if (student.getTakenCourses() == null)
            return true;

        //true if student has not taken this course, false otherwise
        return !student.getTakenCourses().contains(course.getCourseCode());
    }

    public void reload() {

        NavHostFragment.findNavController(StudentHome.this)
                .navigate(R.id.action_StudentHome_self);

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LoginModel.getInstance().getProfile(new GetProfileCallback() {
            @Override
            public void onStudent(Student student) {
                binding.studentText.setText(student.getId());
                if (student.getTakenCourses() != null) {
                    displayItems(student.getTakenCourses(), student.getTakenCourses().size());
                } else {
                    displayItems(student.getTakenCourses(), 0);
                }
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

        binding.addTakenCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginModel.getInstance().getProfile(new GetProfileCallback() {
                    @Override
                    public void onStudent(Student student) {
                        CourseManager.getInstance().getCourses(new GetCoursesCallback() {
                            List<String> eligibleCourses = new ArrayList<String>();

                            @Override
                            public void onCallback(List<Course> courses) {
                                for (Course c : courses)
                                    if (isEligibleToTake(c, student))
                                        eligibleCourses.add(c.getCourseCode());

                                createTakenDialog(student, eligibleCourses);

                            }
                        });
                    }

                    @Override
                    public void onAdmin(Profile admin) {

                    }
                });
            }
        });

        binding.generateTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginModel.getInstance().getProfile(new GetProfileCallback() {
                    @Override
                    public void onStudent(Student student) {
                        CourseManager.getInstance().getCourses(new GetCoursesCallback() {
                            List<String> eligibleCourses = new ArrayList<String>();

                            @Override
                            public void onCallback(List<Course> courses) {
                                for (Course c : courses)
                                    if (isEligibleToPlan(c, student))
                                        eligibleCourses.add(c.getCourseCode());

                                createPlanDialog(student, eligibleCourses);
                            }
                        });
                    }

                    @Override
                    public void onAdmin(Profile admin) {

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