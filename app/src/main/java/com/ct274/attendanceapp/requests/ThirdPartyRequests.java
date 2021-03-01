package com.ct274.attendanceapp.requests;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.URL;

public class ThirdPartyRequests {
    public static Bitmap getImageFromUrl(String path) {
        Bitmap result = null;

        try{
            URL url = new URL(path);
            result = BitmapFactory.decodeStream(url.openStream());
            System.out.println(result);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error!!");
        }


        return result;
    }
}
