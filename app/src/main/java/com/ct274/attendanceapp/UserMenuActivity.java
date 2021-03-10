package com.ct274.attendanceapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ct274.attendanceapp.helpers.RequestPermission;
import com.ct274.attendanceapp.states.UserState;
import com.ct274.attendanceapp.ui.login.LoginActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class UserMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        ArrayList<String> options = new ArrayList<>(Arrays.asList(
                "My profile",
                "My meeting",
                "My registered meeting",
                "Get barcode",
                "Join meeting",
                "Create new meeting",
                "Logout"
        ));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, options);
        ListView optionsMenu = findViewById(R.id.options_menu);
        optionsMenu.setAdapter(adapter);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> {
            finish();
        });

        TextView username = findViewById(R.id.username);
        username.setText(UserState.getInstance().getUsername());

        optionsMenu.setOnItemClickListener((parent, view, position, id) -> {
            switch (position){
                case 2:
                    viewMyProfile(1);
                    break;
                case 3:
                    openGetBarcodeDialog(this);
                    break;
                case 4:
                    openJoinMeetingDialog(this);
                    break;
                case 5:
                    startActivity(new Intent(UserMenuActivity.this, CreateMeetingActivity.class));
                    break;
                case 6:
                    logout();
                    Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UserMenuActivity.this, LoginActivity.class));
                    break;
                default:
                    viewMyProfile(0);
                    break;
            }
        });
    }

    private void logout() {
        // Remove user's tokens
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_tokens), Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(getString(R.string.access_token));
        editor.remove(getString(R.string.refresh_token));
        editor.apply();
    }

    private void openGetBarcodeDialog(Context context) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View getBarcodeDialog = layoutInflater.inflate(R.layout.dialog_barcode, null);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setView(getBarcodeDialog);
        AlertDialog alertDialog = alertBuilder.create();
        String username = UserState.getInstance().getUsername();
        ImageView barcodeImage = getBarcodeDialog.findViewById(R.id.barcode_view);
        Button okButton = getBarcodeDialog.findViewById(R.id.ok_button);
        okButton.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        try {
            // TODO: Get screen solution of device
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            Writer codeWriter;
            codeWriter = new Code128Writer();
            BitMatrix byteMatrix = codeWriter.encode(username, BarcodeFormat.CODE_128, displayMetrics.widthPixels, 400, hintMap);
            int width = byteMatrix.getWidth();
            int height = byteMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for(int i = 0; i < width; i++) {
                for(int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j, byteMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }
            barcodeImage.setImageBitmap(bitmap);

            // TODO: Increase screen brightness
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, 100);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        alertDialog.show();
    }

    private void openJoinMeetingDialog(Context context) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View joinMeetingDialog = layoutInflater.inflate(R.layout.join_meeting_dialog, null);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setView(joinMeetingDialog);
        AlertDialog alertDialog = alertBuilder.create();

        EditText joinKeyInput = joinMeetingDialog.findViewById(R.id.join_key);
        Button joinButton = joinMeetingDialog.findViewById(R.id.join_btn);
        joinButton.setOnClickListener(v -> {
            String joinKey = joinKeyInput.getText().toString();
            Toast.makeText(context, joinKey, Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void viewMyProfile(int tab) {
        Bundle bundle = new Bundle();
        bundle.putInt("tab", tab);
        Intent intent = new Intent(UserMenuActivity.this, MyProfileActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}