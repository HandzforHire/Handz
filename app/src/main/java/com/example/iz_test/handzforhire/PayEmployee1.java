package com.example.iz_test.handzforhire;

import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PayEmployee1 extends Activity{

    ImageView logo,image;
    Button submit_payment;
    EditText payment_amount,tip,payment_method;
    String job_id,employer_id,employee_id,job_name,profile_image;
    ProgressDialog progress_dialog;
    private static final String URL = Constant.SERVER_URL+"applied_job_detailed_view";
    private static final String URL1 = "http://162.144.41.156/~izaapinn/handzforhire/service/payment_service";
    public static String EMPLOYER_ID = "employer_id";
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String EMPLOYEE_ID = "employee_id";
    public static String TIP = "tip";
    public static String PAYMENT_METHOD = "payment_method";
    public static String PAYMENT_AMOUNT = "payment_amount";
    public static String TOTAL_PAYMENT = "total_payment";
    public static String DATE = "transaction_date";
    public static String JOB_NAME = "job_name";
    public static String JOB_ID = "job_id";
    String value = "HandzForHire@~";
    TextView name,date,total;
    String get_tip,get_amount,get_method,get_date,get_total,tip_value,payment_method_value,payment_amount_value,transaction_date_value;
    Integer total_value;
    Swipe swipe;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_employee1);

       /* progress_dialog = new ProgressDialog(this);
        progress_dialog.setMessage("Loading.Please wait....");
        progress_dialog.show();

        logo = (ImageView)findViewById(R.id.logo);
        image = (ImageView)findViewById(R.id.imageView);
        payment_amount = (EditText) findViewById(R.id.pay_amount);
        tip = (EditText) findViewById(R.id.tip);
        payment_method = (EditText) findViewById(R.id.pay_method);
        submit_payment = (Button) findViewById(R.id.submit);
        name = (TextView) findViewById(R.id.text);
        date = (TextView) findViewById(R.id.txt2);
        total = (TextView) findViewById(R.id.total);
*/

        Intent i = getIntent();
        job_id = i.getStringExtra("jobId");
        employer_id = i.getStringExtra("employerId");
        employee_id = i.getStringExtra("employeeId");
        job_name = i.getStringExtra("jobname");
        tip_value = i.getStringExtra("tip");
        payment_method_value = i.getStringExtra("paymentMethod");
        payment_amount_value = i.getStringExtra("payment_amount");
        transaction_date_value = i.getStringExtra("transaction_date");

        System.out.println("hhhhhhhhhhh:payemployee::"+job_id+"..."+employee_id+".."+employer_id+".."+job_name);

        swipe = new Swipe();
        swipe.setListener(new SimpleSwipeListener() {

            @Override
            public boolean onSwipedLeft(MotionEvent event) {
                Intent i = new Intent(PayEmployee1.this,ProfilePage.class);
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
                Intent j = new Intent(PayEmployee1.this, SwitchingSide.class);
                startActivity(j);
                finish();
                return super.onSwipedRight(event);
            }
        });
    }

    public void getJobDetails() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ggggggggget:profile:" + response);
                        onResponserecieved1(response, 2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(XAPP_KEY, value);
                map.put(EMPLOYER_ID, employer_id);
                map.put(JOB_ID, job_id);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved1(String jsonobject, int i) {
        System.out.println("response from interface" + jsonobject);

        String status = null;
        String emp_data = null;

        try {
            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            emp_data = jResult.getString("emp_data");
            System.out.println("jjjjjjjjjjjjjjjob:::emp_data:::" + emp_data);
            if (status.equals("success")) {
                JSONArray array = new JSONArray(emp_data);
                for (int n = 0; n < array.length(); n++) {
                    JSONObject object = (JSONObject) array.get(n);
                    final String username = object.getString("username");
                    profile_image = object.getString("profile_image");
                    final String profilename = object.getString("profile_name");

                    System.out.println("ressss:username::" + username);
                    System.out.println("ressss:profile_image::" + profile_image);
                    System.out.println("ressss:profilename::" + profilename);

                    if(profile_image.equals("")) {
                        progress_dialog.dismiss();
                    }
                    else {

                        Glide.with(this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image);

                    }
                    if (profilename.equals("null")) {
                        name.setText(username);
                    } else {
                        name.setText(profilename);
                    }
                    progress_dialog.dismiss();
                }

            } else {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } /*catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void payment() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ggggggggget:payemployee:" + response);
                        onResponserecieved(response, 2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(XAPP_KEY, value);
                map.put(EMPLOYER_ID, employer_id);
                map.put(EMPLOYEE_ID, employee_id);
                map.put(TIP, get_tip);
                map.put(PAYMENT_METHOD, get_method);
                map.put(JOB_NAME, job_name);
                map.put(JOB_ID, job_id);
                map.put(PAYMENT_AMOUNT, get_amount);
                map.put(TOTAL_PAYMENT, get_total);
                map.put(DATE, get_date);
                return map;
            }
        };

        System.out.println("vvvvvvv:"+"XAPP_KEY:"+value+"."+"EMPLOYER_ID:"+employer_id+"."+"EMPLOYEE_ID:"+ employee_id+"."+"TIP:"+ get_tip+"."+"PAYMENT_METHOD:"+ get_method+ "."+"JOB_NAME:"+job_name+ "."+"JOB_ID:"+job_id+"."+"PAYMENT_AMOUNT:"+ get_amount + "."+"TOTAL_PAYMENT:"+get_total+ "."+"DATE:"+get_date);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved(String jsonobject, int i) {
        System.out.println("response from interface" + jsonobject);

        String status = null;

        try {
            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            if (status.equals("success")) {
                Intent intent = new Intent(PayEmployee1.this,ProfilePage.class);
                intent.putExtra("userId",employer_id);
                startActivity(intent);
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
