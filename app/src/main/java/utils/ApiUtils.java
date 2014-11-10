package utils;

import android.app.Activity;
import android.net.Uri;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by bastien on 11/6/14.
 */
public class ApiUtils {

    private static String getBasePath() {
        return Constants.PRODUCTION ? Constants.PROD_BASE_URL + "/api/v" + Constants.API :
                                                  Constants.DEV_BASE_URL + "/api/v" + Constants.API;
    }

    public static void requestSmsCode(Activity activity, final String phoneNumber,
                     final boolean retry, Response.Listener success, Response.ErrorListener failure)
    {
        Map<String, Object> params = new HashMap<String, Object>();

        String url = getBasePath() + "/sessions.json";

        GeneralUtils.getRequestQueue(activity).add(new StringRequest(Request.Method.POST, url,
                success, failure) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                if (retry) {
                    params.put("retry", "dummy");
                }
                params.put("phone_number", phoneNumber);

                return params;
            }
        });
    }

    public static void checkConfirmationCode(Activity activity, String phoneNumber, String code,
                                          Response.Listener success, Response.ErrorListener failure)
    {
        String url = getBasePath() + "/sessions/confirm_sms_code.json" + "?phone_number="
                                                        + Uri.encode(phoneNumber) + "&code=" + code;

        GeneralUtils.getRequestQueue(activity).add(new StringRequest(Request.Method.GET, url,
                                                                                 success, failure));
    }
}
