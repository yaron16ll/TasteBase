package com.example.tastebase.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tastebase.R;

public class SignUp extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
     private TextView signUpText, passwordError, usernameError;
    private EditText userEditTxt, passwordEditTxt;

    private Spinner countrySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
         findViews();
        initViews();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void initViews() {
        String passwordRegex = "^(?=(?:.*[A-Z]){1})(?=(?:.*[a-z]){5})(?=(?:.*\\d){3}).{9}$";
        String usernameRegex = "^[a-z]{5}$";

        isUserInputValid(passwordEditTxt, passwordError, passwordRegex);
        isUserInputValid(userEditTxt, usernameError, usernameRegex);

        countrySpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> countryAdapter = ArrayAdapter.createFromResource(this,
                R.array.countrylist, android.R.layout.simple_spinner_item);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);

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
        countrySpinner = (Spinner) findViewById(R.id.country_list);


    }
}

