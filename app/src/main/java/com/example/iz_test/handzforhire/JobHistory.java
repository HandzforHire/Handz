package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
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
    Adapter adapter;
    Dialog dialog;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.job_history);

            /*progress_dialog = new ProgressDialog(this);
            progress_dialog.setMessage("Loading.Please wait....");
            progress_dialog.show();*/

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
            System.out.println("iiiiiiiiiiiiiiiiiiiii:"+user_id);

            activeJobs();

            editsearch.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                    String text = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                    adapter.filter(text);
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

        public void activeJobs()
        {

            dialog = new Dialog(JobHistory.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progressbar);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
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
                System.out.println("jjjjjjjjjjjjjjjob:::list:::" + jobList);
                if(status.equals("success"))
                {
                    JSONArray array = new JSONArray(jobList);
                    for(int n = 0; n < array.length(); n++) {
                        JSONObject object = (JSONObject) array.get(n);
                        final String job_name = object.getString("job_name");
                        System.out.println("ressss:job_name::" + job_name);
                        final String image = object.getString("profile_image");
                        System.out.println("ressss:profile_image:::"+image);
                        final String profilename = object.getString("profile_name");
                        System.out.println("ressss:profilename:::"+profilename);
                        final String username = object.getString("username");
                        System.out.println("ressss:username:::"+username);
                        final String jobId = object.getString("job_id");
                        System.out.println("ressss:jobId:::"+jobId);
                        final String employerId = object.getString("employer_id");
                        System.out.println("ressss:employerId:::"+employerId);
                        final String employeeId = object.getString("employee_id");
                        System.out.println("ressss:employeeId:::"+employeeId);
                        final String channelid=object.getString("channel");
                        System.out.println("resss:channel_id::"+channelid);

                        WorldPopulation wp = new WorldPopulation(job_name,image,profilename,username,jobId,employerId,employeeId,channelid,user_id);
                        // Binds all strings into an array
                        arraylist.add(wp);
                        System.out.println("aaaaaaaaarraylist:::" + arraylist);
                        System.out.println("wwwwwwwwwwwwwwwwppp:::" + wp);


                       /* HashMap<String, String> map = new HashMap<String, String>();
                        map.put("name",job_name);
                        map.put("image",image);
                        map.put("profile",profilename);
                        map.put("user",username);
                        map.put("jobId",jobId);
                        map.put("employer",employerId);
                        map.put("employee",employeeId);
                        map.put("channel",channelid);
                        map.put("user_id",user_id);
                        job_list.add(map);
                        System.out.println("job_list:::" + job_list);*/

                    }

                    adapter = new Adapter(this, arraylist) {
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
