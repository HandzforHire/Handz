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

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RehireAdd extends Activity implements SimpleGestureFilter.SimpleGestureListener{

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
    public static String JOB_EXPIRE = "job_expire";
    public static String SUB_CATEGORY = "sub_category";
    public static String CATEGORY_COLOR = "job_category_color";
    public static String DELIST = "delist";
    public static String EMPLOYEE_ID = "employee_id";
    public static String JOB_ID = "job_id";
    String key = "HandzForHire@~";
    String job_id, hour_expected,job_expire;
    TextView job_payout,pocket_expense,paypal_merchant;
    EditText hourly_value,expected_value;
    String value,id,name,category,description,date,start_time,expected_hours,end_time,amount,type;
    String estimated_amount,flexible_status;
    String fee_details = "add";
    String address = "";
    String city = "";
    String state = "";
    String zipcode = "";
    String usertype = "employer";
    String sub_category;
    String job_category_color;
    String expense,fee,payout,duration,employeeId;
    String delist = "yes";
    String post_address = "";
    String latitude = "";
    String longitude = "";
    String current_location = "";

    Dialog dialog;
    private SimpleGestureFilter detector;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_add);

        ImageView logo = (ImageView) findViewById(R.id.logo);
        ImageView back = (ImageView) findViewById(R.id.back);
        TextView create_job = (TextView) findViewById(R.id.create_btn);
        pocket_expense = (TextView) findViewById(R.id.ope);
        paypal_merchant = (TextView) findViewById(R.id.pmpf);
        job_payout = (TextView) findViewById(R.id.ajp);
        hourly_value = (EditText) findViewById(R.id.hourly_text);
        expected_value = (EditText) findViewById(R.id.expected_text);

        Intent i = getIntent();
        id = i.getStringExtra("userId");
        name = i.getStringExtra("job_name");
        category = i.getStringExtra("job_category");
        job_category_color = i.getStringExtra("job_category_color");
        sub_category = i.getStringExtra("sub_category");
        description = i.getStringExtra("job_decription");
        date = i.getStringExtra("job_date");
        start_time = i.getStringExtra("start_time");
        expected_hours = i.getStringExtra("expected_hours");
        amount = i.getStringExtra("payment_amount");
        type = i.getStringExtra("payment_type");
        estimated_amount = i.getStringExtra("estimated_amount");
        flexible_status = i.getStringExtra("flexible_status");
        job_expire = i.getStringExtra("job_expire");
        job_id = i.getStringExtra("job_id");
        duration = i.getStringExtra("duration");
        employeeId = i.getStringExtra("employeeId");

        dialog = new Dialog(RehireAdd.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        detector = new SimpleGestureFilter(this,this);

        hourly_value.setText(amount);
        expected_value.setText(expected_hours);
        String hour = hourly_value.getText().toString();
        String expected = expected_value.getText().toString();
        hour_expected = String.valueOf(Float.valueOf(hour)*Float.valueOf(expected));
        job_payout.setText(hour_expected);
        String handz_fee = "1.00";
        String add = String.valueOf(Float.valueOf(estimated_amount)+Float.valueOf(handz_fee));
        //String add = estimated_amount + handz_fee;
        System.out.println("sssssssssssss:add:add:"+add+"..."+hour_expected);
        String mul = "0.029";
        String multiply = String.valueOf(Float.valueOf(add)*Float.valueOf(mul));
        System.out.println("sssssssssssss:add:multiply:"+multiply);
        String value = "0.30";
        String total = String.valueOf(Float.valueOf(multiply)+Float.valueOf(value));
        System.out.println("sssssssssssss:add:total:"+total);
        String total_value = String.format("%.2f", Float.valueOf(total));
        //double newKB = Math.round(Double.valueOf(total)*100.0)/100.0;
        System.out.println("sssssssssssss:add:total_value:"+total_value);
        paypal_merchant.setText(total_value);
        String pocket_value = String.valueOf(Float.valueOf(hour_expected)+Float.valueOf(handz_fee)+Float.valueOf(total_value));
        System.out.println("sssssssssssss:add:pocket_value:"+pocket_value);
        pocket_expense.setText(pocket_value);

        hourly_value.addTextChangedListener(tw);
        expected_value.addTextChangedListener(tw1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RehireAdd.this,ProfilePage.class);
                startActivity(i);
            }
        });

        create_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payout = job_payout.getText().toString();
                expense = "$ " + pocket_expense.getText().toString();
                fee = "$ " + paypal_merchant.getText().toString();
                System.out.println("sssssssssssss:add:pay:expe:fee:"+payout+".."+expense+".."+fee);
                registerUser();
            }
        });

    }

    private void registerUser()
    {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(Registrationpage3.this,response,Toast.LENGTH_LONG).show();
                        System.out.println("eeeee:createjob2"+response);
                        onResponserecieved(response,1);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        dialog.dismiss();
                        try {
                            String responseBody = new String(volleyError.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            System.out.println("error" + jsonObject);
                        } catch (JSONException e) {

                        } catch (UnsupportedEncodingException error1) {

                        }
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
                params.put(APP_KEY,key);
                params.put(USER_ID,id);
                params.put(JOB_NAME,name);
                params.put(USER_TYPE,usertype);
                params.put(JOB_CATEGORY, category);
                params.put(JOB_DESCRIPTION,description);
                params.put(JOB_DATE,date);
                params.put(JOB_START_DATE,start_time);
                params.put(JOB_END_DATE,start_time);
                params.put(START_TIME,start_time);
                params.put(END_TIME,start_time);
                params.put(JOB_PAYMENT_AMOUNT,amount);
                params.put(POCKET_EXPENSE,expense);
                params.put(JOB_PAYMENT_TYPE,duration);
                params.put(ADDRESS,address);
                params.put(CITY,city);
                params.put(CURRENT_LOCATION,current_location);
                params.put(STATE,state);
                params.put(ZIPCODE,zipcode);
                params.put(POST_ADDRESS,post_address);
                params.put(LATITUDE,latitude);
                params.put(LONGITUDE,longitude);
                params.put(JOB_ADDRESS,address);
                params.put(JOB_CITY,city);
                params.put(JOB_STATE,state);
                params.put(JOB_ZIPCODE,zipcode);
                params.put(ESTIMATED_PAYMENT,expense);
                params.put(FLEXIBLE,flexible_status);
                params.put(PAYPAL_FEE,fee);
                params.put(JOB_PAYOUT,payout);
                params.put(FEE_DETAILS,fee_details);
                params.put(JOB_EXPIRE,job_expire);
                params.put(SUB_CATEGORY,sub_category);
                params.put(CATEGORY_COLOR,job_category_color);
                params.put(DELIST,delist);
                params.put(EMPLOYEE_ID,employeeId);
                return params;
            }

        };

      /*  System.out.println("vvvvvvv1:"+".."+value+".."+id+".."+name+".."+usertype+".."+job_expire);
        System.out.println("vvvvvvv2:"+".."+category+".."+description+".."+date+".."+start_time+"..");
        System.out.println("vvvvvvv3:"+".."+end_time+".."+amount+".."+type+".."+job_address+"..");
        System.out.println("vvvvvvv4:"+".."+job_city+".."+job_state+".."+job_zipcode+".."+post_address+"..");
        System.out.println("vvvvvvv5:"+".."+latitude+".."+longitude+".."+estimated_amount+".."+flexible_status+"..");*/
        System.out.println("66666666-APP_KEY- "+key);
        System.out.println("66666666-USER_ID- "+id);
        System.out.println("66666666-JOB_NAME- "+name);
        System.out.println("66666666-USER_TYPE- "+usertype);
        System.out.println("66666666-JOB_CATEGORY- "+category);
        System.out.println("66666666-JOB_DESCRIPTION- "+description);
        System.out.println("66666666-JOB_DATE- "+date);
        System.out.println("66666666-JOB_START_DATE- "+start_time);
        System.out.println("66666666-JOB_END_DATE- "+start_time);
        System.out.println("66666666-START_TIME- "+start_time);
        System.out.println("66666666-END_TIME- "+start_time);
        System.out.println("66666666-JOB_PAYMENT_AMOUNT- "+amount);
        System.out.println("66666666-POCKET_EXPENSE- "+expense);
        System.out.println("66666666-JOB_PAYMENT_TYPE- "+duration);
        System.out.println("66666666-ADDRESS- "+address);
        System.out.println("66666666-CITY- "+city);
        System.out.println("66666666-CURRENT_LOCATION- "+current_location);
        System.out.println("66666666-STATE- "+state);
        System.out.println("66666666-ZIPCODE- "+zipcode);
        System.out.println("66666666-POST_ADDRESS- "+post_address);
        System.out.println("66666666-LATITUDE- "+latitude);
        System.out.println("66666666-LONGITUDE- "+longitude);
        System.out.println("66666666-JOB_ADDRESS- "+address);
        System.out.println("66666666-JOB_CITY- "+city);
        System.out.println("66666666-JOB_STATE- "+state);
        System.out.println("66666666-JOB_ZIPCODE- "+zipcode);
        System.out.println("66666666-ESTIMATED_PAYMENT- "+estimated_amount);
        System.out.println("66666666-FLEXIBLE- "+flexible_status);
        System.out.println("66666666-PAYPAL_FEE- "+fee);
        System.out.println("66666666-JOB_PAYOUT- "+payout);
        System.out.println("66666666-FEE_DETAILS- "+fee_details);
        System.out.println("66666666-JOB_EXPIRE- "+job_expire);
        System.out.println("66666666-SUB_CATEGORY- "+sub_category);
        System.out.println("66666666-CATEGORY_COLOR- "+job_category_color);
        System.out.println("66666666-DELIST- "+delist);
        System.out.println("66666666-EMPLOYEEID- "+employeeId);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved(String jsonobject, int requesttype) {
        System.out.println("response from interface"+jsonobject);

        String status = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");

            if(status.equals("success"))
            {
                // custom dialog
                final Dialog dialog = new Dialog(RehireAdd.this);
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
                        Intent i = new Intent(RehireAdd.this,ProfilePage.class);
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

                hourly_value.removeTextChangedListener(this);
                hourly_value.setText(cashAmountBuilder.toString());

                hourly_value.setTextKeepState(cashAmountBuilder.toString());
                Selection.setSelection(hourly_value.getText(), cashAmountBuilder.toString().length());

                hourly_value.addTextChangedListener(this);
            }

            String new_pay_amount = hourly_value.getText().toString();
            System.out.println("sssssssssssss::new_pay_amount:"+new_pay_amount);
            String new_hours = expected_value.getText().toString();
            String job_estimated = String.valueOf(Float.valueOf(new_pay_amount)*Float.valueOf(new_hours));
            System.out.println("sssssssssssss:job_estimated:multiply:"+job_estimated);
            job_payout.setText(job_estimated);

           /* String hour = hourly_value.getText().toString();
            String expected = expected_value.getText().toString();
            String hour_expected = String.valueOf(Float.valueOf(hour)*Float.valueOf(expected));
            job_payout.setText(hour_expected);*/
            String handz_fee = "1.00";
            String add = String.valueOf(Float.valueOf(job_estimated)+Float.valueOf(handz_fee));
            //String add = estimated_amount + handz_fee;
            System.out.println("sssssssssssss:add:add:"+add+"..."+hour_expected);
            String mul = "0.029";
            String multiply = String.valueOf(Float.valueOf(add)*Float.valueOf(mul));
            System.out.println("sssssssssssss:add:multiply:"+multiply);
            String value = "0.30";
            String total = String.valueOf(Float.valueOf(multiply)+Float.valueOf(value));
            System.out.println("sssssssssssss:add:total:"+total);
            String total_value = String.format("%.2f", Float.valueOf(total));
            //double newKB = Math.round(Double.valueOf(total)*100.0)/100.0;
            System.out.println("sssssssssssss:add:total_value:"+total_value);
            paypal_merchant.setText(total_value);
            String pocket_value = String.valueOf(Float.valueOf(job_estimated)+Float.valueOf(handz_fee)+Float.valueOf(total_value));
            System.out.println("sssssssssssss:add:pocket_value:"+pocket_value);
            pocket_expense.setText(pocket_value);
        }
    };

    TextWatcher tw1 = new TextWatcher() {

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

                expected_value.removeTextChangedListener(this);
                expected_value.setText(cashAmountBuilder.toString());

                expected_value.setTextKeepState(cashAmountBuilder.toString());
                Selection.setSelection(expected_value.getText(), cashAmountBuilder.toString().length());

                expected_value.addTextChangedListener(this);
            }
            String new_hours = expected_value.getText().toString();
            System.out.println("sssssssssssss::new_hours:"+new_hours);
            String new_amount = hourly_value.getText().toString();
            String estimated = String.valueOf(Float.valueOf(new_hours)*Float.valueOf(new_amount));
            System.out.println("sssssssssssss:estimated:multiply:"+estimated);
            job_payout.setText(estimated);

            /*String hour = hourly_value.getText().toString();
            String expected = expected_value.getText().toString();
            String hour_expected = String.valueOf(Float.valueOf(hour)*Float.valueOf(expected));
            job_payout.setText(hour_expected);*/
            String handz_fee = "1.00";
            String add = String.valueOf(Float.valueOf(estimated)+Float.valueOf(handz_fee));
            //String add = estimated_amount + handz_fee;
            System.out.println("sssssssssssss:add:add:"+add+"..."+hour_expected);
            String mul = "0.029";
            String multiply = String.valueOf(Float.valueOf(add)*Float.valueOf(mul));
            System.out.println("sssssssssssss:add:multiply:"+multiply);
            String value = "0.30";
            String total = String.valueOf(Float.valueOf(multiply)+Float.valueOf(value));
            System.out.println("sssssssssssss:add:total:"+total);
            String total_value = String.format("%.2f", Float.valueOf(total));
            //double newKB = Math.round(Double.valueOf(total)*100.0)/100.0;
            System.out.println("sssssssssssss:add:total_value:"+total_value);
            paypal_merchant.setText(total_value);
            String pocket_value = String.valueOf(Float.valueOf(estimated)+Float.valueOf(handz_fee)+Float.valueOf(total_value));
            System.out.println("sssssssssssss:add:pocket_value:"+pocket_value);
            pocket_expense.setText(pocket_value);
        }
    };


    @Override
    public void onSwipe(int direction) {
        String str = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
                Intent j = new Intent(getApplicationContext(), SwitchingSide.class);
                startActivity(j);
                finish();
                break;
            case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
                Intent i;
                if(Profilevalues.usertype.equals("1")) {
                    i = new Intent(getApplicationContext(), ProfilePage.class);
                }else{
                    i = new Intent(getApplicationContext(), LendProfilePage.class);
                }
                i.putExtra("userId", Profilevalues.user_id);
                i.putExtra("address", Profilevalues.address);
                i.putExtra("city", Profilevalues.city);
                i.putExtra("state", Profilevalues.state);
                i.putExtra("zipcode", Profilevalues.zipcode);
                startActivity(i);
                finish();

                break;
            case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
                break;

        }
        //  Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDoubleTap() {

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event){

        this.detector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }
}
