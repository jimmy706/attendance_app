package com.ct274.attendanceapp.components;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ct274.attendanceapp.R;

public class LoadingDialog {
    Activity activity;
    AlertDialog dialog;
    String message = "Please wait...";

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public LoadingDialog(Activity activity, String message) {
        this.activity = activity;
        this.message = message;
    }

    public void startLoadingDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.loading_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);

        TextView messageText = dialogView.findViewById(R.id.loading_message);
        if(messageText != null) {
            messageText.setText(message);
        }

        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void closeDialog() {
        dialog.dismiss();
    }
}
