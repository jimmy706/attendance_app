package com.ct274.attendanceapp.requests;

import com.ct274.attendanceapp.config.Endpoints;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AttendanceRequests {
    public Response listAttendance(String token, int page, int size) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Endpoints.API_URL + "attendances/list/")
                .addHeader("Authorization", "Bearer " + token)
                .get()
                .build();

        Call call = client.newCall(request);

        return call.execute();
    }

    public Response meetingRegister(String token, String meetingId) throws IOException{
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder().build();
        Request request = new Request.Builder()
                .url(Endpoints.API_URL + "attendances/attendance-register/" + meetingId + "/")
                .addHeader("Authorization", "Bearer " + token)
                .post(body)
                .build();

        Call call = client.newCall(request);

        return call.execute();
    }

    public Response getMeetingDetail(String token, String meetingId) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Endpoints.API_URL + "attendances/" + meetingId + "/")
                .addHeader("Authorization", "Bearer " + token)
                .get()
                .build();

        return client.newCall(request).execute();
    }

    public Response listMyMeeting(String token) throws  IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Endpoints.API_URL + "attendances/my-attendances/")
                .addHeader("Authorization", "Bearer " + token)
                .get()
                .build();

        return client.newCall(request).execute();
    }

    public Response listMyRegisteredMeetings(String token) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Endpoints.API_URL + "attendances/my-registered-attendances/")
                .addHeader("Authorization", "Bearer " + token)
                .get()
                .build();

        return client.newCall(request).execute();
    }
}
