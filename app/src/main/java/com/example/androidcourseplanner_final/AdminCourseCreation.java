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

import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import java.util.ArrayList;
import android.widget.TextView;

import java.util.List;

import Backend.Course;
import Backend.CourseManager;
import Backend.GetCoursesCallback;
import Backend.Session;

public class AdminCourseCreation extends Fragment {
    private AdminCourseCreationBinding binding;
    private Course newCourse;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        newCourse = new Course("", "");
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

        binding.prerequisiteEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                ArrayList<Integer> courseList = new ArrayList<>();

                CourseManager.getInstance().getCourses(new GetCoursesCallback() {
                    @Override
                    public void onCallback(List<Course> courses) {
                        TextView preRequisiteText = view.findViewById(R.id.prerequisite_entry);

                        String[] preReqArray = new String[courses.size()];

                        for(int i = 0; i < courses.size(); i++){
                            preReqArray[i] = courses.get(i).getCourseCode();
                        }

                        boolean[] selectedPrerequisites = new boolean[courses.size()];

                        builder.setTitle("Select Prerequisites");

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
                                String textBox = new String();
                                for(int j = 0; j < courseList.size(); j++) {
                                    newCourse.addPrerequisite(preReqArray[courseList.get(j)]);

                                    textBox += preReqArray[courseList.get(j)];
                                    if(j != courseList.size() - 1) {
                                        textBox += ", ";
                                    }
                                }

                                preRequisiteText.setText(textBox);
                                courseList.clear();
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
                });
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

                newCourse.setName(courseName);
                newCourse.setCourseCode(courseCode);

                if (binding.fallCheckBox.isChecked())
                    newCourse.addSession(Session.Fall);
                if (binding.summerCheckBox.isChecked())
                    newCourse.addSession(Session.Summer);
                if (binding.winterCheckBox.isChecked())
                    newCourse.addSession(Session.Winter);

                //prerequisites are added from the dialog above

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
                        CourseManager.getInstance().addCourse(newCourse);

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
