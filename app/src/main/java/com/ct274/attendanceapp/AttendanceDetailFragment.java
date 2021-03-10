package com.ct274.attendanceapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.ct274.attendanceapp.components.LoadingDialog;
import com.ct274.attendanceapp.components.MemberAdapter;
import com.ct274.attendanceapp.models.Attendance;
import com.ct274.attendanceapp.models.User;
import com.ct274.attendanceapp.requests.AttendanceRequests;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Response;

public class AttendanceDetailFragment extends Fragment {
    private final String [] makeARoleTypes = new String[] {"Select joined members", "Scan barcode"};
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private final Attendance attendance;
    private AttendanceRequests attendanceRequests = new AttendanceRequests();
    private String accessToken;
    private LoadingDialog loadingDialog;
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
        loadingDialog = new LoadingDialog(getActivity());
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
        String imagePath = "https://ui-avatars.com/api/?name=" + attendance.getCreator().getFull_name() +  "&background=0D8ABC&color=fff&rounded=true";
        CircleImageView avatar = rootView.findViewById(R.id.avatar);
        Picasso.get().load(imagePath)
                .placeholder(R.drawable.user_circle_icon)
                .error(R.drawable.user_circle_icon)
                .into(avatar);

        title.setText(attendance.getTitle());
        datetime.setText(attendance.getFormatDay() + ", " + attendance.getStart_time() + " - " + attendance.getEnd_time());
        description.setText(attendance.getDescription());
        username.setText(attendance.getCreator().getAccount().getUsername());
        full_name.setText(attendance.getCreator().getFull_name());
        registerButton.setChecked(attendance.isRegistered());

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.shared_tokens) , Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(getString(R.string.access_token), "");

        registerButton.setOnClickListener(v -> {
            if(attendance.isRegistered()) {
                loadingDialog.startLoadingDialog();
                leaveMeeting();
            }
            else {
                loadingDialog.startLoadingDialog();
                joinMeeting();
            }
        });

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
                            Intent intent = new Intent(getActivity(), CheckJoinedMembers.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("attendance_id", attendance.getId());
                            intent.putExtras(bundle);
                            startActivity(intent);
                            break;
                    }
                });
                alertBuilder.show();
            });
        }

        ImageButton shareBtn = rootView.findViewById(R.id.share_btn);
        if(attendance.getShareKey() != null) {
            shareBtn.setOnClickListener(v->{
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, attendance.getShareKey());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, "Share meeting key");
                startActivity(shareIntent);

            });
        }
        else {
            shareBtn.setVisibility(View.GONE);
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

    private void joinMeeting() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = attendanceRequests.requestJoinMeeting(accessToken, attendance.getId());
                    if(response.isSuccessful()) {
                        System.out.println(response.body().string());
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    loadingDialog.closeDialog();
                }
            }
        }).start();
    }

    private void leaveMeeting() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = attendanceRequests.requestLeaveMeeting(accessToken, attendance.getId());
                    if(response.isSuccessful()) {
                        System.out.println(response.body().string());
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    loadingDialog.closeDialog();
                }
            }
        }).start();
    }
}