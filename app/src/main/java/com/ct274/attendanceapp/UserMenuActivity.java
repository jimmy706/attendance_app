package com.ct274.attendanceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.ct274.attendanceapp.ui.login.LoginActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class UserMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        ArrayList<String> options = new ArrayList<>(Arrays.asList(
                "My profile",
                "Create new meeting",
                "My registered meeting",
                "Enrolled history",
                "Logout"
        ));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, options);
        ListView optionsMenu = findViewById(R.id.options_menu);
        optionsMenu.setAdapter(adapter);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            finish();
        });

        optionsMenu.setOnItemClickListener((parent, view, position, id) -> {
            switch (position){
                case 1:
                    startActivity(new Intent(UserMenuActivity.this, CreateMeetingActivity.class));
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UserMenuActivity.this, LoginActivity.class));
                    break;
                default:
                    break;
            }
        });
    }
}