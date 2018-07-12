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
import com.github.pwittchen.swipe.library.rx2.SimpleSwipeListener;
import com.github.pwittchen.swipe.library.rx2.Swipe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SummaryMultiply extends Activity {

    EditText pay_amount,hours;
    TextView add,subtract,total;
    String job_id,job_expire;
    String value,id,name,usertype,category,description,date,start_time,expected_hours,end_time,amount,type,address,city,current_location;
    String state,zipcode,post_address,latitude,longitude,estimated_amount,flexible_status;
    Swipe swipe;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_multiply);

        pay_amount = (EditText) findViewById(R.id.amount);
        hours = (EditText) findViewById(R.id.hours);
        total = (TextView) findViewById(R.id.total);
        add = (TextView) findViewById(R.id.add);
        subtract = (TextView) findViewById(R.id.subtract);
        ImageView logo = (ImageView) findViewById(R.id.logo);

        Intent i = getIntent();
        id = i.getStringExtra("userId");
        name = i.getStringExtra("job_name");
        category = i.getStringExtra("job_category");
        description = i.getStringExtra("job_decription");
        date = i.getStringExtra("job_date");
        start_time = i.getStringExtra("job_start_date");
        expected_hours = i.getStringExtra("expected_hours");
        amount = i.getStringExtra("payment_amount");
        type = i.getStringExtra("payment_type");
        current_location = i.getStringExtra("location");
        post_address = i.getStringExtra("post_address");
        latitude = i.getStringExtra("latitude");
        longitude = i.getStringExtra("longitude");
        estimated_amount = i.getStringExtra("estimated_payment");
        flexible_status = i.getStringExtra("flexible");
        job_expire = i.getStringExtra("job_expire");

        System.out.println("sssssssssssss:amount::"+amount+"..."+estimated_amount+"..."+expected_hours+"..."+job_expire);

        pay_amount.setText(amount);
        hours.setText(expected_hours);
        total.setText(estimated_amount);

        pay_amount.addTextChangedListener(tw);
        hours.addTextChangedListener(tw1);

        swipe = new Swipe();
        swipe.setListener(new SimpleSwipeListener() {
            @Override
            public void onSwipingLeft(MotionEvent event) {
                super.onSwipingLeft(event);
                Intent i = new Intent(SummaryMultiply.this,ProfilePage.class);
                i.putExtra("userId", Profilevalues.user_id);
                i.putExtra("address", Profilevalues.address);
                i.putExtra("city", Profilevalues.city);
                i.putExtra("state", Profilevalues.state);
                i.putExtra("zipcode", Profilevalues.zipcode);
                startActivity(i);
                finish();
            }

            @Override
            public boolean onSwipedLeft(MotionEvent event) {
                Intent i = new Intent(SummaryMultiply.this,ProfilePage.class);
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
            public void onSwipingRight(MotionEvent event) {
                super.onSwipingRight(event);
                Intent j = new Intent(SummaryMultiply.this, SwitchingSide.class);
                startActivity(j);
                finish();

            }

            @Override
            public boolean onSwipedRight(MotionEvent event) {
                Intent j = new Intent(SummaryMultiply.this, SwitchingSide.class);
                startActivity(j);
                finish();
                return super.onSwipedRight(event);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expectedhour = hours.getText().toString();
                String paymentamount = pay_amount.getText().toString();
                String totalvalue = total.getText().toString();
                System.out.println("7777777777777::"+ expectedhour+",,"+paymentamount+",,"+totalvalue);
                Intent i = new Intent(SummaryMultiply.this,SummaryAdd.class);
                i.putExtra("userId", id);
                i.putExtra("job_name",name);
                i.putExtra("job_category", category);
                i.putExtra("job_decription", description);
                i.putExtra("job_date", date);
                i.putExtra("job_start_date", start_time);
                i.putExtra("expected_hours", expectedhour);
                i.putExtra("payment_amount", paymentamount);
                i.putExtra("payment_type", type);
                i.putExtra("location", current_location);
                i.putExtra("post_address", post_address);
                i.putExtra("latitude",latitude);
                i.putExtra("longitude", longitude);
                i.putExtra("estimated_payment",estimated_amount);
                i.putExtra("flexible", flexible_status);
                i.putExtra("job_expire", job_expire);
                startActivity(i);
            }
        });

        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String expectedhour = hours.getText().toString();
                String paymentamount = pay_amount.getText().toString();
                String totalvalue = total.getText().toString();
                System.out.println("7777777777777::"+ expectedhour+",,"+paymentamount+",,"+totalvalue);
                Intent i = new Intent(SummaryMultiply.this,SummarySubtract.class);
                i.putExtra("userId", id);
                i.putExtra("job_name",name);
                i.putExtra("job_category", category);
                i.putExtra("job_decription", description);
                i.putExtra("job_date", date);
                i.putExtra("job_start_date", start_time);
                i.putExtra("expected_hours", expectedhour);
                i.putExtra("payment_amount", paymentamount);
                i.putExtra("payment_type", type);
                i.putExtra("location", current_location);
                i.putExtra("post_address", post_address);
                i.putExtra("latitude",latitude);
                i.putExtra("longitude", longitude);
                i.putExtra("estimated_payment",estimated_amount);
                i.putExtra("flexible", flexible_status);
                i.putExtra("job_expire", job_expire);
                startActivity(i);
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

                pay_amount.setTextKeepState(cashAmountBuilder.toString());
                Selection.setSelection(pay_amount.getText(), cashAmountBuilder.toString().length());

                pay_amount.addTextChangedListener(this);
            }

           String new_pay_amount = pay_amount.getText().toString();
            System.out.println("sssssssssssss::new_pay_amount:"+new_pay_amount);
            String new_hours = hours.getText().toString();
            String job_estimated = String.valueOf(Float.valueOf(new_pay_amount)*Float.valueOf(new_hours));
            System.out.println("sssssssssssss:job_estimated:multiply:"+job_estimated);
            total.setText(job_estimated);
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

                hours.removeTextChangedListener(this);
                hours.setText(cashAmountBuilder.toString());

                hours.setTextKeepState(cashAmountBuilder.toString());
                Selection.setSelection(hours.getText(), cashAmountBuilder.toString().length());

                hours.addTextChangedListener(this);
            }
            String new_hours = hours.getText().toString();
            System.out.println("sssssssssssss::new_hours:"+new_hours);
            String new_amount = pay_amount.getText().toString();
            String estimated = String.valueOf(Float.valueOf(new_hours)*Float.valueOf(new_amount));
            System.out.println("sssssssssssss:estimated:multiply:"+estimated);
            total.setText(estimated);
        }
    };

    public boolean dispatchTouchEvent(MotionEvent event){

        swipe.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }
}
