package utils;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by bastien on 11/6/14.
 */
public class WavedApplication extends Application {

    RequestQueue queue = null;

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    public RequestQueue getRequestQueue()
    {
        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }

        return queue;
    }
}
