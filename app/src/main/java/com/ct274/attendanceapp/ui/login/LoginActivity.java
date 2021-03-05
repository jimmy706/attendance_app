package com.ct274.attendanceapp.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ct274.attendanceapp.Home;
import com.ct274.attendanceapp.R;
import com.ct274.attendanceapp.RegisterActivity;
import com.ct274.attendanceapp.config.Endpoints;
import com.ct274.attendanceapp.requests.AuthRequests;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final TextView registerAction = findViewById(R.id.register_account_action);

        registerAction.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });


        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(username,
                        password);

                AuthRequests authRequests = new AuthRequests(getApplicationContext());

                new Thread(){
                  public void run() {
                      try {
                          Response response = authRequests.loginRequest(username, password);
                          if(response.code() < 300) {
                              String jsonData = Objects.requireNonNull(response.body()).string();
                              System.out.println(jsonData);
                              try {
                                  JSONObject jsonObject = new JSONObject(jsonData);
                                  if(jsonObject.has("access") && jsonObject.has("refresh")) {
                                      String access = jsonObject.getString("access");
                                      String refresh = jsonObject.getString("refresh");
                                      // TODO: Saved tokens to SharedPreferences
                                      SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_tokens), Context.MODE_PRIVATE);
                                      SharedPreferences.Editor editor = sharedPref.edit();
                                      editor.putString(getString(R.string.access_token), access);
                                      editor.putString(getString(R.string.refresh_token), refresh);
                                      editor.apply();
                                      startActivity(new Intent(LoginActivity.this, Home.class));
                                      LoginActivity.this.runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show());
                                  }

                              } catch (JSONException e) {
                                  e.printStackTrace();
                              }
                          }
                          else {
                              LoginActivity.this.runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Wrong username or password", Toast.LENGTH_SHORT).show());
                          }
                      } catch (Exception e) {
                          e.printStackTrace();
                      }
                        finally {
                          loadingProgressBar.setVisibility(View.INVISIBLE);
                      }
                  }
                }.start();


            }
        });
    }


}