package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.android.volley.AuthFailureError;
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

    public class PendingJobs extends Activity {

        ListView list;
        private static final String SEARCH_URL = Constant.SERVER_URL+"job_lists";
        ArrayList<HashMap<String, String>> job_list = new ArrayList<HashMap<String, String>>();
        public static String XAPP_KEY = "X-APP-KEY";
        String value = "HandzForHire@~";
        public static String KEY_USER = "user_id";
        public static String KEY_TYPE = "type";
        String user_id,address,city,state,zipcode,cat_type,cat_id,job_cat_name,name,date,amount,jobId;

        String jobname,jobdate,pay,esti,jobstatus;
        ImageView logo;
        ProgressDialog progress_dialog;
        String type = "applied";
        Button active_jobs,job_history;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.pending_jobs);

            progress_dialog = new ProgressDialog(this);
            progress_dialog.setMessage("Loading.Please wait....");
            progress_dialog.show();

            list = (ListView) findViewById(R.id.listview);
            logo = (ImageView) findViewById(R.id.logo);
            active_jobs = (Button) findViewById(R.id.btn1);
            job_history = (Button) findViewById(R.id.btn2);

            Intent i = getIntent();
            user_id = i.getStringExtra("userId");
            address = i.getStringExtra("address");
            city = i.getStringExtra("city");
            state = i.getStringExtra("state");
            zipcode = i.getStringExtra("zipcode");
            cat_type = i.getStringExtra("type");
            cat_id = i.getStringExtra("categoryId");
            job_cat_name = i.getStringExtra("category");
            System.out.println("11iiiiiiiiiiiiiiiiiiiii:user_id;;;" + user_id);
            System.out.println("11iiiiiiiiiiiiiiiiiiiii:cat_id::" + cat_id);
            System.out.println("11iiiiiiiiiiiiiiiiiiiii:cat_type::" + cat_type);

            searchJobList();

            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            active_jobs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(PendingJobs.this,ActiveJobs.class);
                    i.putExtra("userId", user_id);
                    i.putExtra("address", address);
                    i.putExtra("city", city);
                    i.putExtra("state", state);
                    i.putExtra("zipcode", zipcode);
                    startActivity(i);
                }
            });

            job_history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(PendingJobs.this,JobHistory.class);
                    i.putExtra("userId", user_id);
                    i.putExtra("address", address);
                    i.putExtra("city", city);
                    i.putExtra("state", state);
                    i.putExtra("zipcode", zipcode);
                    startActivity(i);
                }
            });
        }

        public void searchJobList() {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, SEARCH_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("reeeeeeeeeeeeeeeee:pendingjobs:::" +response);
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
                                String status = jsonObject.getString("msg");
                                if(status.equals("No Jobs Found"))
                                {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(PendingJobs.this);
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
                               progress_dialog.dismiss();
                            }
                            catch (JSONException e)
                            {

                            } catch (UnsupportedEncodingException error1) {

                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(XAPP_KEY, value);
                    params.put(KEY_USER,user_id);
                    params.put(KEY_TYPE,type);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

        private void onResponserecieved(String response, int i) {
            String status = null;

            try
            {
                JSONObject result = new JSONObject(response);
                status = result.getString("status");
                if(status.equals("success"))
                {
                    String job = result.getString("job_lists");
                    System.out.println("jjjjjjjjjjjjjjjob:"+job);
                    JSONArray array = new JSONArray(job);
                    for(int n = 0; n < array.length(); n++)
                    {
                        JSONObject object = (JSONObject) array.get(n);
                        String category = object.getString("job_category");
                        System.out.println("ressss::category:" + category);
                        jobname = object.getString("job_name");
                        jobdate = object.getString("job_date");
                        esti = object.getString("job_payment_type");
                        pay = object.getString("job_estimated_payment");
                        jobId = object.getString("id");
                        jobstatus=object.getString("job_status");

                        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date date = (Date) inFormat.parse(jobdate);
                        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
                        String goal = outFormat.format(date);
                        System.out.println("gggg"+goal);

                        System.out.println("0000"+jobname);
                        System.out.println("0000"+jobdate);
                        System.out.println("0000" + esti);
                        System.out.println("0000" + pay);
                        System.out.println("ressss::jobId:" + jobId);
                        System.out.println("status::"+jobstatus);
                    }
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("name", jobname);
                    map.put("date", jobdate);
                    map.put("type", esti);
                    map.put("amount", pay);
                    map.put("jobId",jobId);
                    map.put("status",jobstatus);

                    job_list.add(map);
                    System.out.println("job_list:::" + job_list);
                    PendingAdapter arrayAdapter = new PendingAdapter(this, job_list){
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
                    progress_dialog.dismiss();
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                               /* view.setSelected(true);
                                String job_id = ((TextView) view.findViewById(R.id.job_id)).getText().toString();
                                System.out.println("ssssssssssselected:job_id:" + job_id);
                                Intent i = new Intent(PendingJobs.this,JobDescription.class);
                                i.putExtra("userId",user_id);
                                i.putExtra("jobId",job_id);
                                startActivity(i);*/
                        }
                    });
                }
            }catch (JSONException e){
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            {


            }
        }
}
