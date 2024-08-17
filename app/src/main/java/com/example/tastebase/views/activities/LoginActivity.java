package com.example.tastebase.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.tastebase.R;
import com.example.tastebase.controllers.LoginController;
import com.example.tastebase.databinding.LoginActivityBinding;
import com.example.tastebase.models.User;
import com.example.tastebase.utilities.UserViewModel;
import com.example.tastebase.utilities.interfaces.GetUsersFromDB;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private LoginActivityBinding binding;
    private User user;
    private boolean isPasswordValid, isUsernameValid;
    private LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Data Binding
        initDataBinding();

        // Initialize Controller
        loginController = new LoginController(this);

        // Initialize SharedPreferences
        loginController.initSharedPreferences();

        // Initialize Views and set up listeners
        initViews();
    }

    private void initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.login_activity);
        UserViewModel viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        user = new User();
        binding.setUser(user);
        binding.setLifecycleOwner(this);
    }

    private void initViews() {
        String passwordRegex = "^(?=(?:.*[A-Z]){1})(?=(?:.*[a-z]){5})(?=(?:.*\\d){3}).{9}$";
        String usernameRegex = "^[a-z0-9]{5}$";

        binding.signup.setOnClickListener(v -> passToSignUpActivity());
        binding.loginButton.setOnClickListener(v -> passToHomePageActivity());

        isPasswordValid(binding.passwordError, passwordRegex);
        isUsernameValid(binding.usernameError, usernameRegex);
    }

    private void passToSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void passToHomePageActivity() {
        user.setUsername(binding.username.getText().toString());
        user.setPassword(binding.password.getText().toString());

        if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            showToast("Username or password cannot be empty!");
            return;
        }

        loginController.validateUser(user.getUsername(), user.getPassword(), new GetUsersFromDB() {
            @Override
            public void onSuccess(ArrayList<User> users) {
                User validatedUser = loginController.isUserRegistered(users, user.getUsername(), user.getPassword());
                if (validatedUser != null) {
                    showToast("You're logged in!");
                    loginController.saveUserToSharedPreferences(validatedUser);
                    startActivity(new Intent(LoginActivity.this, HomePageActivity.class));
                    finish();
                } else {
                    showToast("Invalid username or password!");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                showToast("Login failed: " + errorMessage);
            }
        });
    }

    private void isPasswordValid(TextView errorMsg, String regex) {
        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                if (newText.matches(regex)) {
                    user.setPassword(newText);
                    isPasswordValid = true;
                    errorMsg.setVisibility(View.INVISIBLE);
                } else {
                    errorMsg.setVisibility(View.VISIBLE);
                    isPasswordValid = false;
                }
                checkFieldsForValidity();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void isUsernameValid(TextView errorMsg, String regex) {
        binding.username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                if (newText.matches(regex)) {
                    user.setUsername(newText);
                    isUsernameValid = true;
                    errorMsg.setVisibility(View.INVISIBLE);
                } else {
                    errorMsg.setVisibility(View.VISIBLE);
                    isUsernameValid = false;
                }
                checkFieldsForValidity();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void checkFieldsForValidity() {
        binding.loginButton.setEnabled(isPasswordValid && isUsernameValid);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
