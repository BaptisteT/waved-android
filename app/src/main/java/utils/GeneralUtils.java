package utils;

import android.content.Context;

import com.android.volley.RequestQueue;

/**
 * Created by bastien on 11/6/14.
 */
public class GeneralUtils {

    public static RequestQueue getRequestQueue(Context ctx) {
        return ((WavedApplication) ctx.getApplicationContext()).getRequestQueue();
    }
}
