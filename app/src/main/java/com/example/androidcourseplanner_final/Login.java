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

import Backend.GetProfileCallback;
import Backend.AuthenticationCallback;
import Backend.LoginModel;
import Backend.LoginPresenter;
import Backend.Profile;
import Backend.Student;
import Backend.UserInfoChecker;

public class Login extends Fragment {
    private LoginBinding binding;
    private LoginPresenter presenter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        presenter = new LoginPresenter(this, LoginModel.getInstance());
        binding = com.example.androidcourseplanner_final.databinding.LoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void generateMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public String getEmail() {
        return binding.Email.getText().toString();
    }

    public String getPassword() {
        return binding.Password.getText().toString();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(UserInfoChecker.checkEmail(getEmail()) != null || UserInfoChecker.checkPassword(getPassword()) != null) {
                    generateMessage("Missing required information");
                    return;
                }

                presenter.signIn(getEmail(), getPassword(), new AuthenticationCallback() {
                    @Override
                    public void onSuccess() {
                        LoginModel.getInstance().getProfile(new GetProfileCallback() {
                            @Override
                            public void onStudent(Student student) {
                                NavHostFragment.findNavController(Login.this)
                                        .navigate(R.id.action_Login_to_StudentHome);
                            }

                            @Override
                            public void onAdmin(Profile admin) {
                                NavHostFragment.findNavController(Login.this)
                                        .navigate(R.id.action_Login_to_AdminHome);
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        generateMessage("Invalid Login Information");
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