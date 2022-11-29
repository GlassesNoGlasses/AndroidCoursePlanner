package com.example.androidcourseplanner_final;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.example.androidcourseplanner_final.databinding.LoginBinding;

import androidx.fragment.app.Fragment;

import Backend.Admin;
import Backend.GetProfileCallback;
import Backend.AuthenticationCallback;
import Backend.Student;

public class Login extends Fragment {
    private LoginBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = com.example.androidcourseplanner_final.databinding.LoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.Email.getText().toString();
                String password = binding.Password.getText().toString();

                if (email.equals("") || password.equals("")) {
                    Toast.makeText(getContext(),
                            "Fill in empty fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Backend.Login.signIn(email, password, new AuthenticationCallback() {
                    @Override
                    public void onSuccess() {
                        Backend.Login.getProfile(new GetProfileCallback() {
                            @Override
                            public void onStudent(Student student) {
                                NavHostFragment.findNavController(Login.this)
                                        .navigate(R.id.action_Login_to_StudentHome);
                            }

                            @Override
                            public void onAdmin(Admin admin) {
                                NavHostFragment.findNavController(Login.this)
                                        .navigate(R.id.action_Login_to_AdminHome);
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getContext(),
                                "Invalid Login Information", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.SignupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Login.this)
                        .navigate(R.id.action_Login_to_Signup);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}