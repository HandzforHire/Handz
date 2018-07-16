package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.pwittchen.swipe.library.rx2.SimpleSwipeListener;
import com.github.pwittchen.swipe.library.rx2.Swipe;

public class LendPaypalAccount extends Activity {

    EditText email, password;
    Button save;
    TextView signup;
    ImageView h_logo;

    String user_id;
    String address, city, state, zipcode;
    RelativeLayout layout;
    Swipe swipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paypal_account);


        email = (EditText) findViewById(R.id.pay_email);
        password = (EditText) findViewById(R.id.pay_pass);
        save = (Button) findViewById(R.id.pay_save);
        signup = (TextView) findViewById(R.id.pay_signup);
        h_logo = (ImageView) findViewById(R.id.logo);
        layout = (RelativeLayout) findViewById(R.id.layout);

        Intent i = getIntent();
        user_id = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");
        System.out.println("uuuuuuuuuuuuser:id::" + user_id);

        h_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LendPaypalAccount.this, LendEditUserProfile.class);
                i.putExtra("userId", user_id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
                finish();
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LendPaypalAccount.this, IntegrationPaypal.class);
                startActivity(i);
                finish();
            }
        });
        swipe = new Swipe();
        swipe.setListener(new SimpleSwipeListener() {
            @Override
            public boolean onSwipedLeft(MotionEvent event) {
                Intent i = new Intent(LendPaypalAccount.this,LendProfilePage.class);
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
                Intent j = new Intent(LendPaypalAccount.this, SwitchingSide.class);
                startActivity(j);
                finish();
                return super.onSwipedRight(event);
            }
        });

    }
    public boolean dispatchTouchEvent(MotionEvent event){

        swipe.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }
}