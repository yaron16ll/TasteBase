package com.example.tastebase.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tastebase.R;

public class Login extends AppCompatActivity {
    private TextView signUpText, passwordError, usernameError;
    private EditText userEditTxt, passwordEditTxt;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        findViews();
        initViews();
    }

    private void initViews() {
        String passwordRegex = "^(?=(?:.*[A-Z]){1})(?=(?:.*[a-z]){5})(?=(?:.*\\d){3}).{9}$";
        String usernameRegex = "^[a-z]{5}$";


        passActivity(HomePage.class,loginButton);
        passActivity(SignUp.class,signUpText);


        isUserInputValid(passwordEditTxt, passwordError, passwordRegex);
        isUserInputValid(userEditTxt, usernameError, usernameRegex);

    }

    private void passActivity(Class targetClass, View button){

        button.setOnClickListener(v -> {

            Intent intent = new Intent(this, targetClass);
            intent.putExtra("key", "value"); // Add data to the intent
            startActivity(intent);
        });

    }





    private void isUserInputValid(EditText editText, TextView errorMsg, String regex) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String newText = s.toString();

                if (newText.matches(regex)) {
                    System.out.println("Password is valid.");
                    errorMsg.setVisibility(View.INVISIBLE);

                } else {
                    errorMsg.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private void findViews() {
        signUpText = findViewById(R.id.signup);
        userEditTxt = findViewById(R.id.username);
        passwordEditTxt = findViewById(R.id.password);
        usernameError = findViewById(R.id.usernameError);
        passwordError = findViewById(R.id.passwordError);
        loginButton = findViewById(R.id.login_button);

    }
}