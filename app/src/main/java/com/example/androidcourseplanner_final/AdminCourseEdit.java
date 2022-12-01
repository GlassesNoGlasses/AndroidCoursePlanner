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

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;
import android.app.Dialog;
import java.util.ArrayList;

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
    private Course newCourse;

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
                newCourse = course;
            }
        }, courseCode);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AdminCourseEdit.this)
                        .navigate(R.id.action_AdminCourseEdit_to_AdminHome);
            }
        });
        binding.prerequisiteEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                ArrayList<Integer> courseList = new ArrayList<>();

                CourseManager.getInstance().getCourses(new GetCoursesCallback() {
                    TextView preRequisiteText = view.findViewById(R.id.prerequisite_entry);
                    @Override
                    public void onCallback(List<Course> courses) {

                        String[] preReqArray = new String[courses.size()];

                        for(int i = 0; i < courses.size(); i++){
                            preReqArray[i] = courses.get(i).getCourseCode();
                            if (newCourse.getPrerequisites().contains(preReqArray[i])){
                                courseList.add(i);
                            }
                        }

                        boolean[] selectedPrerequisites = new boolean[courses.size()];

                        builder.setTitle("Select PreRequisites");

                        builder.setCancelable(false);

                        builder.setMultiChoiceItems(preReqArray, selectedPrerequisites, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                if (b) {
                                    courseList.add(i);
                                } else {
                                    courseList.remove(Integer.valueOf(i));
                                }
                            }
                        });

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                newCourse.getPrerequisites().clear();
                                StringBuilder stringBuilder = new StringBuilder();
                                for(int j = 0; j < courseList.size(); j++) {
                                    stringBuilder.append(preReqArray[courseList.get(j)]);
                                    if(j != courseList.size() - 1) {
                                        stringBuilder.append(", ");
                                    }
                                }
                                for(int l = 0; l < courseList.size(); l++) {
                                    newCourse.addPrerequisite(preReqArray[courseList.get(l)]);
                                }
                                preRequisiteText.setText(stringBuilder.toString());
                                courseList.clear();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                for (int j = 0; j < selectedPrerequisites.length; j++) {
                                    selectedPrerequisites[j] = false;
                                    courseList.clear();
                                    preRequisiteText.setText("");
                                }
                            }
                        });
                        builder.show();
                    }
                });
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
