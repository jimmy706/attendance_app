package com.ct274.attendanceapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.ct274.attendanceapp.components.LoadingDialog;
import com.ct274.attendanceapp.requests.AuthRequests;
import com.ct274.attendanceapp.ui.login.LoginActivity;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    AuthRequests authRequests;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.register_button);
        authRequests = new AuthRequests(this);

        loginButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
        registerButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });

        if(!isNetworkConnected()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getResources().getString(R.string.no_network));
            alertDialogBuilder.setMessage(getResources().getString(R.string.check_network));

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_tokens) ,Context.MODE_PRIVATE);
        String rfToken = sharedPreferences.getString(getString(R.string.refresh_token), "");
        if(!rfToken.isEmpty()) {
            loadingDialog = new LoadingDialog(this, "Logging...");
            loadingDialog.startLoadingDialog();
            getTokenAndLogin(rfToken);
        }
    }

    private void getTokenAndLogin(String rfToken) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Response response = authRequests.refreshToken(rfToken);
                    if(response.isSuccessful()){
                        String data = response.body().string();
                        JSONObject jsonObject = new JSONObject(data);

                        if(jsonObject.has("access")) {
                            String access = jsonObject.getString("access");

                            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_tokens), Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(getString(R.string.access_token), access);
                            editor.apply();
                            startActivity(new Intent(MainActivity.this, Home.class));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    MainActivity.this.runOnUiThread(()-> {
                        Toast.makeText(MainActivity.this, "Failed to logging", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_tokens), Context.MODE_PRIVATE);
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
                        editor.remove(getString(R.string.access_token));
                        editor.remove(getString(R.string.refresh_token));
                        editor.apply();
                    });
                }
                finally {
                    MainActivity.this.runOnUiThread(()->{
                        loadingDialog.closeDialog();
                    });
                }
            }
        }).start();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}