package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.pwittchen.swipe.library.rx2.SimpleSwipeListener;
import com.github.pwittchen.swipe.library.rx2.Swipe;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditCreateJob2 extends Activity {

    String id, address, city, state, zipcode, name, category, date, start_time, end_time, amount, type, description;
    private static final String URL = Constant.SERVER_URL+"edit_job";
    private static final String GET_JOB = Constant.SERVER_URL+"job_detail_view";
    String usertype = "employer";
    public static String USER_ID = "user_id";
    public static String JOB_NAME = "job_name";
    public static String JOB_CATEGORY = "job_category";
    public static String JOB_DESCRIPTION = "job_description";
    public static String JOB_DATE = "job_date";
    public static String JOB_START_DATE = "job_start_date";
    public static String JOB_END_DATE = "job_end_date";
    public static String START_TIME = "start_time";
    public static String END_TIME = "end_time";
    public static String JOB_PAYMENT_AMOUNT = "job_payment_amount";
    public static String JOB_PAYMENT_TYPE = "job_payment_type";
    public static String ADDRESS = "address";
    public static String CITY = "city";
    public static String STATE = "state";
    public static String ZIPCODE = "zipcode";
    public static String POST_ADDRESS = "post_address";
    public static String APP_KEY = "X-APP-KEY";
    public static String LATITUDE = "lat";
    public static String LONGITUDE = "lon";
    public static String CURRENT_LOCATION = "currentlocation";
    public static String POCKET_EXPENSE = "out_of_pocket_expense";
    public static String USER_TYPE = "user_type";
    public static String JOB_STATE = "job_state";
    public static String ESTIMATED_PAYMENT = "job_estimated_payment";
    public static String FLEXIBLE = "job_date_time_flexible";
    public static String JOB_ZIPCODE = "job_zipcode";
    public static String JOB_ADDRESS = "job_address";
    public static String JOB_CITY = "job_city";
    public static String JOB_ID = "job_id";
    String value = "HandzForHire@~";
    TextView text, pros, cons;
    EditText add, cit, stat, zip;
    String pocket = "100";
    String current_location;
    CheckBox check1, check2;
    Button post;
    String j_address, j_city, j_state, j_zipcode, post_address, job_id,jobId,getAddress,getCity,getState,getZipcode;
    RelativeLayout relative;
    LinearLayout linear, layout;
    ImageView logo;
    static String get_lat;
    static String get_lon;
    double lat,lon;
    static String latitude;
    static String longitude;
    String estimated_amount,flexible_status;
    LocationTrack locationTrack;
    String new_value = "0.0";
    Dialog dialog;
    Swipe swipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_create_job2);

        dialog = new Dialog(EditCreateJob2.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        Intent i = getIntent();
        id = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");
        name = i.getStringExtra("name");
        category = i.getStringExtra("category");
        description = i.getStringExtra("description");
        jobId = i.getStringExtra("job_id");
        date = i.getStringExtra("date");
        start_time = i.getStringExtra("start_time");
        end_time = i.getStringExtra("end_time");
        amount = i.getStringExtra("payment_amount");
        type = i.getStringExtra("payment_type");
        estimated_amount = i.getStringExtra("estimated_amount");
        flexible_status = i.getStringExtra("flexible_status");

        text = (TextView) findViewById(R.id.bt1);
        add = (EditText) findViewById(R.id.address);
        cit = (EditText) findViewById(R.id.city);
        stat = (EditText) findViewById(R.id.state);
        zip = (EditText) findViewById(R.id.zipcode);
        check1 = (CheckBox) findViewById(R.id.checkBox1);
        check2 = (CheckBox) findViewById(R.id.checkBox2);
        post = (Button) findViewById(R.id.post);
        pros = (TextView) findViewById(R.id.pros);
        cons = (TextView) findViewById(R.id.cons);
        relative = (RelativeLayout) findViewById(R.id.layout2);
        linear = (LinearLayout) findViewById(R.id.lay);
        layout = (LinearLayout) findViewById(R.id.layout);
        logo = (ImageView) findViewById(R.id.logo);

        getJobDetails();

        text.setText(name);
       /* add.setText(getAddress);
        cit.setText(getCity);
        stat.setText(getState);
        zip.setText(getZipcode);*/
        linear.setVisibility(View.VISIBLE);

        pros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(EditCreateJob2.this);
                dialog.setContentView(R.layout.pros_popup);

                // set the custom dialog components - text, image and button
                ImageView close = (ImageView) dialog.findViewById(R.id.close_btn);
                // if button is clicked, close the custom dialog
                close.setOnClickListener(new View.OnClickListener() {
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
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                j_address = add.getText().toString().trim();
                j_city = cit.getText().toString().trim();
                j_state = stat.getText().toString().trim();
                j_zipcode = zip.getText().toString().trim();
                if(j_address.equals("")&&j_city.equals("")&&j_state.equals("")&&j_zipcode.equals(""))
                {
                    add.clearFocus();
                    cit.clearFocus();
                    stat.clearFocus();
                    zip.clearFocus();
                    linear.setVisibility(View.VISIBLE);






                }
                if(!j_address.equals("")&&!j_city.equals("")&&!j_state.equals("")&&!j_zipcode.equals(""))
                {
                    check1.setChecked(false);
                }

            }
        });

        cons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(EditCreateJob2.this);
                dialog.setContentView(R.layout.cons_popup);

                // set the custom dialog components - text, image and button
                ImageView close = (ImageView) dialog.findViewById(R.id.close_btn);
                // if button is clicked, close the custom dialog
                close.setOnClickListener(new View.OnClickListener() {
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
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        check1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check1.isChecked())
                {
                    add.setText("");
                    cit.setText("");
                    stat.setText("");
                    zip.setText("");
                    locationTrack = new LocationTrack(EditCreateJob2.this);
                    if (locationTrack.canGetLocation()) {
                        lon = locationTrack.getLongitude();
                        lat = locationTrack.getLatitude();
                        latitude = String.valueOf(lat);
                        longitude = String.valueOf(lon);
                        System.out.println("kkkkkkkkkkkkkk:latitude::check::"+latitude);
                        System.out.println("kkkkkkkkkkkkkk:longitude:check::"+longitude);
                        Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(lon) + "\nLatitude:" + Double.toString(lat), Toast.LENGTH_SHORT).show();
                    } else {
                        locationTrack.showSettingsAlert();
                    }
                }
            }
        });

        add.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    check1.setChecked(false);
                } else {
                    linear.setVisibility(View.VISIBLE);
                }
            }
        });
        cit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    check1.setChecked(false);
                } else {
                    linear.setVisibility(View.VISIBLE);
                }
            }
        });
        stat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    check1.setChecked(false);
                } else {
                    linear.setVisibility(View.VISIBLE);
                }
            }
        });
        zip.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    check1.setChecked(false);
                } else {
                    linear.setVisibility(View.VISIBLE);
                }
            }
        });
        swipe = new Swipe();
        swipe.setListener(new SimpleSwipeListener() {


            @Override
            public boolean onSwipedLeft(MotionEvent event) {
                Intent i = new Intent(EditCreateJob2.this,ProfilePage.class);
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
                Intent j = new Intent(EditCreateJob2.this, SwitchingSide.class);
                startActivity(j);
                finish();
                return super.onSwipedRight(event);
            }
        });
    }

    public void validate() {
        j_address = add.getText().toString().trim();
        j_city = cit.getText().toString().trim();
        j_state = stat.getText().toString().trim();
        j_zipcode = zip.getText().toString().trim();

        if(check2.isChecked()) {
            post_address = "yes";
        }
        else {
            post_address = "no";
            String address = j_address + j_city + j_state + j_zipcode;
            System.out.println("ssssssssss:add::"+ address);
            getGeoCoordsFromAddress(this,address);
            System.out.println("kkkkkkkkkkkkkk:getlatitude:"+get_lat);
            System.out.println("kkkkkkkkkkkkkk:getlatitude:"+get_lon);
            latitude = get_lat;
            longitude = get_lon;
            System.out.println("kkkkkkkkkkkkkk:latitude:"+latitude);
            System.out.println("kkkkkkkkkkkkkk:longitude:"+longitude);
        }
        if(check1.isChecked())
        {
            current_location = "yes";
            locationTrack = new LocationTrack(EditCreateJob2.this);
            if (locationTrack.canGetLocation()) {
                lon = locationTrack.getLongitude();
                lat = locationTrack.getLatitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(lon);
                System.out.println("kkkkkkkkkkkkkk:latitude::check::"+latitude);
                System.out.println("kkkkkkkkkkkkkk:longitude:check::"+longitude);
                Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(lon) + "\nLatitude:" + Double.toString(lat), Toast.LENGTH_SHORT).show();
            } else {
                locationTrack.showSettingsAlert();
            }
        }
        else
        {
            current_location = "no";
            String address = j_address + j_city + j_state + j_zipcode;
            System.out.println("ssssssssss:add::"+ address);
            getGeoCoordsFromAddress(this,address);
            System.out.println("kkkkkkkkkkkkkk:getlatitude:"+get_lat);
            System.out.println("kkkkkkkkkkkkkk:getlatitude:"+get_lon);
            latitude = get_lat;
            longitude = get_lon;
            System.out.println("kkkkkkkkkkkkkk:latitude:"+latitude);
            System.out.println("kkkkkkkkkkkkkk:longitude:"+longitude);
        }
        if (!check1.isChecked() && (j_address.equals("") || j_state.equals("") || j_city.equals("") || j_zipcode.equals(""))) {
            final Dialog dialog = new Dialog(EditCreateJob2.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must Fill In either\"Address or Current location option\" Box");
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
        } else {
            if(latitude.equals("0.0")||longitude.equals("0.0"))
            {
                final Dialog dialog = new Dialog(EditCreateJob2.this);
                dialog.setContentView(R.layout.custom_dialog);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Please enter \"Valid Address\" Box");
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
            else
            {
                //registerUser();
                Intent i = new Intent(EditCreateJob2.this,SummaryMultiply.class);
                i.putExtra("key",value);
                i.putExtra("userId", id);
                i.putExtra("job_name",name);
                i.putExtra("user_type", usertype);
                i.putExtra("job_category", category);
                i.putExtra("job_decription", description);
                i.putExtra("job_date", date);
                i.putExtra("job_start_date", start_time);
                i.putExtra("job_end_date",end_time);
                i.putExtra("start_time", start_time);
                i.putExtra("end_time", end_time);
                i.putExtra("payment_amount", amount);
                i.putExtra("pocket_expense", pocket);
                i.putExtra("payment_type", type);
                i.putExtra("address",address);
                i.putExtra("city", city);
                i.putExtra("location", current_location);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                i.putExtra("post_address", post_address);
                i.putExtra("latitude",latitude);
                i.putExtra("longitude", longitude);
                i.putExtra("job_address", address);
                i.putExtra("job_city", city);
                i.putExtra("job_state", state);
                i.putExtra("job_zipcode", zipcode);
                i.putExtra("estimated_payment",estimated_amount);
                i.putExtra("flexible", flexible_status);
                i.putExtra("paypal_fee", new_value);
                i.putExtra("job_payout", flexible_status);
                i.putExtra("fee_details", new_value);
                startActivity(i);
                finish();
            }
        }
    }

    private void registerUser() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(Registrationpage3.this,response,Toast.LENGTH_LONG).show();
                        System.out.println("eeeee:" + response);
                        onResponserecieved(response, 1);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.show();
                      /*  try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            System.out.println("eeeeeeeeeeeeror:" + jsonObject);

                        } catch (JSONException e) {
                            //Handle a malformed json response
                        } catch (
                                UnsupportedEncodingException error1) {
                        }*/

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put(APP_KEY,value);
                params.put(USER_ID,id);
                params.put(JOB_NAME,name);
                params.put(USER_TYPE,usertype);
                params.put(JOB_CATEGORY, category);
                params.put(JOB_DESCRIPTION,description);
                params.put(JOB_DATE,date);
                params.put(JOB_START_DATE,start_time);
                params.put(JOB_END_DATE,end_time);
                params.put(START_TIME,start_time);
                params.put(END_TIME,end_time);
                params.put(JOB_PAYMENT_AMOUNT,amount);
                params.put(POCKET_EXPENSE,pocket);
                params.put(JOB_PAYMENT_TYPE,type);
                params.put(ADDRESS,address);
                params.put(CITY,j_city);
                params.put(CURRENT_LOCATION,current_location);
                params.put(STATE,state);
                params.put(ZIPCODE,zipcode);
                params.put(POST_ADDRESS,post_address);
                params.put(LATITUDE,latitude);
                params.put(LONGITUDE,longitude);
                params.put(JOB_ADDRESS,address);
                params.put(JOB_CITY,city);
                params.put(JOB_STATE,city);
                params.put(JOB_ID,jobId);
                params.put(JOB_ZIPCODE,zipcode);
                params.put(ESTIMATED_PAYMENT,estimated_amount);
                params.put(FLEXIBLE,flexible_status);
                return params;
            }
        };

        System.out.println("vvvvvvv1:"+".."+value+".."+id+".."+name+".."+usertype+"..");
        System.out.println("vvvvvvv2:"+".."+category+".."+description+".."+date+".."+start_time+"..");
        System.out.println("vvvvvvv3:"+".."+end_time+".."+amount+".."+type+".."+address+"..");
        System.out.println("vvvvvvv4:"+".."+city+".."+state+".."+zipcode+".."+post_address+"..");
        System.out.println("vvvvvvv5:"+".."+latitude+".."+longitude+".."+estimated_amount+".."+flexible_status+"..");

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved(String jsonobject, int requesttype) {
        System.out.println("response from interface" + jsonobject);

        String status = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");
            job_id = jResult.getString("job_id");
            System.out.println("jjjjjjjjjjjob:id::" + job_id);

            if (status.equals("success")) {
                // custom dialog
                final Dialog dialog = new Dialog(EditCreateJob2.this);
                dialog.setContentView(R.layout.gray_custom);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Job Updated Successfully");
                Button dialogButton = (Button) dialog.findViewById(R.id.ok);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent i = new Intent(EditCreateJob2.this, EditPostedJobs.class);
                        i.putExtra("userId", id);
                        i.putExtra("jobId", job_id);
                        i.putExtra("address", address);
                        i.putExtra("city", city);
                        i.putExtra("state", state);
                        i.putExtra("zipcode", zipcode);
                        startActivity(i);
                        finish();
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                return;
            } else {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getJobDetails()
    {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_JOB,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:new:get:job:" + response);
                        onResponserecieved1(response, 2);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            System.out.println("error" + jsonObject);
                        } catch (JSONException e) {
                            //Handle a malformed json response
                        } catch (UnsupportedEncodingException error1) {

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(APP_KEY, value);
                params.put(JOB_ID, jobId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved1(String jsonobject, int i) {
        System.out.println("response from interface" + jsonobject);

        String status = null;
        String job_data = null;

        try {
            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            if (status.equals("success")) {
                job_data = jResult.getString("job_data");
                System.out.println("jjjjjjjjjjjjjjjob:::job_data:::" + job_data);
                JSONObject object = new JSONObject(job_data);
                String get_address = object.getString("address");
                System.out.println("nnnnnnnnnnn:get_address::"+get_address);
                String get_city = object.getString("city");
                System.out.println("nnnnnnnnnnn:get_city::" + get_city);
                String get_state = object.getString("state");
                System.out.println("nnnnnnnnnnn:get_state::" + get_state);
                String get_zipcode = object.getString("zipcode");
                System.out.println("nnnnnnnnnnn:get_zipcode::" + get_zipcode);
            } else {
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static LatLng getGeoCoordsFromAddress(Context c, String address)
    {
        Geocoder geocoder = new Geocoder(c);
        List<Address> addresses;
        try
        {
            addresses = geocoder.getFromLocationName(address, 1);
            if(addresses.size() > 0)
            {
                double latitud = addresses.get(0).getLatitude();
                double longitud = addresses.get(0).getLongitude();
                get_lat = String.valueOf(latitud);
                get_lon = String.valueOf(longitud);
                System.out.println("kkkkkkkkkkkkkk:latitude:"+get_lat);
                System.out.println("kkkkkkkkkkkkkk:latitude:"+get_lon);
                return new LatLng(latitud, longitud);
            }
            else
            {
                return null;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public boolean dispatchTouchEvent(MotionEvent event){

        swipe.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

}