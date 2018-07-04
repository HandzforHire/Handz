package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewSearchJob extends Activity{

    ListView list;
    private static final String SEARCH_URL = Constant.SERVER_URL+"job_search";
    private static final String URL = Constant.SERVER_URL+"job_lists";
    ArrayList<HashMap<String, String>> job_list = new ArrayList<HashMap<String, String>>();
    public static String XAPP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    public static String KEY_SEARCHTYPE = "search_type";
    public static String RADIUS = "location";
    public static String CATEGORY = "category";
    public static String ZIPCODE = "zipcode";
    public static String USER_ID = "user_id";
    public static String TYPE = "type";
    String user_id,address,city,state,zipcode,radius,category,name,date,pay_type,amount,jobId,image,zip,alljobs;
    ImageView logo;
    ProgressDialog progress_dialog;
    String cat = "category";
    String zip_type = "zipcode";
    String display_all = "display_all";
    String cat_zip = "category,zipcode";
    String cat_loc = "category,location";
    String zip_loc = "zipcode,location";
    String type = "";
     int timeout = 60000;
    ImageView new_search,map_view;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_search_job);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

       /* progress_dialog = new ProgressDialog(this);
        progress_dialog.setMessage("Loading.Please wait....");
        progress_dialog.show();
*/

        dialog = new Dialog(ViewSearchJob.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        list = (ListView) findViewById(R.id.listview);
        logo = (ImageView) findViewById(R.id.logo);
        new_search = (ImageView) findViewById(R.id.new_search);
        map_view = (ImageView) findViewById(R.id.map_view);

        Intent i = getIntent();
        user_id = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");
        radius = i.getStringExtra("radius");
        category = i.getStringExtra("categoryId");
        zip = i.getStringExtra("zip");
        alljobs = i.getStringExtra("alljobs");
        System.out.println("11iiiiiiiiiiiiiiiiiiiii:viewsearchjob:user_id;;;" + user_id);
        System.out.println("11iiiiiiiiiiiiiiiiiiiii:viewsearchjob:radius::" + radius);
        System.out.println("11iiiiiiiiiiiiiiiiiiiii:viewsearchjob:category::" + category);
        System.out.println("11iiiiiiiiiiiiiiiiiiiii:viewsearchjob:zip::" + zip);
        System.out.println("11iiiiiiiiiiiiiiiiiiiii:viewsearchjob:alljobs::" + alljobs);

        if(!category.equals(""))
        {
            type = "category";
            searchJobList();
        }
        else if(!zip.equals(""))
        {
            type = "zipcode";
            searchJobList();
        }
        else if(!radius.equals(""))
        {
            type = "location";
            searchJobList();
        }
        else if(!category.equals("")&&!zip.equals(""))
        {
            type = "category,zipcode";
            searchJobList();
        }
        else if (!category.equals("")&&!radius.equals(""))
        {
            type = "category,location";
            searchJobList();
        }
        else if(!zip.equals("")&&!radius.equals(""))
        {
            type = "zipcode,location";
            searchJobList();
        }
        else if(!category.equals("")&&!zip.equals("")&&!radius.equals(""))
        {
            type = "display_all";
            searchJobList();
        }
        else if(!alljobs.equals(""))
        {
            type = "employee";
            joblist();
        }
       /* else if(!alljobs.equals("")&&!zip.equals(""))
        {
            type = "employee";
            joblist();
        }
        else if (!alljobs.equals("")&&!radius.equals(""))
        {
            type = "employee";
            joblist();
        }
        else if (!alljobs.equals("")&&!zip.equals("")&&!radius.equals(""))
        {
            type = "employee";
            joblist();
        }*/
        else {
            type = "employee";
            joblist();
        }

        System.out.println("vvvvvvv:type:::::"+type);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        new_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewSearchJob.this,SearchJob.class);
                i.putExtra("userId",user_id);
                i.putExtra("address",address);
                i.putExtra("city",city);
                i.putExtra("state",state);
                i.putExtra("zipcode",zipcode);
                startActivity(i);
            }
        });

        map_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewSearchJob.this,MapActivity.class);
                i.putExtra("userId",user_id);
                i.putExtra("address",address);
                i.putExtra("city",city);
                i.putExtra("state",state);
                i.putExtra("zipcode",zipcode);
                startActivity(i);
            }
        });
    }

    private void joblist() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("reeeeeeeeeeeeeeeee:search_job:joblist::" +response);
                        onResponserecieved1(response, 2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            System.out.println("volley error::: " + jsonObject);

                        } catch (JSONException e) {
                            //Handle a malformed json response
                            System.out.println("volley error ::" + e.getMessage());
                        } catch (UnsupportedEncodingException errors) {
                            System.out.println("volley error ::" + errors.getMessage());
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(USER_ID,user_id);
                params.put(TYPE,type);
                return params;
            }
        };

        System.out.println("vvvvvvv:job list:::::"+value+".."+user_id+".."+type);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void searchJobList() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEARCH_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("reeeeeeeeeeeeeeeee:search_job:::" +response);
                        onResponserecieved(response, 2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            System.out.println("volley error::: " + jsonObject);
                            String status = jsonObject.getString("msg");
                            if(status.equals("No Jobs Found"))
                            {
                                // custom dialog
                                final Dialog dialog = new Dialog(ViewSearchJob.this);
                                dialog.setContentView(R.layout.custom_dialog);

                                // set the custom dialog components - text, image and button
                                TextView text = (TextView) dialog.findViewById(R.id.text);
                                text.setText("No Active Jobs Found");
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
                        } catch (JSONException e) {
                            //Handle a malformed json response
                            System.out.println("volley error ::" + e.getMessage());
                        } catch (UnsupportedEncodingException errors) {
                            System.out.println("volley error ::" + errors.getMessage());
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(KEY_SEARCHTYPE,type);
                params.put(CATEGORY,category);
                params.put(ZIPCODE,zip);
                params.put(RADIUS,radius);
                return params;
            }
        };

        System.out.println("vvvvvvv:searchjoblist:::::"+value+".."+type+".."+category+".."+zip+".."+radius);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                        name = object.getString("job_name");
                        date = object.getString("job_date");
                        pay_type = object.getString("job_payment_type");
                        amount = object.getString("job_payment_amount");
                        jobId = object.getString("id");
                        image = object.getString("profile_image");

                        System.out.println("ressss:name::"+name);
                        System.out.println("ressss:date::"+date);
                        System.out.println("ressss:pay_type::" + pay_type);
                        System.out.println("ressss::amount:" + amount);
                        System.out.println("ressss::jobId:" + jobId);
                        System.out.println("ressss::image:" + image);
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("name", name);
                    map.put("date", date);
                    map.put("type", pay_type);
                    map.put("amount", amount);
                    map.put("jobId",jobId);
                    map.put("image",image);
                    job_list.add(map);
                    System.out.println("job_list:::" + job_list);
                    CustomList arrayAdapter = new CustomList(this, job_list){
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
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            view.setSelected(true);
                            String job_id = ((TextView) view.findViewById(R.id.job_id)).getText().toString();
                            System.out.println("ssssssssssselected:job_id:" + job_id);
                            Intent i = new Intent(ViewSearchJob.this,JobDescription.class);
                            i.putExtra("userId",user_id);
                            i.putExtra("jobId",job_id);
                            startActivity(i);
                        }
                    });
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void onResponserecieved1(String jsonobject, int i) {
        System.out.println("response from interface" + jsonobject);

        String status = null;

        try
        {
            JSONObject result = new JSONObject(jsonobject);
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
                    name = object.getString("job_name");
                    date = object.getString("job_date");
                    pay_type = object.getString("job_payment_type");
                    amount = object.getString("job_payment_amount");
                    jobId = object.getString("id");
                    image = object.getString("profile_image");

                    System.out.println("ressss:name::"+name);
                    System.out.println("ressss:date::"+date);
                    System.out.println("ressss:pay_type::" + pay_type);
                    System.out.println("ressss::amount:" + amount);
                    System.out.println("ressss::jobId:" + jobId);
                    System.out.println("ressss::image:" + image);
                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("name", name);
                    map.put("date", date);
                    map.put("type", pay_type);
                    map.put("amount", amount);
                    map.put("jobId",jobId);
                    map.put("image",image);
                    job_list.add(map);
                    System.out.println("job_list:::" + job_list);
                    CustomList arrayAdapter = new CustomList(this, job_list){
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
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            view.setSelected(true);
                            String job_id = ((TextView) view.findViewById(R.id.job_id)).getText().toString();
                            System.out.println("ssssssssssselected:job_id:" + job_id);
                            Intent i = new Intent(ViewSearchJob.this,JobDescription.class);
                            i.putExtra("userId",user_id);
                            i.putExtra("jobId",job_id);
                            startActivity(i);
                        }
                    });
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

}
