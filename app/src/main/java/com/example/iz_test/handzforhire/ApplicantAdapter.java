package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApplicantAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    private static final String HIRE_URL = Constant.SERVER_URL+"hire_job";
    private static final String REFUSE_URL = Constant.SERVER_URL+"refuse_jobs";
    public static String JOB_NAME = "job_name";
    public static String EMPLOYER_ID = "employer_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String JOB_ID = "job_id";
    public static String EMPLOYEE_ID = "employee_id";
    public static String USER_TYPE = "userType";
    String value = "HandzForHire@~";
    String usertype = "employer";
    String employee_id,job_id,employer_id,job_name;


    public ApplicantAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.list_applicant, null);

        TextView name = (TextView) vi.findViewById(R.id.name);
        TextView comments = (TextView) vi.findViewById(R.id.comments);
        TextView jobname = (TextView) vi.findViewById(R.id.jobname);
        TextView hire = (TextView) vi.findViewById(R.id.hire);
        TextView refuse = (TextView) vi.findViewById(R.id.refuse);

        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiremethod();
            }
        });

        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refusemethod();
            }
        });

        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface font = Typeface.createFromAsset(activity.getAssets(), fontPath);
        String fontPath1 = "fonts/calibri.ttf";
        Typeface font1 = Typeface.createFromAsset(activity.getAssets(), fontPath1);

        HashMap<String, String> items = new HashMap<String, String>();
        items = data.get(position);
        final String get_name = items.get("username");
        System.out.println("iiiiiiiiiiiiiiiiiiid:get_name::" + get_name);
        final String get_comments = items.get("comments");
        System.out.println("iiiiiiiiiiiiiiiiiiid:get_comments::" + get_comments);
        employee_id = items.get("employee_id");
        System.out.println("iiiiiiiiiiiiiiiiiiid:employee_id::" + employee_id);
        job_id = items.get("job_id");
        System.out.println("iiiiiiiiiiiiiiiiiiid:job_id::" + job_id);
        employer_id = items.get("employer_id");
        System.out.println("iiiiiiiiiiiiiiiiiiid:employer_id::" + employer_id);
        job_name = items.get("jobname");
        System.out.println("iiiiiiiiiiiiiiiiiiid:job_name::" + job_name);

        name.setText(get_name);
        name.setTypeface(font);
        comments.setText(get_comments);
        comments.setTypeface(font);
        jobname.setText(job_name);

        return vi;
    }

    public void hiremethod()
    {
        /*StringRequest stringRequest = new StringRequest(Request.Method.POST, HIRE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:hire::service:::" + response);
                        onResponserecieved1(response, 1);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            System.out.println("error" + jsonObject);
                        } catch (JSONException e) {
                            //Handle a malformed json response
                        } catch (UnsupportedEncodingException error1) {

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(EMPLOYER_ID, employer_id);
                params.put(JOB_ID, job_id);
                params.put(EMPLOYEE_ID, employee_id);
                params.put(JOB_NAME,job_name);
                params.put(USER_TYPE,usertype);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);*/

        final String url = "http://162.144.41.156/~izaapinn/handzforhire/service/hire_job?X-APP-KEY="+value+"&employer_id="+employer_id+"&job_id="+job_id+"&employee_id="+employee_id+"&job_name="+job_name;

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        System.out.println("resssssssssssssssss:hire::service:::" + response);
                        onResponserecieved1(response, 1);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d("Error.Response", response);
                    }
                }
        );

        // add it to the RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(getRequest);
    }

    public void onResponserecieved1(JSONObject jsonobject, int i) {
        String status = null;

        try {

            JSONObject jResult = new JSONObject(String.valueOf(jsonobject));

            status = jResult.getString("status");

            if(status.equals("success"))
            {
                Intent intent=new Intent(activity,ProfilePage.class);
                intent.putExtra("userId",employer_id);
                activity.startActivity(intent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void refusemethod()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REFUSE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:refuse::service:::" + response);
                        onResponserecieved(response, 2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            System.out.println("error" + jsonObject);
                        } catch (JSONException e) {
                            //Handle a malformed json response
                        } catch (UnsupportedEncodingException error1) {

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(EMPLOYER_ID, employer_id);
                params.put(JOB_ID, job_id);
                params.put(EMPLOYEE_ID, employee_id);
                params.put(USER_TYPE,usertype);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved(String jsonobject, int i) {
        String status = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");

            if(status.equals("success"))
            {
                        Intent intent = new Intent(activity,ProfilePage.class);
                        intent.putExtra("userId",employer_id);
                        activity.startActivity(intent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}