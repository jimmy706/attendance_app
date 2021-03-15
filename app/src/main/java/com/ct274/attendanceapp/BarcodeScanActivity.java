package com.ct274.attendanceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ct274.attendanceapp.components.LoadingDialog;
import com.ct274.attendanceapp.requests.AttendanceRequests;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

public class BarcodeScanActivity extends AppCompatActivity {
    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    //This class provides methods to play DTMF tones
    private ToneGenerator toneGen1;
    private String meetingId;
    private String accessToken;
    private LoadingDialog loadingDialog;
    private boolean allowScan = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scan);
        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        surfaceView  = findViewById(R.id.surface_view);
        loadingDialog = new LoadingDialog(BarcodeScanActivity.this);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_tokens) , Context.MODE_PRIVATE);
        accessToken = sharedPreferences.getString(getString(R.string.access_token), "");
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            meetingId = bundle.getString("attendance_id");
        }
        initialiseDetectorsAndSources();

        ImageButton backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(v -> {
            finish();
        });
    }

    private void initialiseDetectorsAndSources() {
        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        cameraSource = new CameraSource
                .Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                try {
                    if(ActivityCompat.checkSelfPermission(BarcodeScanActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        cameraSource.start(surfaceView.getHolder());
                    }
                    else {
                        finish();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(),"To prevent memory leak, barcode has been stopped",Toast.LENGTH_SHORT).show();
            }

            @SuppressLint("MissingPermission")
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if(barcodes.size() != 0 && allowScan) {
                    allowScan = false;
                    AttendanceRequests attendanceRequests = new AttendanceRequests();
                    String username = barcodes.valueAt(0).displayValue;
                    BarcodeScanActivity.this.runOnUiThread(()-> {
                        loadingDialog.startLoadingDialog();
                        MediaPlayer mediaPlayer =  MediaPlayer.create(BarcodeScanActivity.this, R.raw.sound);
                        mediaPlayer.start();
                    });


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            cameraSource.stop();
                            try {
                                Response response = attendanceRequests.attendMeetingViaUsername(accessToken, username, meetingId);
                                String data = response.body().string();
                                if(response.isSuccessful()) {
                                    JSONObject jsonObject = new JSONObject(data);
                                    JSONObject enroller = jsonObject.getJSONObject("enroller");
                                    String first_name = enroller.getString("first_name");
                                    String last_name = enroller.getString("last_name");
                                    String username = enroller.getString("username");
                                    BarcodeScanActivity.this.runOnUiThread(()-> {
                                        Toast.makeText(BarcodeScanActivity.this, username + " - " + first_name + " " + last_name, Toast.LENGTH_SHORT).show();
                                    });
                                }
                                else {
                                    BarcodeScanActivity.this.runOnUiThread(()-> {
                                        Toast.makeText(BarcodeScanActivity.this, "Failed to scan this barcode", Toast.LENGTH_SHORT).show();
                                    });
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                                BarcodeScanActivity.this.runOnUiThread(()-> {
                                    Toast.makeText(BarcodeScanActivity.this, "Failed to scan this barcode", Toast.LENGTH_SHORT).show();
                                });
                            }
                            finally {
                                BarcodeScanActivity.this.runOnUiThread(()->loadingDialog.closeDialog());
                                allowScan = true;
                                try {
                                    cameraSource.start(surfaceView.getHolder());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }

            }
        });
    }
}