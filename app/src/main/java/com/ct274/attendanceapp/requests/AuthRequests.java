package com.ct274.attendanceapp.requests;

import android.content.Context;

import com.ct274.attendanceapp.config.Endpoints;
import com.ct274.attendanceapp.models.RegisterUser;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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


    public Response loginRequest(String username, String password) throws Exception {
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

        return call.execute();
    }


    public Response registerRequest(RegisterUser registerUser) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        RequestBody body = new FormBody.Builder()
                .add("username", registerUser.getUsername())
                .add("password", registerUser.getPassword())
                .add("first_name", registerUser.getFirst_name())
                .add("last_name", registerUser.getLast_name())
                .add("description", registerUser.getDescription())
                .add("email", registerUser.getEmail())
                .add("major", registerUser.getMajor())
                .build();

        Request request = new Request.Builder()
                .url(Endpoints.API_URL + "attendances/create-account/")
                .post(body)
                .build();

        Call call = client.newCall(request);

        return call.execute();
    }

    public Response getMyProfile(String token) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url(Endpoints.API_URL + "attendances/profile/")
                .get()
                .addHeader("Authorization", "Bearer " + token)
                .build();

        Call call = client.newCall(request);

        return call.execute();
    }
}
