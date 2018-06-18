package com.example.iz_test.handzforhire;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ApplyJob extends Activity{

    private static final String JOB_URL = Constant.SERVER_URL+"apply_jobs";
    public static String APP_KEY = "X-APP-KEY";
    public static String JOB_ID = "job_id";
    public static String EMPLOYER_ID = "employer_id";
    public static String EMPLOYEE_ID = "employee_id";
    public static String COMMENTS = "comments";
    public static String USER_TYPE = "user_type";
    String value = "HandzForHire@~";
    String job_id,user_id,employer_id,job_name,profile_name,image,date,start_time,end_time,amount,type,comments;
    TextView name,dat,amt,pay,text,job;
    ProgressDialog progress_dialog;
    ImageView default_image,profile_image;
    EditText com;
    RelativeLayout rating_lay;
    String usertype = "employee";
    int timeout = 60000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_job);

        progress_dialog = new ProgressDialog(this);
        progress_dialog.setMessage("Loading.Please wait....");
        progress_dialog.show();

        TextView apply = (TextView) findViewById(R.id.apply);
        name = (TextView) findViewById(R.id.text1);
        dat = (TextView) findViewById(R.id.tv2);
        amt = (TextView) findViewById(R.id.tv4);
        pay = (TextView) findViewById(R.id.tv5);
        text = (TextView) findViewById(R.id.tv7);
        job = (TextView) findViewById(R.id.tv1);
        com = (EditText) findViewById(R.id.edit);
   default_image = (ImageView) findViewById(R.id.default_image);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        rating_lay = (RelativeLayout) findViewById(R.id.rating);

        Intent i = getIntent();
        job_id = i.getStringExtra("jobId");
        user_id = i.getStringExtra("userId");
        employer_id = i.getStringExtra("employerId");
        job_name = i.getStringExtra("job_name");
        date = i.getStringExtra("date");
        start_time = i.getStringExtra("start_time");
        end_time = i.getStringExtra("end_time");
        profile_name = i.getStringExtra("profile_name");
        amount = i.getStringExtra("amount");
        type = i.getStringExtra("type");
        image = i.getStringExtra("image");
        System.out.println("hhhhhhhhhhhhh:apply:::"+job_name+date+start_time+end_time+profile_name+amount+type+image);
        System.out.println("hhhhhhhhhhhhh:image:::"+image);

        name.setText(profile_name);
        dat.setText(date);
        amt.setText(amount);
        pay.setText(type);
        text.setText(profile_name);
        job.setText(job_name);

        if(image.equals(""))
        {
            default_image.setVisibility(View.VISIBLE);
            progress_dialog.dismiss();
            System.out.println("iiiiiiiiiiiiiiiiiiid:get_image22::" + image);
        }
        else {
            URL url = null;
            try {
                url = new URL(image);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap bmp = null;
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            default_image.setVisibility(View.INVISIBLE);
            profile_image.setImageBitmap(bmp);
            progress_dialog.dismiss();
        }


        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comments = com.getText().toString().trim();
                applyJob();
            }
        });

        rating_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ApplyJob.this,ReviewRating.class);
                i.putExtra("userId", user_id);
                i.putExtra("image",image);
                i.putExtra("name", profile_name);
                startActivity(i);
            }
        });
    }

    public void applyJob()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JOB_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("reeeeeeeeeeeeeeeee:apply_job:::" + response);
                        onResponserecieved1(response, 2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            System.out.println("error" + jsonObject);
                            String status = jsonObject.getString("msg");
                            if(status.equals("You are not allowed to apply for the job")) {
                                // custom dialog
                                final Dialog dialog = new Dialog(ApplyJob.this);
                                dialog.setContentView(R.layout.custom_dialog);

                                // set the custom dialog components - text, image and button
                                TextView text = (TextView) dialog.findViewById(R.id.text);
                                text.setText("You are not allowed to apply for the job");
                                Button dialogButton = (Button) dialog.findViewById(R.id.ok);
                                // if button is clicked, close the custom dialog
                                dialogButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                                dialog.show();
                                Window window = dialog.getWindow();
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                            }
                        } catch (JSONException e) {

                        } catch (UnsupportedEncodingException error1) {

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(APP_KEY, value);
                params.put(JOB_ID, job_id);
                params.put(USER_TYPE, usertype);
                params.put(EMPLOYEE_ID, user_id);
                params.put(EMPLOYER_ID, employer_id);
                params.put(COMMENTS, comments);
                return params;
            }
        };

        System.out.println("values::"+job_id+".."+usertype+".."+employer_id+"..."+comments+".."+user_id);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved1(String jsonobject, int requesttype) {
        System.out.println("response from interface"+jsonobject);

        String status = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");

            if(status.equals("success"))
            {
                // custom dialog
                final Dialog dialog = new Dialog(ApplyJob.this);
                dialog.setContentView(R.layout.gray_custom);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Job Applied Successfully");
                Button dialogButton = (Button) dialog.findViewById(R.id.ok);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent i = new Intent(ApplyJob.this,LendProfilePage.class);
                        i.putExtra("userId",user_id);
                        startActivity(i);
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }
            else
            {
                // custom dialog
                final Dialog dialog = new Dialog(ApplyJob.this);
                dialog.setContentView(R.layout.custom_dialog);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Job Applied Failed.Please try again....");
                Button dialogButton = (Button) dialog.findViewById(R.id.ok);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent i = new Intent(ApplyJob.this,LendProfilePage.class);
                        i.putExtra("userId",user_id);
                        startActivity(i);
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
