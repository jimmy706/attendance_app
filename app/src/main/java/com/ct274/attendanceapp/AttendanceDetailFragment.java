package com.ct274.attendanceapp;

import android.Manifest;
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
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import org.jetbrains.annotations.NotNull;

public class AttendanceDetailFragment extends Fragment {
    private String [] makeARoleTypes = new String[] {"Select joined members", "Scan barcode"};
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private String attendanceId;

    public AttendanceDetailFragment(String attendanceId) {
        // Required empty public constructor
        this.attendanceId = attendanceId;
    }


    private void requestCameraPermission() {
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(getContext(), BarcodeScanActivity.class));
        }
        else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
    }

    private void startBarcodeScanActivity() {
        Intent intent = new Intent(getActivity(), BarcodeScanActivity.class);
        Bundle sendData = new Bundle();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_attendance_detail, container, false);


        Button toggleJoinAttendanceBtn = rootView.findViewById(R.id.join_attendance_btn);
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