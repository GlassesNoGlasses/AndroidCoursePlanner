package com.example.androidcourseplanner_final;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.androidcourseplanner_final.databinding.SignupBinding;

import Backend.AuthenticationCallback;
import Backend.LoginModel;
import Backend.UserInfoChecker;


public class Signup extends Fragment {
    private SignupBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = com.example.androidcourseplanner_final.databinding.SignupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Signup.this)
                        .navigate(R.id.action_Signup_to_Login);
            }
        });

        binding.SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.username.getText().toString();
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                String errorCheck = Backend.UserInfoChecker.checkAll(username, email, password);

                if (errorCheck != null) {
                    Toast.makeText(getContext(), errorCheck, Toast.LENGTH_SHORT).show();
                    return;
                }
                LoginModel.getInstance().signUp(username, email, password, new AuthenticationCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(),
                                "Account created! Welcome", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(Signup.this)
                                .navigate(R.id.action_Signup_to_Login);
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getContext(),
                                "Signup Failed", Toast.LENGTH_SHORT).show();
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

