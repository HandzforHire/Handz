package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SummaryMultiply extends Activity {

    EditText pay_amount,hours;
    TextView create_job,add,subtract,total;
    String job_id,job_city,job_state,job_zipcode;
    private static final String URL = Constant.SERVER_URL+"create_job";
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
    public static String JOB_PAYOUT = "jobPayout";
    public static String PAYPAL_FEE = "paypalFee";
    public static String FEE_DETAILS = "fee_details";
    String key = "HandzForHire@~";
    String value,id,name,usertype,category,description,date,start_time,end_time,amount,pocket,type,address,city,current_location;
    String state,zipcode,post_address,job_address,latitude,longitude,estimated_amount,flexible_status,paypal_fee,fee_details;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_multiply);

        pay_amount = (EditText) findViewById(R.id.amount);
        hours = (EditText) findViewById(R.id.hours);
        total = (TextView) findViewById(R.id.total);
        add = (TextView) findViewById(R.id.add);
        //create_job = (TextView) findViewById(R.id.create_job);
        subtract = (TextView) findViewById(R.id.subtract);
        ImageView logo = (ImageView) findViewById(R.id.logo);

        Intent i = getIntent();
        value = i.getStringExtra("key");
        id = i.getStringExtra("userId");
        name = i.getStringExtra("job_name");
        usertype = i.getStringExtra("user_type");
        category = i.getStringExtra("job_category");
        description = i.getStringExtra("job_decription");
        date = i.getStringExtra("job_date");
        start_time = i.getStringExtra("job_start_date");
        end_time = i.getStringExtra("job_end_date");
        start_time = i.getStringExtra("start_time");
        end_time = i.getStringExtra("end_time");
        amount = i.getStringExtra("payment_amount");
        pocket = i.getStringExtra("pocket_expense");
        type = i.getStringExtra("payment_type");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        current_location = i.getStringExtra("location");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");
        post_address = i.getStringExtra("post_address");
        latitude = i.getStringExtra("latitude");
        longitude = i.getStringExtra("longitude");
        job_address = i.getStringExtra("job_address");
        job_city = i.getStringExtra("job_city");
        job_state = i.getStringExtra("job_state");
        job_zipcode = i.getStringExtra("job_zipcode");
        estimated_amount = i.getStringExtra("estimated_payment");
        flexible_status = i.getStringExtra("flexible");
        paypal_fee = i.getStringExtra("paypal_fee");
        flexible_status = i.getStringExtra("job_payout");
        fee_details = i.getStringExtra("fee_details");

        System.out.println("sssssssssssss:amount::"+amount+"..."+estimated_amount+"..."+end_time);

        pay_amount.setText(amount);
        hours.setText(end_time);
        total.setText(estimated_amount);

        pay_amount.addTextChangedListener(tw);

       /* create_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });*/

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SummaryMultiply.this,SummaryAdd.class);
                startActivity(i);
                finish();
            }
        });

        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SummaryMultiply.this,SummarySubtract.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void registerUser()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(Registrationpage3.this,response,Toast.LENGTH_LONG).show();
                        System.out.println("eeeee:createjob2"+response);
                        onResponserecieved(response,1);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        /*try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            System.out.println("error" + jsonObject);
                        } catch (JSONException e) {

                        } catch (UnsupportedEncodingException error1) {

                        }*/
                        if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
                            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                            volleyError = error;
                            System.out.println("error" + volleyError);
                        }else{

                        }
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
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
                params.put(CITY,city);
                params.put(CURRENT_LOCATION,current_location);
                params.put(STATE,state);
                params.put(ZIPCODE,zipcode);
                params.put(POST_ADDRESS,post_address);
                params.put(LATITUDE,latitude);
                params.put(LONGITUDE,longitude);
                params.put(JOB_ADDRESS,job_address);
                params.put(JOB_CITY,job_city);
                params.put(JOB_STATE,job_state);
                params.put(JOB_ZIPCODE,job_zipcode);
                params.put(ESTIMATED_PAYMENT,estimated_amount);
                params.put(FLEXIBLE,flexible_status);
                params.put(PAYPAL_FEE,paypal_fee);
                params.put(JOB_PAYOUT,flexible_status);
                params.put(FEE_DETAILS,fee_details);
                return params;
            }

        };

     /*   System.out.println("vvvvvvv1:"+".."+value+".."+id+".."+name+".."+usertype+"..");
        System.out.println("vvvvvvv2:"+".."+category+".."+description+".."+date+".."+start_time+"..");
        System.out.println("vvvvvvv3:"+".."+end_time+".."+amount+".."+type+".."+address+"..");
        System.out.println("vvvvvvv4:"+".."+city+".."+state+".."+zipcode+".."+post_address+"..");
        System.out.println("vvvvvvv5:"+".."+latitude+".."+longitude+".."+estimated_amount+".."+flexible_status+"..");*/
        System.out.println("USER_ID "+id);
        System.out.println("JOB_NAME "+name);
        System.out.println("USER_TYPE "+usertype);
        System.out.println("JOB_CATEGORY "+category);
        System.out.println("JOB_DESCRIPTION "+description);
        System.out.println("JOB_START_DATE "+start_time);
        System.out.println("END_TIME "+end_time);
        System.out.println("JOB_PAYMENT_AMOUNT "+amount);
        System.out.println("POCKET_EXPENSE "+pocket);
        System.out.println("JOB_PAYMENT_TYPE "+type);
        System.out.println("ADDRESS "+address);
        System.out.println("CITY "+job_city);
        System.out.println("CURRENT_LOCATION "+current_location);
        System.out.println("STATE "+state);
        System.out.println("ZIPCODE "+zipcode);
        System.out.println("POST_ADDRESS "+post_address);
        System.out.println("LATITUDE "+latitude);
        System.out.println("LONGITUDE"+longitude);
        System.out.println("ESTIMATED_PAYMENT "+estimated_amount);
        System.out.println("FLEXIBLE "+flexible_status);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved(String jsonobject, int requesttype) {
        System.out.println("response from interface"+jsonobject);

        String status = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");
            job_id = jResult.getString("job_id");
            System.out.println("jjjjjjjjjjjob:id::"+job_id);

            if(status.equals("success"))
            {
                // custom dialog
                final Dialog dialog = new Dialog(SummaryMultiply.this);
                dialog.setContentView(R.layout.gray_custom);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Job Created Successfully");
                Button dialogButton = (Button) dialog.findViewById(R.id.ok);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent i = new Intent(SummaryMultiply.this,PostedJobs.class);
                        i.putExtra("userId", id);
                        i.putExtra("jobId",job_id);
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
            }
            else
            {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    TextWatcher tw = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (!s.toString().matches("^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {
                String userInput = "" + s.toString().replaceAll("[^\\d]", "");
                StringBuilder cashAmountBuilder = new StringBuilder(userInput);
                while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                    cashAmountBuilder.deleteCharAt(0);
                }
                while (cashAmountBuilder.length() < 3) {
                    cashAmountBuilder.insert(0, '0');
                }
                cashAmountBuilder.insert(cashAmountBuilder.length() - 2, '.');

                pay_amount.removeTextChangedListener(this);
                pay_amount.setText(cashAmountBuilder.toString());

                pay_amount.setTextKeepState("$" + cashAmountBuilder.toString());
                Selection.setSelection(pay_amount.getText(), cashAmountBuilder.toString().length() + 1);

                pay_amount.addTextChangedListener(this);
                System.out.println("sssssssssssss::pay_amount:"+pay_amount);
            }

           String new_pay_amount = pay_amount.getText().toString();
            System.out.println("sssssssssssss::new_pay_amount:"+new_pay_amount);
            String amount1 = new_pay_amount.substring(1);
            System.out.println("amount:: "+amount1);
            String new_hours = hours.getText().toString();
            String job_estimated = String.valueOf(Float.valueOf(amount1)*Float.valueOf(new_hours));
            System.out.println("sssssssssssss:job_estimated:multiply:"+job_estimated);
            total.setText(job_estimated);
        }
    };
}
