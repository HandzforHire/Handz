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
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PostedJobs extends Activity {

    private static final String GET_URL = Constant.SERVER_URL+"get_profile_image";
    private static final String URL = Constant.SERVER_URL+"job_lists";
    ArrayList<HashMap<String, String>> job_list = new ArrayList<HashMap<String, String>>();
    ImageView image,profile,logo;
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String TYPE = "type";
    String value = "HandzForHire@~";
    String address,city,state,zipcode,id,jobId,job_id,name,date,amount,applicants,profile_image,profilename,dlist;
    TextView profile_name;
    ListView list;
    ProgressDialog progress_dialog;
    String type = "posted";
    int timeout = 60000;
    RelativeLayout rating_lay;
    Button active_btn,history_btn;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_posted_jobs);

      /*  progress_dialog = new ProgressDialog(this);
        progress_dialog.setMessage("Loading.Please wait....");
        progress_dialog.show();*/


        dialog = new Dialog(PostedJobs.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        //image = (ImageView)findViewById(R.id.default_image);
        //profile = (ImageView)findViewById(R.id.profile_image);
        //profile_name = (TextView) findViewById(R.id.text1);
        logo = (ImageView)findViewById(R.id.logo);
        list = (ListView)findViewById(R.id.listview);
        //rating_lay = (RelativeLayout) findViewById(R.id.rating);
        active_btn = (Button) findViewById(R.id.btn1);
        history_btn = (Button) findViewById(R.id.btn2);

        Intent i = getIntent();
        id = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");
        System.out.println("iiiiiiiiiiiiiiiiiiiii:" + id);

       // getProfileimage();
        listPostedJobs();

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostedJobs.this, ProfilePage.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
                finish();
            }
        });

        /*rating_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostedJobs.this,ReviewRating.class);
                i.putExtra("userId", id);
                i.putExtra("image",profile_image);
                i.putExtra("name", profilename);
                startActivity(i);
            }
        });*/

        active_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostedJobs.this,ActiveJobs.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

        history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PostedJobs.this,JobHistory.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

    }

    public void listPostedJobs() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:view:posted:jobs" + response);
                        onResponserecieved1(response, 2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String( error.networkResponse.data, "utf-8" );
                            JSONObject jsonObject = new JSONObject( responseBody );
                            System.out.println("volley error::: "+jsonObject);
                            String status = jsonObject.getString("msg");
                            if(status.equals("No Jobs Found"))
                            {
                                // custom dialog
                                final Dialog dialog = new Dialog(PostedJobs.this);
                                dialog.setContentView(R.layout.custom_dialog);

                                // set the custom dialog components - text, image and button
                                TextView text = (TextView) dialog.findViewById(R.id.text);
                                text.setText("No Jobs Found");
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
                            else {

                                final Dialog dialog = new Dialog(PostedJobs.this);
                                dialog.setContentView(R.layout.custom_dialog);

                                // set the custom dialog components - text, image and button
                                TextView text = (TextView) dialog.findViewById(R.id.text);
                                text.setText("Login Failed.Please try again");
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
                            dialog.dismiss();
                        } catch ( JSONException e ) {
                            //Handle a malformed json response
                            System.out.println("volley error ::"+e.getMessage());
                        } catch (UnsupportedEncodingException errors){
                            System.out.println("volley error ::"+errors.getMessage());
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(KEY_USERID, id);
                params.put(TYPE,type);
                return params;
            }
        };

        System.out.println("values::"+value+".."+id+".."+type);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved1(String jsonobject, int i) {
        System.out.println("response from interface"+jsonobject);

        String status = null;
        String jobList = null;

        try {
            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            jobList = jResult.getString("job_lists");
            System.out.println("jjjjjjjjjjjjjjjob:::list:::"+jobList);
            if(status.equals("success"))
            {
                JSONArray array = new JSONArray(jobList);
                for(int n = 0; n < array.length(); n++)
                {
                    JSONObject object = (JSONObject) array.get(n);
                    name = object.getString("job_name");
                    date = object.getString("job_date");
                    type = object.getString("job_payment_type");
                    amount = object.getString("job_payment_amount");
                    applicants = object.getString("no_of_applicants_applied");
                    job_id = object.getString("job_id");
                    dlist=object.getString("delist");

                    System.out.println("ressss:name::"+name);
                    System.out.println("ressss:date::"+date);
                    System.out.println("ressss:recur::" + type);
                    System.out.println("ressss::amount:" + amount);
                    System.out.println("ressss::applicants:" + applicants);
                    System.out.println("ressss::job_id:" + job_id);

                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("name", name);
                    map.put("date", date);
                    map.put("type", type);
                    map.put("amount", amount);
                    map.put("no_of_applicants",applicants);
                    map.put("userId",id);
                    map.put("address",address);
                    map.put("city",city);
                    map.put("state",state);
                    map.put("zipcode",zipcode);
                    map.put("jobId",job_id);
                    map.put("d_list",dlist);
                    job_list.add(map);
                    System.out.println("job_list:::" + job_list);
                    ViewListAdapter adapter = new ViewListAdapter(this, job_list);
                    list.setAdapter(adapter);
                    ViewListAdapter arrayAdapter = new ViewListAdapter(this, job_list){
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent){
                            // Get the current item from ListView
                            View view = super.getView(position,convertView,parent);
                            if(position %2 == 1)
                            {
                                // Set a background color for ListView regular row/item
                                view.setBackgroundColor(Color.parseColor("#BF178487"));
                            }
                            else
                            {
                                // Set the background color for alternate row/item
                                view.setBackgroundColor(Color.parseColor("#BFE8C64B"));
                            }
                            return view;
                        }
                    };

                    // DataBind ListView with items from ArrayAdapter
                    list.setAdapter(arrayAdapter);
                    dialog.dismiss();
                }
            }
            else
            {
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getProfileimage()
    {
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
                map.put(KEY_USERID, id);
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

            if(status.equals("success"))
            {
                profile_image = jResult.getString("profile_image");
                profilename = jResult.getString("profile_name");
                System.out.println("ggggggggget:profilename:" + profilename);
                profile_name.setText(profilename);
                System.out.println("ggggggggget:profile_image:" + profile_image);
                URL url = new URL(profile_image);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                bmp = addBorderToBitmap(bmp, 10, Color.BLACK);
                bmp = addBorderToBitmap(bmp, 3, Color.BLACK);
               /* Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);*/
                image.setVisibility(View.INVISIBLE);
                profile.setImageBitmap(bmp);
                //profile_name.setText(user_name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
