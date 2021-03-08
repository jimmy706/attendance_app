package com.ct274.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ct274.attendanceapp.components.LoadingDialog;
import com.ct274.attendanceapp.models.RegisterUser;
import com.ct274.attendanceapp.requests.AuthRequests;
import com.ct274.attendanceapp.ui.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import okhttp3.Response;


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


            if(isValidated()) {
                LoadingDialog loadingDialog = new LoadingDialog(RegisterActivity.this, "Creating new account");
                loadingDialog.startLoadingDialog();

                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                String first_name = firstNameInput.getText().toString();
                String last_name = lastNameInput.getText().toString();
                String email = emailInput.getText().toString();
                String description = "";
                String major = majorInput.getSelectedItem().toString();

                RegisterUser registerUser = new RegisterUser(username, email, first_name, last_name, password, major, description);
                AuthRequests authRequests = new AuthRequests(this);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Response response = authRequests.registerRequest(registerUser);
                            String jsonData = Objects.requireNonNull(response.body()).string();
                            System.out.println(jsonData);
                            JSONObject jsonObject = new JSONObject(jsonData);
                            if(jsonObject.has("account")) {
                                RegisterActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this, "Create account successfully, login to continue", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                    }
                                });
                            }
                            else if(jsonObject.length() > 0) {
                                JSONArray names = jsonObject.names();
                                for(int i = 0; i < 1; i++) {
                                    String key = (String) names.get(i);
                                    String message = jsonObject.getString(key);
                                    RegisterActivity.this.runOnUiThread(()-> {
                                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                    });
                                }
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                        finally {
                           RegisterActivity.this.runOnUiThread(loadingDialog::closeDialog);
                        }
                    }
                }).start();
            }

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