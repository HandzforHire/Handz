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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class JobDetails extends Activity{

    ImageView profile_image, default_image,close;
    TextView profile_name, description, date, time, amount, type,name;
    private static final String URL = Constant.SERVER_URL+"job_detail_view";
    public static String APP_KEY = "X-APP-KEY";
    public static String JOB_ID = "job_id";
    public static String EMPLOYER_ID = "employer_id";
    public static String EMPLOYEE_ID = "employee_id";
    String value = "HandzForHire@~";
    String job_id,user_id,employerId;
    ProgressDialog progress_dialog;
    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_details);

       /* progress_dialog = new ProgressDialog(this);
        progress_dialog.setMessage("Loading.Please wait....");
        progress_dialog.show();*/


        profile_image = (ImageView) findViewById(R.id.profile_image);
        default_image = (ImageView) findViewById(R.id.default_image);
        profile_name = (TextView) findViewById(R.id.text1);
        description = (TextView) findViewById(R.id.description_text);
        name = (TextView) findViewById(R.id.job_name_text);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        amount = (TextView) findViewById(R.id.amount);
        type = (TextView) findViewById(R.id.type);
        close = (ImageView) findViewById(R.id.close_btn);

        Intent i = getIntent();
        job_id = i.getStringExtra("jobId");
        user_id = i.getStringExtra("userId");
        System.out.println("ssssssssssselected:job_id:" + job_id);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getJobDetails();

    }

    public void getJobDetails()
    {

        dialog = new Dialog(JobDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("reeeeeeeeeeeeeeeee:job_description:::" + response);
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
        System.out.println("response from interface" + jsonobject);

        String status = null;
        String job_data = null;

        try {
            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            if (status.equals("success")) {
                job_data = jResult.getString("job_data");
                System.out.println("jjjjjjjjjjjjjjjob:::job_data:::" + job_data);
                JSONObject object = new JSONObject(job_data);
                String get_name = object.getString("job_name");
                System.out.println("nnnnnnnnnnn:name::"+get_name);
                String get_description = object.getString("description");
                System.out.println("nnnnnnnnnnn:description::" + get_description);
                String get_date = object.getString("job_date");
                System.out.println("nnnnnnnnnnn:date::" + get_date);
                String get_start_time = object.getString("start_time");
                System.out.println("nnnnnnnnnnn:start_time::" + get_start_time);
                String get_end_time = object.getString("end_time");
                System.out.println("nnnnnnnnnnn:end_time::" + get_end_time);
                String get_amount = object.getString("job_payment_amount");
                System.out.println("nnnnnnnnnnn:amount::" + get_amount);
                String get_type = object.getString("job_payment_type");
                System.out.println("nnnnnnnnnnn:type::" + get_type);
                String get_profile_name = object.getString("profile_name");
                System.out.println("nnnnnnnnnnn:get_profile_name::" + get_profile_name);
                String image = object.getString("profile_image");
                System.out.println("ressss::image:" + image);
                employerId = object.getString("employer_id");
                System.out.println("ressss::employerId:" + employerId);
                profile_name.setText(get_profile_name);
                description.setText(get_description);


                date.setText(get_date);
                time.setText(get_start_time);
                type.setText(get_type);
                amount.setText(get_amount);
                name.setText(get_name);

                if(image.equals(""))
                {
                    dialog.dismiss();
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
                    bmp = addBorderToBitmap(bmp, 10, Color.BLACK);
                    bmp = addBorderToBitmap(bmp, 3, Color.BLACK);
                    default_image.setVisibility(View.INVISIBLE);
                    profile_image.setImageBitmap(bmp);
                    dialog.dismiss();
                }

            } else {
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected Bitmap addBorderToBitmap(Bitmap srcBitmap, int borderWidth, int borderColor){
        // Initialize a new Bitmap to make it bordered bitmap
        Bitmap dstBitmap = Bitmap.createBitmap(
                srcBitmap.getWidth() + borderWidth*2, // Width
                srcBitmap.getHeight() + borderWidth*2, // Height
                Bitmap.Config.ARGB_8888 // Config
        );
        Canvas canvas = new Canvas(dstBitmap);

        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setAntiAlias(true);
        Rect rect = new Rect(
                borderWidth / 2,
                borderWidth / 2,
                canvas.getWidth() - borderWidth / 2,
                canvas.getHeight() - borderWidth / 2
        );
        canvas.drawRect(rect,paint);
        canvas.drawBitmap(srcBitmap, borderWidth, borderWidth, null);
        srcBitmap.recycle();

        // Return the bordered circular bitmap
        return dstBitmap;
    }

}
