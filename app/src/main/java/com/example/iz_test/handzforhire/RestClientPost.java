package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/*
 * Created by IZ-parimala on 7/26/2015.
 */
public class RestClientPost {
    private String url;
    private int requestType;
    private static RequestQueue queue;
    private String TAG="Json Response";
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";
    private ProgressDialog pDialog;
    ConnectionDetector cd;
    Context context;
    public enum RequestMethod
    {
        POST
    };


    public RestClientPost(Context context, int requestType) {
        //this.url = "http://162.144.41.156/~izaapinn/handzforhire/service/job_search";
       this.url = "http://50.17.167.215/handz/service/job_search";
        this.requestType = requestType;
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        cd=new ConnectionDetector(context);
    }
    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void execute(RequestMethod method, Activity activity)
            throws Exception {
             Log.d("", "Request params " + url);
        this.context=activity;
        postData(url, activity,(ResponseListener)activity);
    }

    private void postData(String url, final Context activity, final ResponseListener replist) {

        if(cd.isConnectingToInternet()) {

            //queue = Volley.newRequestQueue(activity);
            if (queue == null) {
                queue = Volley.newRequestQueue(activity);
            }
            int timeout = 60000; // 60 seconds - time out

            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                replist.onResponseReceived(jsonResponse,requestType);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String s="{\"status\":\"error\",\"request_type\":\"job_lists\",\"msg\":\"No Jobs Found\",\"error_code\":\"8\"}";
                            System.out.println("volley error "+error.getMessage());
                            try {
                            JSONObject jsonResponse = new JSONObject(s);
                            replist.onResponseReceived(jsonResponse,requestType);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

            ) {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("X-APP-KEY","HandzForHire@~");
                    params.put("search_type", "location");
                    params.put("lat", String.valueOf(FindJobMap.lat));
                    params.put("lon", String.valueOf(FindJobMap.lon));
                    params.put("miles","5");
                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(timeout,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);
        }
    }


}
