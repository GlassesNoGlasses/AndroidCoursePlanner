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

                if (username.equals("") || email.equals("") || password.equals("")) {
                    Toast.makeText(getContext(),
                            "Fill in empty fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Backend.Login.signUp(username, email, password, new AuthenticationCallback() {
                    @Override
                    public void onSuccess() {
                        NavHostFragment.findNavController(Signup.this)
                                .navigate(R.id.action_Signup_to_Login);
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getContext(),
                                "Sign In Failed", Toast.LENGTH_SHORT).show();
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

