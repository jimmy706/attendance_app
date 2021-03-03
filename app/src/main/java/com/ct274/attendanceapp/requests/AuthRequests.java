package com.ct274.attendanceapp.requests;

import android.content.Context;

import com.ct274.attendanceapp.config.Endpoints;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthRequests {
    private Context context;

    public AuthRequests (Context context) {
        this.context = context;
    }


    public Call loginRequest(String username, String password, Callback callback) throws Exception {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .build();

        Request request = new Request.Builder()
                .url(Endpoints.API_URL + "api/token/")
                .post(body)
                .build();

        Call call = client.newCall(request);

        call.enqueue(callback);
        return call;
    }


}
