package com.example.androidcourseplanner_final;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.androidcourseplanner_final.databinding.MainActivityBinding;

public class MainActivity extends AppCompatActivity {

    private MainActivityBinding binding;
    private static String courseCode;

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
}
