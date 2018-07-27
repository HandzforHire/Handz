package com.example.iz_test.handzforhire;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Map;

public class MakePayment extends Activity{

    String job_id,user_id,job_name;
    private static final String URL = Constant.SERVER_URL+"applied_job_detailed_view";
    private static final String CANCEL_URL = Constant.SERVER_URL+"job_canceled";
    public static String EMPLOYER_ID = "employer_id";
    public static String EMPLOYEE_ID = "employee_id";
    public static String USER_TYPE = "user_type";
    public static String STATUS = "status";
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String JOB_ID = "job_id";
    String value = "HandzForHire@~";
    ImageView image;
    ProgressDialog progress_dialog;
    TextView name,job_cancel;
    Button pay_employee;

    String employee,profile_image,profile_name,user_name,employerId,employeeId;
    Dialog dialog;
    Swipe swipe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_payment);


        dialog = new Dialog(MakePayment.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        ImageView logo = (ImageView) findViewById(R.id.logo);
        image = (ImageView) findViewById(R.id.imageView);
        name = (TextView) findViewById(R.id.text);
        pay_employee = (Button) findViewById(R.id.pe);
       // payment_already = (Button) findViewById(R.id.cpa);
        job_cancel = (TextView) findViewById(R.id.jwc);

        Intent i = getIntent();
        job_id = i.getStringExtra("job_id");
        user_id = i.getStringExtra("userId");
        job_name = i.getStringExtra("job_name");
        profile_image = i.getStringExtra("image");
        profile_name = i.getStringExtra("profilename");
        user_name = i.getStringExtra("username");
        employerId=i.getStringExtra("employer");
        employeeId=i.getStringExtra("employee");


        //getJobDetails();

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MakePayment.this,ProfilePage.class);
                i.putExtra("userId",user_id);
                startActivity(i);
            }
        });

        pay_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MakePayment.this,PayEmployee.class);
                i.putExtra("jobId",job_id);
                i.putExtra("employerId",user_id);
                i.putExtra("employeeId",employee);
                i.putExtra("jobname",job_name);
                i.putExtra("image",profile_image);
                startActivity(i);
            }
        });
        job_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showalert();
            }
        });

        if(profile_image==null) {

        }
        else {
            Glide.with(this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image);
        }
        if(profile_name==null)
        {
            name.setText(user_name);

        }
        else {
            name.setText(profile_name);

        }

        swipe = new Swipe();
        swipe.setListener(new SimpleSwipeListener() {

            @Override
            public boolean onSwipedLeft(MotionEvent event) {
                Intent i = new Intent(MakePayment.this,ProfilePage.class);
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
                Intent j = new Intent(MakePayment.this, SwitchingSide.class);
                startActivity(j);
                finish();
                return super.onSwipedRight(event);
            }
        });

    }

    public void getJobDetails() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ggggggggget:profile:" + response);
                        onResponserecieved1(response, 2);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        //Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(XAPP_KEY, value);
                map.put(EMPLOYER_ID, user_id);
                map.put(JOB_ID, job_id);
                return map;
            }
        };

        System.out.println("vvvvvvv4:"+".."+value+".."+user_id+".."+job_id);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void cancelJob() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, CANCEL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ggggggggget:profile:" + response);
                        onResponserecieved1(response, 3);

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
                map.put(JOB_ID, job_id);
                map.put(EMPLOYER_ID, employeeId);
                map.put(EMPLOYEE_ID, employerId);
                map.put(USER_TYPE, "employer");
                map.put(STATUS, "job_canceled");

                return map;
            }
        };

        System.out.println("vvvvvvv4:"+".."+value+".."+user_id+".."+job_id);

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
            System.out.println("jjjjjjjjjjjjjjjob:::emp_data:::" + emp_data);
            if (status.equals("success")) {
                if(i==3){
                    Intent main = new Intent(MakePayment.this,ProfilePage.class);
                    startActivity(main);
                    finish();
                }else {
                    emp_data = jResult.getString("emp_data");
                    JSONArray array = new JSONArray(emp_data);
                    for (int n = 0; n < array.length(); n++) {
                        JSONObject object = (JSONObject) array.get(n);
                        final String username = object.getString("username");
                        profile_image = object.getString("profile_image");
                        employee = object.getString("employee_id");
                        final String profilename = object.getString("profile_name");

                        if (profile_image.equals("")) {

                        } else {
                            Glide.with(this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image);
                        }
                        if (profilename.equals("null")) {
                            name.setText(username);
                        } else {
                            name.setText(profilename);
                        }

                    }
                }
            } else {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } /*catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

*/
    }


    protected Bitmap addBorderToBitmap(Bitmap srcBitmap, int borderWidth, int borderColor) {
        // Initialize a new Bitmap to make it bordered bitmap
        Bitmap dstBitmap = Bitmap.createBitmap(
                srcBitmap.getWidth() + borderWidth * 2, // Width
                srcBitmap.getHeight() + borderWidth * 2, // Height
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
        canvas.drawRect(rect, paint);
        canvas.drawBitmap(srcBitmap, borderWidth, borderWidth, null);
        srcBitmap.recycle();

        // Return the bordered circular bitmap
        return dstBitmap;
    }

    public  void showalert()
    {
        final Dialog dialog = new Dialog(MakePayment.this);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.job_cancel);


        // set values for custom dialog components - text, image and button
        TextView txt_yes = (TextView) dialog.findViewById(R.id.txt_yes);
        TextView txt_no = (TextView) dialog.findViewById(R.id.txt_no);

        txt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelJob();
                dialog.dismiss();
            }
        });
        txt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public boolean dispatchTouchEvent(MotionEvent event){

        swipe.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

}
