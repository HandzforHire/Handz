package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PayEmployee extends Activity  implements SimpleGestureFilter.SimpleGestureListener{

    ImageView logo,image;
    Button next_button;
    EditText payment_amount,tip,payment_method;
    String job_id,employer_id,employee_id,job_name,profile_image;
    ProgressDialog progress_dialog;
    TextView name,date,total;
    String get_tip,get_amount,get_method,get_date,get_total;
    Integer total_value;
    Dialog dialog;
    private SimpleGestureFilter detector;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_employee);

        dialog = new Dialog(PayEmployee.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        logo = (ImageView)findViewById(R.id.logo);
        image = (ImageView)findViewById(R.id.imageView);
        payment_amount = (EditText) findViewById(R.id.pay_amount);
        tip = (EditText) findViewById(R.id.tip);
        payment_method = (EditText) findViewById(R.id.pay_method);
        next_button = (Button) findViewById(R.id.next1);
        name = (TextView) findViewById(R.id.text);
        date = (TextView) findViewById(R.id.txt2);
        total = (TextView) findViewById(R.id.total);

        Intent i = getIntent();
        job_id = i.getStringExtra("jobId");
        employer_id = i.getStringExtra("employerId");
        employee_id = i.getStringExtra("employeeId");
        job_name = i.getStringExtra("jobname");

        detector = new SimpleGestureFilter(this,this);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("MM-dd-yyyy");
        String strDate = mdformat.format(calendar.getTime());
        System.out.println("DDDDDDDd:date::"+strDate);
        date.setText(strDate);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PayEmployee.this,ProfilePage.class);
                i.putExtra("userId",employer_id);
                startActivity(i);
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_tip = tip.getText().toString().trim();
                get_amount = payment_amount.getText().toString().trim();
                get_method = payment_method.getText().toString().trim();
                get_date = date.getText().toString().trim();
                total_value = Integer.parseInt(get_amount)+Integer.parseInt(get_tip);
                get_total = String.valueOf(total_value);
                total.setText(get_total);
                Intent i = new Intent(PayEmployee.this,PayEmployee1.class);
                i.putExtra("jobId",job_id);
                i.putExtra("employerId",employer_id);
                i.putExtra("employeeId",employee_id);
                i.putExtra("jobName",job_name);
                i.putExtra("tip",get_tip);
                i.putExtra("paymentMethod",get_method);
                i.putExtra("payment_amount",get_tip);
                i.putExtra("transaction_date",get_date);
                startActivity(i);
            }
        });
    }

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
