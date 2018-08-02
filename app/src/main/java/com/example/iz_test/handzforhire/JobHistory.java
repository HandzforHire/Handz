package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JobHistory extends Activity {

        private static final String URL = Constant.SERVER_URL+"job_history_listing";
        ArrayList<HashMap<String, String>> job_list = new ArrayList<HashMap<String, String>>();
        ImageView logo;
        public static String KEY_USERID = "user_id";
        public static String XAPP_KEY = "X-APP-KEY";
        public static String TYPE = "type";
        String value = "HandzForHire@~";
        String address,city,state,zipcode,user_id,job_id;
        TextView profile_name;
        Button posted_job,active_job;
        ListView list;
        ProgressDialog progress_dialog;
        String usertype = "employer";
    int timeout = 60000;
    EditText editsearch;
    ArrayList<WorldPopulation> arraylist = new ArrayList<WorldPopulation>();
    String rating_value,rating_id,category1,category2,category3,category4,category5;
    Adapter adapter;
    Dialog dialog;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.job_history);

            /*progress_dialog = new ProgressDialog(this);
            progress_dialog.setMessage("Loading.Please wait....");
            progress_dialog.show();*/

            dialog = new Dialog(JobHistory.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progressbar);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


            posted_job = (Button) findViewById(R.id.btn1);
            active_job = (Button)findViewById(R.id.btn2);
            logo = (ImageView)findViewById(R.id.logo);
            list = (ListView) findViewById(R.id.listview);
            editsearch = (EditText) findViewById(R.id.search);

            Intent i = getIntent();
            user_id = i.getStringExtra("userId");
            address = i.getStringExtra("address");
            city = i.getStringExtra("city");
            state = i.getStringExtra("state");
            zipcode = i.getStringExtra("zipcode");

            activeJobs();
            adapter = new Adapter(this, arraylist);

            editsearch.addTextChangedListener(new TextWatcher() {
                private List<WorldPopulation> worldpopulationlist =  new ArrayList<WorldPopulation>();
                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                    String charText = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                    adapter.getFilter().filter(charText);
                  //
                 //   adapter.notifyDataSetChanged();
                    //JobHistory.this.
                    System.out.println("array list "+arraylist.size());
                 /*   if (charText.length() == 0) {
                        worldpopulationlist.addAll(arraylist);
                    }
                    else
                    {
                        for (WorldPopulation wp : arraylist)
                        {

                            System.out.println("charText "+charText);
                            if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)||wp.getTransaction_date().toLowerCase(Locale.getDefault()).contains(charText)||wp.getProfilename().toLowerCase(Locale.getDefault()).contains(charText)||wp.getJob_category().toLowerCase(Locale.getDefault()).contains(charText)||wp.getUsername().toLowerCase(Locale.getDefault()).contains(charText)||wp.getDescription().toLowerCase(Locale.getDefault()).contains(charText))
                            {
                                System.out.println("job name "+wp.getName());
                                worldpopulationlist.add(wp);
                            }
                        }
                    }
                    adapter = new Adapter(getApplicationContext(), worldpopulationlist);
                    list.setAdapter(adapter);*/
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                    // TODO Auto-generated method stub
                }
            });

       logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(JobHistory.this,ProfilePage.class);
                    i.putExtra("userId", user_id);
                    i.putExtra("address", address);
                    i.putExtra("city", city);
                    i.putExtra("state", state);
                    i.putExtra("zipcode", zipcode);
                    startActivity(i);
                    finish();
                }
            });

            posted_job.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(JobHistory.this,PostedJobs.class);
                    i.putExtra("userId", user_id);
                    i.putExtra("address", address);
                    i.putExtra("city", city);
                    i.putExtra("state", state);
                    i.putExtra("zipcode", zipcode);
                    startActivity(i);
                }
            });

            active_job.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(JobHistory.this,ActiveJobs.class);
                    i.putExtra("userId", user_id);
                    i.putExtra("address", address);
                    i.putExtra("city", city);
                    i.putExtra("state", state);
                    i.putExtra("zipcode", zipcode);
                    startActivity(i);
                }
            });


        }

        public void activeJobs() {
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
                                if(status.equals("No Jobs Found"))
                                {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(JobHistory.this);
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
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                    params.put(KEY_USERID, user_id);
                    params.put(TYPE, usertype);
                    return params;
                }
            };
            System.out.println("vvvvvvv"+ value+"."+user_id+"."+ usertype);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }

        public void onResponserecieved(String jsonobject, int i) {
            System.out.println("response from interface"+jsonobject);

            String status = null;
            String jobList = null;

            try {
                JSONObject jResult = new JSONObject(jsonobject);
                status = jResult.getString("status");
                jobList = jResult.getString("job_lists");

                if(status.equals("success"))
                {
                    JSONArray array = new JSONArray(jobList);
                    for(int n = 0; n < array.length(); n++) {
                        JSONObject object = (JSONObject) array.get(n);
                        final String job_name = object.getString("job_name");
                        final String image = object.getString("profile_image");
                        final String profilename = object.getString("profile_name");
                        final String username = object.getString("username");
                        final String jobId = object.getString("job_id");
                        final String employerId = object.getString("employer_id");
                        final String employeeId = object.getString("employee_id");
                        final String channelid=object.getString("channel");
                        final String tran_date=object.getString("transaction_date");
                        final String job_category=object.getString("job_category");
                        final String description=object.getString("description");
                        final String msg_notification =object.getString("employer_notificationCountMsgJobhistory");
                        final String star_notification =object.getString("employer_notificationCountStarRating");


                        String rating=object.getString("rating");

                        if(rating.equals("null"))
                        {
                            rating_value = "";
                            rating_id = "";
                            category1 = "";
                            category2 = "";
                            category3 = "";
                            category4 = "";
                            category5 = "";
                        }
                        else
                        {
                            JSONObject Result = new JSONObject(rating);
                            rating_value = Result.getString("rating");
                            rating_id = Result.getString("id");
                            category1 = Result.getString("category1");
                            category2 = Result.getString("category2");
                            category3 = Result.getString("category3");
                            category4 = Result.getString("category4");
                            category5 = Result.getString("category5");
                        }

                        WorldPopulation wp = new WorldPopulation(job_name,image,profilename,username,jobId,employerId,employeeId,channelid,user_id,rating_id,rating_value,category1,category2,category3,category4,category5,tran_date,job_category,description,msg_notification,star_notification);
                        // Binds all strings into an array
                        arraylist.add(wp);

                    }

                    adapter = new Adapter(this, arraylist);


                    // DataBind ListView with items from ArrayAdapter
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            view.setSelected(true);
                            job_id = ((TextView) view.findViewById(R.id.job_id)).getText().toString();
                            System.out.println("ssssssssssselected:item:" + job_id);
                        }
                    });

                }
                else
                {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



}
