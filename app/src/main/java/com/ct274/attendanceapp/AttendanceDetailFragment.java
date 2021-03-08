package com.ct274.attendanceapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ct274.attendanceapp.models.Attendance;
import com.google.android.gms.vision.barcode.Barcode;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.Objects;

public class AttendanceDetailFragment extends Fragment {
    private final String [] makeARoleTypes = new String[] {"Select joined members", "Scan barcode"};
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private final Attendance attendance;

    public AttendanceDetailFragment(Attendance attendance) {
        // Required empty public constructor
        this.attendance = attendance;
    }


    private void requestCameraPermission() {
        if(ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(getContext(), BarcodeScanActivity.class));
        }
        else {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    private void startBarcodeScanActivity() {
        Intent intent = new Intent(getActivity(), BarcodeScanActivity.class);
        Bundle sendData = new Bundle();
        sendData.putString("attendance_id", attendance.getId());
        intent.putExtras(sendData);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_attendance_detail, container, false);


        Button toggleJoinAttendanceBtn = rootView.findViewById(R.id.join_attendance_btn);
        if(!attendance.isHost()){
            toggleJoinAttendanceBtn.setVisibility(View.GONE);
        }

        TextView title = rootView.findViewById(R.id.title);
        TextView datetime = rootView.findViewById(R.id.datetime);
        TextView description = rootView.findViewById(R.id.description);
        TextView username = rootView.findViewById(R.id.username);
        TextView full_name = rootView.findViewById(R.id.full_name);
        ToggleButton registerButton = rootView.findViewById(R.id.register_button);

        title.setText(attendance.getTitle());
        datetime.setText(attendance.getFormatDay() + ", " + attendance.getStart_time() + " - " + attendance.getEnd_time());
        description.setText(attendance.getDescription());
        username.setText(attendance.getCreator().getAccount().getUsername());
        full_name.setText(attendance.getCreator().getFull_name());
        registerButton.setChecked(attendance.isRegistered());

        if(toggleJoinAttendanceBtn != null) {
            toggleJoinAttendanceBtn.setOnClickListener(v -> {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(v.getContext());
                alertBuilder.setTitle("Pick a method");
                alertBuilder.setItems(makeARoleTypes, (dialog, which) -> {
                    switch (which){
                        case 1:
                            requestCameraPermission();
                            break;
                        default:
                            Toast.makeText(getContext(), makeARoleTypes[which], Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
                alertBuilder.show();
            });
        }

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CAMERA_PERMISSION) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startBarcodeScanActivity();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}