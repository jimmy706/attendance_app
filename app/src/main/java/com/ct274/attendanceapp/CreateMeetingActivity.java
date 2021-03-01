package com.ct274.attendanceapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ct274.attendanceapp.helpers.StringHandle;

import java.util.Calendar;
import java.util.Date;

public class CreateMeetingActivity extends AppCompatActivity {

    EditText dayInput, startTimeInput, endTimeInput, titleInput, descriptionInput;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meeting);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minus = calendar.get(Calendar.MINUTE);

        dayInput = findViewById(R.id.input_day);
        startTimeInput = findViewById(R.id.input_start_time);
        endTimeInput = findViewById(R.id.input_end_time);
        titleInput = findViewById(R.id.input_title);
        descriptionInput = findViewById(R.id.input_description);

        dayInput.setText(StringHandle.formatDate(new Date()));


        dayInput.setOnFocusChangeListener((v, hasFocus) -> {
           if(hasFocus) {
               DatePickerDialog datePickerDialog = new DatePickerDialog(this);
               datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                   dayInput.setText(
                          (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "-" +
                           (month < 10 ? "0" + (month + 1) : (month + 1))
                           + "-" + year);
               });
               datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
               datePickerDialog.show();
           }
        });

        startTimeInput.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        (view, selectHour, selectMinus) -> {
                            startTimeInput.setText(
                                    (selectHour < 10 ? "0" + selectHour : selectHour) + ":" +
                                            (selectMinus < 10 ? "0" + selectMinus : selectMinus)
                                    );
                        },hour, minus, false );
                timePickerDialog.show();
            }

        });

        startTimeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) {
                    endTimeInput.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
                }
                else {
                    endTimeInput.setInputType(InputType.TYPE_NULL);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        endTimeInput.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        (view, selectHour, selectMinus) -> {
                            endTimeInput.setText(
                                    (selectHour < 10 ? "0" + selectHour : selectHour) + ":" +
                                            (selectMinus < 10 ? "0" + selectMinus : selectMinus)
                            );
                            Calendar endTimeCalendar = Calendar.getInstance();
                            endTimeCalendar.set(Calendar.HOUR, selectHour);
                            endTimeCalendar.set(Calendar.MINUTE, selectMinus);

                            String startTime = startTimeInput.getText().toString();
                            if(startTime.length() != 0) {
                                String[] splitVal = startTime.split(":");
                                String startTimeHour = splitVal[0];
                                String startTimeMinus = splitVal[1];

                                Calendar startTimeCalendar = Calendar.getInstance();
                                startTimeCalendar.set(Calendar.HOUR, Integer.parseInt(startTimeHour));
                                startTimeCalendar.set(Calendar.MINUTE, Integer.parseInt(startTimeMinus));

                                try {
                                     if(endTimeCalendar.compareTo(startTimeCalendar) < 0) {
                                         endTimeInput.setError(getResources().getText(R.string.end_time_validate));
                                     }
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },hour, minus, false );
                timePickerDialog.show();
            }
        });

        Button submitBtn = findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(v -> {
            if(isValidated()){
                String title = titleInput.getText().toString();
                String day = dayInput.getText().toString();
                String start_time = startTimeInput.getText().toString();
                String end_time = endTimeInput.getText().toString();
                String description = descriptionInput.getText().toString();
            }
        });

        ImageButton backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(v ->  {
            finish();
        });
    }

    private boolean isValidated() {
        boolean result = true;
        if(titleInput.getText().length() == 0) {
            result = false;
            titleInput.setError(getResources().getText(R.string.title_required));
        }
        if(dayInput.getText().length() == 0) {
            result = false;
            dayInput.setError(getResources().getText(R.string.day_required));
        }
        if(startTimeInput.getText().length() == 0) {
            result = false;
            startTimeInput.setError(getResources().getText(R.string.start_time_required));
        }
        if(endTimeInput.getText().length() == 0) {
            result = false;
            endTimeInput.setError(getResources().getText(R.string.end_time_required));
        }

        return  result;
    }

}