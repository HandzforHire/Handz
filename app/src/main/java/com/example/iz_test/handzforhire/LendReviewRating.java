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
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LendReviewRating extends Activity {

    String image,id,date,profile_image,profilename,average_rating;
    private static final String URL = Constant.SERVER_URL+"review_rating";
    ArrayList<HashMap<String, String>> job_list = new ArrayList<HashMap<String, String>>();
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    public static String TYPE = "type";
    String usertype = "employee";
    int timeout = 60000;
    ProgressDialog progress_dialog;
    ListView list;
    Button close;
    Dialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_rating);

        /*progress_dialog = new ProgressDialog(this);
        progress_dialog.setMessage("Loading.Please wait....");
        progress_dialog.show();*/

        dialog = new Dialog(LendReviewRating.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        list = (ListView) findViewById(R.id.listview);
        close = (Button) findViewById(R.id.cancel_btn);
        ImageView image = (ImageView)findViewById(R.id.profile_image);
        TextView name = (TextView) findViewById(R.id.t2);

        Intent i = getIntent();
        id = i.getStringExtra("userId");
        profile_image = i.getStringExtra("image");
        profilename = i.getStringExtra("name");
        System.out.println("iiiiiiiiiiiiiiiiiiiii:"+id);
        completerating();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        name.setText(profilename);
        if(profile_image.equals(""))
        {

        }
        else {

            Glide.with(LendReviewRating.this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(LendReviewRating.this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image);

        }


    }

    public void completerating() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:job_history::" + response);
                        onResponserecieved(response, 1);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        try {
                            String responseBody = new String( error.networkResponse.data, "utf-8" );
                            JSONObject jsonObject = new JSONObject( responseBody );
                            System.out.println("error"+jsonObject);
                            String status = jsonObject.getString("msg");
                            if(status.equals("This User Currently Does Not Have Any Ratings"))
                            {
                                // custom dialog
                                final Dialog dialog = new Dialog(LendReviewRating.this);
                                dialog.setContentView(R.layout.custom_dialog);

                                // set the custom dialog components - text, image and button
                                TextView text = (TextView) dialog.findViewById(R.id.text);
                                text.setText("This User Currently Does Not Have Any Ratings");
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
                params.put(TYPE, usertype);
                return params;
            }
        };
        System.out.println("vvvvvvv"+ value+"."+id+"."+ usertype);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved(String jsonobject, int i) {
        System.out.println("response from interface"+jsonobject);

        String status = null;
        String rating_list = null;

        try {
            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            rating_list = jResult.getString("rating_lists");
            System.out.println("jjjjjjjjjjjjjjjob:::rating_list:::" + rating_list);
            if(status.equals("success"))
            {
                JSONArray array = new JSONArray(rating_list);
                for(int n = 0; n < array.length(); n++) {
                    JSONObject object = (JSONObject) array.get(n);
                    final String employee = object.getString("employee");
                    System.out.println("ressss:employee::" + employee);
                    date = object.getString("job_date");
                    System.out.println("ressss:date:::"+date);
                    JSONObject object1 = new JSONObject(employee);
                    for(int a = 0; a < object1.length(); a++) {
                        image = object1.getString("profile_image");
                        System.out.println("ressss:profile_image:::"+image);
                        average_rating = object1.getString("average_rating");
                        System.out.println("ressss:average_rating:::"+average_rating);
                    }
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("image",image);
                    map.put("average",average_rating);
                    map.put("date",date);
                    job_list.add(map);
                    System.out.println("job_list:::" + job_list);
                    ReviewAdapter arrayAdapter = new ReviewAdapter(this, job_list) {
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

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            view.setSelected(true);

                        }
                    });
                }

            }
            else
            {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
