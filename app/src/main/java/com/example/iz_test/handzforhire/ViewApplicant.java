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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewApplicant extends Activity {

    private static final String GET_URL = Constant.SERVER_URL+"get_profile_image";
    private static final String URL = Constant.SERVER_URL+"applied_job_detailed_view";
    ArrayList<HashMap<String, String>> job_list = new ArrayList<HashMap<String, String>>();
    ImageView image, profile,close;
    public static String EMPLOYER_ID = "employer_id";
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String JOB_ID = "job_id";
    String value = "HandzForHire@~";
    String address, city, state, zipcode, user_id, job_id,name,username,comments,employee,profile_image,profilename;
    TextView profile_name,job_name;
    ListView list;
    ProgressDialog progress_dialog;
    RelativeLayout rating_lay;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_applicant);

        /*progress_dialog = new ProgressDialog(this);
        progress_dialog.setMessage("Loading.Please wait....");
        progress_dialog.show();*/

        image = (ImageView) findViewById(R.id.default_image);
        profile = (ImageView) findViewById(R.id.profile_image);
        profile_name = (TextView) findViewById(R.id.text1);
        job_name = (TextView) findViewById(R.id.name);
        close = (ImageView) findViewById(R.id.close_btn);
        list = (ListView) findViewById(R.id.listview);
        rating_lay = (RelativeLayout) findViewById(R.id.rating);

        Intent i = getIntent();
        user_id = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");
        job_id = i.getStringExtra("jobId");
        name = i.getStringExtra("jobname");
        System.out.println("iiiiiiiiiiiiiiiiiiiii:user_id" + user_id);
        System.out.println("iiiiiiiiiiiiiiiiiiiii:jobname::" + name);
        System.out.println("iiiiiiiiiiiiiiiiiiiii:jobid::" + job_id);

        job_name.setText(name);

        getProfileimage();
        listPostedJobs();

        rating_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewApplicant.this,ReviewRating.class);
                i.putExtra("userId", user_id);
                i.putExtra("image",profile_image);
                i.putExtra("name", profilename);
                startActivity(i);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void getProfileimage() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ggggggggget:profile:" + response);
                        onResponserecieved2(response, 2);
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
                map.put(KEY_USERID, user_id);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved2(String jsonobject, int requesttype) {
        String status = null;
         profile_image = null;
         profilename = null;
        try {

            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");

            if (status.equals("success")) {
                profile_image = jResult.getString("profile_image");
                profilename = jResult.getString("profile_name");
                System.out.println("ggggggggget:profilename:" + profilename);
                profile_name.setText(profilename);
                System.out.println("ggggggggget:profile_image:" + profile_image);
              /*  java.net.URL url = new URL(profile_image);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                bmp = addBorderToBitmap(bmp, 10, Color.BLACK);
                bmp = addBorderToBitmap(bmp, 3, Color.BLACK);
               *//* Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);*//*
                image.setVisibility(View.INVISIBLE);
                profile.setImageBitmap(bmp);*/
                //profile_name.setText(user_name);
                //Glide.with(ViewApplicant.this).load(profile_image).error(R.drawable.default_profile).into(image);
                profile.setVisibility(View.INVISIBLE);
                Glide.with(ViewApplicant.this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        } /*catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void listPostedJobs()
    {
        dialog = new Dialog(ViewApplicant.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:view applicant:::" + response);
                        onResponserecieved1(response, 2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       /* try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            System.out.println("error" + jsonObject);
                        } catch (JSONException e) {
                            //Handle a malformed json response
                        } catch (UnsupportedEncodingException error1) {

                        }*/
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(EMPLOYER_ID, user_id);
                params.put(JOB_ID, job_id);
                return params;
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
                    username = object.getString("username");
                    comments = object.getString("comments");
                    employee = object.getString("employee_id");
                    System.out.println("ressss:username::" + username);
                    System.out.println("ressss:comments::" + comments);
                    System.out.println("ressss:employee_id::" + employee);
                }

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("username", username);
                map.put("comments", comments);
                map.put("employee_id",employee);
                map.put("job_id",job_id);
                map.put("employer_id",user_id);
                map.put("jobname",name);
                job_list.add(map);
                System.out.println("job_list:::" + job_list);
                ApplicantAdapter arrayAdapter = new ApplicantAdapter(this, job_list) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        // Get the current item from ListView
                        View view = super.getView(position, convertView, parent);
                        if (position % 2 == 1) {
                            // Set a background color for ListView regular row/item
                            view.setBackgroundColor(Color.parseColor("#BF178487"));
                        } else {
                            // Set the background color for alternate row/item
                            view.setBackgroundColor(Color.parseColor("#BFE8C64B"));
                        }
                        return view;
                    }
                };

                // DataBind ListView with items from ArrayAdapter
                list.setAdapter(arrayAdapter);
                dialog.dismiss();

            } else {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
}