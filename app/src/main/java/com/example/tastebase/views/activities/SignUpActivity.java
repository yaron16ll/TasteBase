package com.example.tastebase.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.tastebase.R;
import com.example.tastebase.controllers.SignUpController;
import com.example.tastebase.databinding.SignupActivityBinding;
import com.example.tastebase.models.User;
import com.example.tastebase.utilities.UserViewModel;
import com.example.tastebase.utilities.interfaces.GetUsersFromDB;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private SignupActivityBinding binding;
    private User user;
    private boolean isPasswordValid, isUsernameValid, isCountryValid;
    private SignUpController signUpController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDataBinding();
        signUpController = new SignUpController(this);
        initViews();
    }

    private void initDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.signup_activity);
        UserViewModel viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        user = new User();
        binding.setUser(user);
        binding.setLifecycleOwner(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            isCountryValid = false;
        } else {
            user.setCountry(parent.getItemAtPosition(position).toString());
            isCountryValid = true;
        }
        checkFieldsForValidity();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // No action needed
    }

    private void checkFieldsForValidity() {
        binding.signupButton.setEnabled(isPasswordValid && isUsernameValid && isCountryValid);
    }

    private void passToLoginActivity() {
        signUpController.getUsersFromDB(new GetUsersFromDB() {
            @Override
            public void onSuccess(ArrayList<User> users) {
                if (!signUpController.isUserExist(users, user.getUsername())) {
                    signUpController.addUserToDB(user, aVoid -> {
                        showToast("You are signed up");
                        moveToLoginActivity();
                    }, e -> showToast("Failed to add user: " + e.getMessage()));
                } else {
                    showToast("Username already exists!");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                showToast("Sign up failed: " + errorMessage);
            }
        });
    }

    private void moveToLoginActivity() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void isPasswordValid(TextView errorMsg, String regex) {
        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

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
            public void afterTextChanged(Editable s) {}
        });
    }

    private void isUsernameValid(TextView errorMsg, String regex) {
        binding.username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

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
            public void afterTextChanged(Editable s) {}
        });
    }

    private void initViews() {
        String passwordRegex = "^(?=(?:.*[A-Z]){1})(?=(?:.*[a-z]){5})(?=(?:.*\\d){3}).{9}$";
        String usernameRegex = "^[a-z0-9]{5}$";

        isPasswordValid(binding.passwordError, passwordRegex);
        isUsernameValid(binding.usernameError, usernameRegex);

        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(this,
                R.array.countrylist, android.R.layout.simple_spinner_item);

        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.countryList.setAdapter(countryAdapter);
        binding.countryList.setOnItemSelectedListener(this);

        binding.signupButton.setOnClickListener(v -> passToLoginActivity());
    }

    // Reusable showToast method
    private void showToast(String message) {
        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
