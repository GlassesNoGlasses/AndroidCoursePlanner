package com.example.androidcourseplanner_final;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.androidcourseplanner_final.databinding.MainActivityBinding;
import java.util.List;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;
    private static String courseCode;
    private static HashMap<Integer, HashMap<Integer, List<String>>> timeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = MainActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NavController navController = Navigation.findNavController(
                this, R.id.nav_host_fragment_content_main);

    }

    public void setCourseCode(String code) {
        courseCode = code;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setTimeline(HashMap<Integer, HashMap<Integer, List<String>>> timeline) {this.timeline = timeline;}

    public HashMap<Integer, HashMap<Integer, List<String>>> getTimeline() { return this.timeline;}
}
