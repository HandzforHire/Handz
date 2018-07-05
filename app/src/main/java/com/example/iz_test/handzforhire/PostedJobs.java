package com.example.iz_test.handzforhire;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
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
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

        dialog = new Dialog(PostedJobs.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


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
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onResponserecieved1(response, 2);
                        dialog.dismiss();
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

                        } catch ( JSONException e ) {
                            //Handle a malformed json response
                            System.out.println("volley error ::"+e.getMessage());
                        } catch (UnsupportedEncodingException errors){
                            System.out.println("volley error ::"+errors.getMessage());
                        }
                        dialog.dismiss();
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
                job_list.clear();
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
                   /* ViewListAdapter adapter = new ViewListAdapter(this, job_list);
                    list.setAdapter(adapter);*/
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
            /*    profile.setVisibility(View.GONE);
                Glide.with(this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image);
*/
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } /*catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }



    public class ViewListAdapter extends BaseAdapter {

        private Activity activity;
        String dlist,job_id;
        String value = "HandzForHire@~";
        private ArrayList<HashMap<String, String>> data;
        private  LayoutInflater inflater = null;
        public ViewListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
            activity = a;
            data = d;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return data.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (convertView == null)
                vi = inflater.inflate(R.layout.view_listview, null);

            TextView job_name = (TextView) vi.findViewById(R.id.text1);
            TextView when = (TextView) vi.findViewById(R.id.text2);
            TextView pay = (TextView) vi.findViewById(R.id.text6);
            TextView date = (TextView) vi.findViewById(R.id.text3);
            TextView amount = (TextView) vi.findViewById(R.id.text7);
            TextView type = (TextView) vi.findViewById(R.id.text8);
            final TextView jobId = (TextView) vi.findViewById(R.id.job_id);
            final TextView applicants = (TextView) vi.findViewById(R.id.no_applicants);
            ImageView checked=(ImageView)vi.findViewById(R.id.img);
            ImageView unchecked=(ImageView)vi.findViewById(R.id.img1);


            String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
            Typeface font = Typeface.createFromAsset(activity.getAssets(), fontPath);
            String fontPath1 = "fonts/calibri.ttf";
            Typeface font1 = Typeface.createFromAsset(activity.getAssets(), fontPath1);

            HashMap<String, String> items = new HashMap<String, String>();
            items = data.get(position);
            final String get_name = items.get("name");
            final String get_date = items.get("date");
            String get_amount = items.get("amount");
            String get_type = items.get("type");
            job_id = items.get("jobId");
            final String get_applicants = items.get("no_of_applicants");
            final String user_id = items.get("userId");
            final String address = items.get("address");
            final String city = items.get("city");
            final String state = items.get("state");
            final String zipcode = items.get("zipcode");
            dlist= items.get("d_list");
            System.out.println("iiiiiidlist::"+dlist);



            checked.setTag(position);
            unchecked.setTag(position);

            if (dlist.equals("no"))
            {
                unchecked.setVisibility(View.VISIBLE);
                checked.setVisibility(View.INVISIBLE);


            }else
            {
                checked.setVisibility(View.VISIBLE);
                unchecked.setVisibility(View.INVISIBLE);
            }

            unchecked.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos= (int) v.getTag();
                    dlist="yes";
                    HashMap<String, String> items = new HashMap<String, String>();
                    items = data.get(pos);
                    job_id=items.get("jobId");
                    check();

                }
            });

            checked.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos= (int) v.getTag();
                    dlist="no";
                    HashMap<String, String> items = new HashMap<String, String>();
                    items = data.get(pos);
                    job_id=items.get("jobId");
                    check();

                }
            });
            DateFormat dateInstance = SimpleDateFormat.getDateInstance();
            DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat destDf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
            try {
                java.util.Date dates = srcDf.parse(get_date);
                date.setText("" + destDf.format(dates));

            } catch (Exception e)
            {
                System.out.println("error " + e.getMessage());
            }


            if(get_applicants.equals("0"))
            {
                applicants.setVisibility(View.INVISIBLE);
            }
            else
            {
                applicants.setText(get_applicants);
            }

            job_name.setText(get_name);
            job_name.setTypeface(font);
            when.setTypeface(font);
            pay.setTypeface(font);
            //date.setText(get_date);
            //date.setTypeface(font1);
            amount.setText(get_amount);
            amount.setTypeface(font1);
            type.setText(get_type);
            type.setTypeface(font1);
            jobId.setText(job_id);

            applicants.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    if (get_applicants.equals("0")) {
                        final Dialog dialog = new Dialog(activity);
                        dialog.setContentView(R.layout.custom_dialog);

                        // set the custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.text);
                        text.setText("No Job Applied");
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
                        return;
                    } else {
                        Intent i = new Intent(activity, ViewApplicant.class);
                        i.putExtra("jobId", job_id);
                        i.putExtra("userId",user_id);
                        i.putExtra("address", address);
                        i.putExtra("city",city);
                        i.putExtra("zipcode", zipcode);
                        i.putExtra("state",state);
                        i.putExtra("jobname",get_name);
                        v.getContext().startActivity(i);
                    }
                }
            });

            return vi;
        }

        private void check()
        {
            final String url = Constant.SERVER_URL+"remove_job?X-APP-KEY="+value+"&delist="+dlist+"&job_id="+job_id;

            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response)
                        {

                            Log.d("Response", response.toString());
                            System.out.println("resssssssssssssssss:dlist::service:::" + response);
                            onResponserecieved1(response, 1);
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {

                        }
                    }
            );


            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            requestQueue.add(getRequest);
        }

        public void onResponserecieved1(JSONObject jsonobject, int i) {
            String status = null;

            try {

                JSONObject jResult = new JSONObject(String.valueOf(jsonobject));

                status = jResult.getString("status");

                if(status.equals("success"))
                {
                  type="posted";
                  listPostedJobs();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }



}
