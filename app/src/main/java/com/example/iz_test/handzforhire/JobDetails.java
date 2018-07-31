package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.pwittchen.swipe.library.rx2.SimpleSwipeListener;
import com.github.pwittchen.swipe.library.rx2.Swipe;
import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class JobDetails extends Activity{

    ImageView profile_image,close;
    TextView profile_name, description, date, time, amount, type,name,text3;
    private static final String URL = Constant.SERVER_URL+"job_detail_view";
    public static String APP_KEY = "X-APP-KEY";
    public static String JOB_ID = "job_id";
    public static String EMPLOYER_ID = "employer_id";
    public static String EMPLOYEE_ID = "employee_id";
    String value = "HandzForHire@~";
    String job_id,user_id,employerId;
    ProgressDialog progress_dialog;
    Dialog dialog;
    Swipe swipe;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_details);

        dialog = new Dialog(JobDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        profile_image = (ImageView) findViewById(R.id.profile_image);
        profile_name = (TextView) findViewById(R.id.text1);
        description = (TextView) findViewById(R.id.description_text);
        name = (TextView) findViewById(R.id.job_name_text);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        text3=(TextView)findViewById(R.id.text3);
        amount = (TextView) findViewById(R.id.amount);
        type = (TextView) findViewById(R.id.type);
        close = (ImageView) findViewById(R.id.close_btn);

        Intent i = getIntent();
        job_id = i.getStringExtra("jobId");
        user_id = i.getStringExtra("userId");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        swipe = new Swipe();
        swipe.setListener(new SimpleSwipeListener() {

            @Override
            public boolean onSwipedLeft(MotionEvent event) {
                Intent i = new Intent(JobDetails.this,ProfilePage.class);
                i.putExtra("userId", Profilevalues.user_id);
                i.putExtra("address", Profilevalues.address);
                i.putExtra("city", Profilevalues.city);
                i.putExtra("state", Profilevalues.state);
                i.putExtra("zipcode", Profilevalues.zipcode);
                startActivity(i);
                finish();

                return super.onSwipedLeft(event);
            }


            @Override
            public boolean onSwipedRight(MotionEvent event) {
                Intent j = new Intent(JobDetails.this, SwitchingSide.class);
                startActivity(j);
                finish();
                return super.onSwipedRight(event);
            }
        });
        getJobDetails();

    }

    public void getJobDetails() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onResponserecieved(response, 2);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            System.out.println("error" + jsonObject);
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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved(String jsonobject, int i) {

        String status = null;
        String job_data = null;

        try {
            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            if (status.equals("success")) {
                job_data = jResult.getString("job_data");
                JSONObject object = new JSONObject(job_data);
                String get_name = object.getString("job_name");
                String get_description = object.getString("description");
                String get_date = object.getString("job_date");
                String get_start_time = object.getString("start_time");
                String get_end_time = object.getString("end_time");
                String get_amount = object.getString("job_payment_amount");
                String get_type = object.getString("job_payment_type");
                String get_profile_name = object.getString("profile_name");
                String image = object.getString("profile_image");

                employerId = object.getString("employer_id");
                profile_name.setText(get_profile_name);
                description.setText(get_description);

                DateFormat dateInstance = SimpleDateFormat.getDateInstance();
                DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat destDf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
                try {
                    java.util.Date dates = srcDf.parse(get_date);
                    date.setText(""+destDf.format(dates));
                }catch (Exception e){

                }

                time.setText(get_start_time);
                type.setText(get_type);
                amount.setText(get_amount);
                name.setText(get_name);

                if(image.equals(""))
                {
                }
                else {
                    Glide.with(this).load(image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(profile_image);
                }

            } else {
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public boolean dispatchTouchEvent(MotionEvent event){

        swipe.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

}
