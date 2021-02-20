package com.ct274.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ct274.attendanceapp.ui.login.LoginActivity;


public class RegisterActivity extends AppCompatActivity {
    final  String[] majors = new String[]{
            "Computer Science",
            "Networking",
            "Information Technology",
            "Economy",
            "International Business"
    };
    EditText firstNameInput;
    EditText lastNameInput;
    EditText usernameInput;
    EditText emailInput;
    EditText passwordInput;
    Spinner majorInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstNameInput = findViewById(R.id.first_name_input);
        lastNameInput = findViewById(R.id.last_name_input);
        usernameInput = findViewById(R.id.username_input);
        passwordInput = findViewById(R.id.password_input);
        emailInput = findViewById(R.id.email_input);
        majorInput = findViewById(R.id.major_input);
        Spinner majorInput = findViewById(R.id.major_input);
        ArrayAdapter<String> majorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, majors);
        majorInput.setAdapter(majorAdapter);

        TextView loginAction = findViewById(R.id.login_action);
        loginAction.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
;
        Button signUpButton = findViewById(R.id.sign_up_btn);
        signUpButton.setOnClickListener(v -> {
            isValidated();

        });
    }

    private boolean isValidated() {
        boolean result = true;
        if(firstNameInput.getText().length() == 0){
            firstNameInput.setError("First name is required");
            result = false;
        }
        if(lastNameInput.getText().length() == 0) {
            lastNameInput.setError("Last name is required");
            result = false;
        }
        if(usernameInput.getText().length() == 0) {
            usernameInput.setError("Username is required");
            result = false;
        }
        if(emailInput.getText().length() == 0) {
            emailInput.setError("Email is required");
            result = false;
        }
        if(passwordInput.getText().length() == 0) {
            passwordInput.setError("Password is required");
            result = false;
        }
        return result;
    }
}