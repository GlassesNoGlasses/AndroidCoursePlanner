package com.example.androidcourseplanner_final;

import static androidx.databinding.DataBindingUtil.setContentView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
//        List<String> testTimeline = new ArrayList<>();
//        testTimeline.add("TIME02");
//        Timeline.getInstance().generateTimeline(testTimeline, new TimelineCallback() {
//            @Override
//            public void onCallback(HashMap<String, List<String>> callback) {
//                Log.d("Timeline StudentHome:", String.valueOf(callback));
//            }
//        });
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

    private void createDialog(Student s, List<String> courseList) {
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
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
//        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                for (int j = 0; j < selectedPrerequisites.length; j++) {
//                    selectedPrerequisites[j] = false;
//                    courseList.clear();
//                    preRequisiteText.setText("");
//                }
//            }
//        });
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

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        generateLists();

        LoginModel.getInstance().getProfile(new GetProfileCallback() {
            @Override
            public void onStudent(Student student) { binding.studentText.setText(student.getId()); }

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

                                createDialog(student, eligibleCourses);
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