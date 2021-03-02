package com.ct274.attendanceapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.ct274.attendanceapp.helpers.RequestPermission;
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
                "Get barcode",
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
                case 2:
                    openGetBarcodeDialog(this);
                    break;
                case 3:
                    startActivity(new Intent(UserMenuActivity.this, CreateMeetingActivity.class));
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(UserMenuActivity.this, LoginActivity.class));
                    break;
                default:
                    startActivity(new Intent(UserMenuActivity.this, MyProfileActivity.class));
                    break;
            }
        });
    }

    private void openGetBarcodeDialog(Context context) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View getBarcodeDialog = layoutInflater.inflate(R.layout.dialog_barcode, null);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setView(getBarcodeDialog);
        AlertDialog alertDialog = alertBuilder.create();
        String username = "b1709272";
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
}