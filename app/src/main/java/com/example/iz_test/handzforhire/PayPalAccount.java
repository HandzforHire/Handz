package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class PayPalAccount extends Activity{

    EditText email,password;
    Button save;
    TextView signup;
    ImageView h_logo;

    String user_id;
    String address,city,state,zipcode;
    RelativeLayout layout;

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
        System.out.println("uuuuuuuuuuuuser:id::"+user_id);

        h_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PayPalAccount.this, EditUserProfile.class);
                i.putExtra("userId",user_id);
                i.putExtra("address",address);
                i.putExtra("city",city);
                i.putExtra("state",state);
                i.putExtra("zipcode",zipcode);
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

       /* email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setHint("");
            }
        });
        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                password.setHint("");
            }
        });*/
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PayPalAccount.this,IntegrationPaypal.class);
                startActivity(i);
                finish();
            }
        });

    }

}
