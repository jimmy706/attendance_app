package com.ct274.attendanceapp.requests;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.ct274.attendanceapp.config.Endpoints;
import com.ct274.attendanceapp.helpers.StringHandle;

import org.json.JSONArray;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AttendanceRequests {
    private static final MediaType JSON_TYPE
            = MediaType.parse("application/json; charset=utf-8");

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Response listAttendance(String token, int page, int size) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("page", Integer.toString(page));
        queryMap.put("size", Integer.toString(size));

        String url = Endpoints.API_URL + "attendances/list/?" + StringHandle.convertMapToQueryString(queryMap);
        Log.i("Request URL: ", url);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + token)
                .get()
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

    public Response requestJoinMeeting(String token, String meetingId) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .build();
        Request request = new Request.Builder()
                .url(Endpoints.API_URL + "attendances/attendance-register/" + meetingId + "/")
                .addHeader("Authorization", "Bearer " + token)
                .post(body)
                .build();

        return client.newCall(request).execute();
    }

    public Response requestLeaveMeeting(String token, String meetingId) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .build();

        Request request = new Request.Builder()
                .url(Endpoints.API_URL + "attendances/leave-attendance/" + meetingId + "/")
                .addHeader("Authorization", "Bearer " + token)
                .post(body)
                .build();

        return client.newCall(request).execute();
    }

    public Response listMeetingMembers(String meetingId) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Endpoints.API_URL + "attendances/"  + meetingId + "/members/")
                .get()
                .build();


        return client.newCall(request).execute();
    }

    public Response checkEnrollMultipleMembers(String token, String data, String meetingId) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(data, JSON_TYPE);
        Request request = new Request.Builder()
                .url(Endpoints.API_URL + "attendances/check-join-multiples/" + meetingId + "/")
                .addHeader("Authorization", "Bearer " + token)
                .put(body)
                .build();

        return client.newCall(request).execute();
    }

    public Response attendMeetingViaUsername(String token, String username, String meetingId) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .build();
        Request request = new Request.Builder()
                .url(Endpoints.API_URL + "attendances/add-join-member/" + meetingId + "/")
                .addHeader("Authorization", "Bearer " + token)
                .put(body)
                .build();

        return client.newCall(request).execute();
    }

    public Response joinMeetingWithKey(String token, String key) throws IOException {
        String url = "attendances/register-via-key/" + key +  "/";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder().build();
        Request request = new Request.Builder()
                .url(Endpoints.API_URL + url)
                .addHeader("Authorization", "Bearer " + token)
                .post(body)
                .build();

        return client.newCall(request).execute();
    }
}

