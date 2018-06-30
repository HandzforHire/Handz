package com.example.iz_test.handzforhire;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
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

public class SwitchingSide extends Activity{

    private static final String URL = Constant.SERVER_URL+"logout";
    public static String XAPP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    public static String KEY_USERID = "user_id";
    String user_id = "70";
    SessionManager session;
    LinearLayout promo;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.switching_side);

        session = new SessionManager(getApplicationContext());
        promo = (LinearLayout) findViewById(R.id.promo_video);
        ImageView logo = (ImageView) findViewById(R.id.logo);
        Button lend_hand = (Button) findViewById(R.id.lend_hand);
        final TextView signOut = (TextView) findViewById(R.id.sign_out);

        promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SwitchingSide.this,PromoVideo.class);
                startActivity(i);
                finish();
            }
        });

        lend_hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SwitchingSide.this,MapActivity.class);
                startActivity(i);
                finish();
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SwitchingSide.this,LendProfilePage.class);
                startActivity(i);
                finish();
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.logoutUser();
                signoutmethod();
            }
        });
    }

    private void signoutmethod()
    {
        dialog = new Dialog(SwitchingSide.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:signout::" + response);
                        onResponserecieved(response, 2);
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
                map.put(KEY_USERID, user_id);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    public void onResponserecieved(String jsonobject, int i) {
        System.out.println("response from interface"+jsonobject);

        String status = null;
        String categories = null;

        try {
            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            if(status.equals("success"))
            {
                Intent intent = new Intent(SwitchingSide.this,MainActivity.class);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
