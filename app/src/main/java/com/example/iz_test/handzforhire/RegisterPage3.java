package com.example.iz_test.handzforhire;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

import com.android.volley.DefaultRetryPolicy;
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

public class RegisterPage3 extends AppCompatActivity implements ResponseListener1{

    Button next;
    EditText u_name, pass, re_pass;
    CheckBox check1, check2;
    String user_name, password, retype_password, address;
    RelativeLayout layout;
    String first, last, add1, add2, city, state, zip, email, re_email;
    private String TAG = RegisterPage3.class.getSimpleName();
    private static final String REGISTER_URL = Constant.SERVER_URL+"user_register";
    String get_email,get_address,get_city,get_state,get_zipcode,user_id,user_type,get_password;

    public static String USERNAME = "username";
    public static String PASS = "password";
    public static String EMAIL = "email";
    public static String FNAME = "firstname";
    public static String LNAME = "lastname";
    public static String ADDRESS = "address";
    public static String CITY = "city";
    public static String STATE = "state";
    public static String ZIPCODE = "zipcode";
    public static String USERTYPE = "usertype";
    public static String DEVICETOKEN = "devicetoken";
    public static String XAPP_KEY = "X-APP-KEY";
    ProgressDialog progress_dialog;
    TextView handz_condition,feature;
    private static final int REQUEST_PHONE_STATE = 0;
    String usertype = "employer";
    String value = "HandzForHire@~";
    int timeout = 60000;
    String deviceId;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page3);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        session = new SessionManager(getApplicationContext());

        next = (Button) findViewById(R.id.next1);
        check1 = (CheckBox) findViewById(R.id.checkBox1);
        check2 = (CheckBox) findViewById(R.id.checkBox2);
        u_name = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        re_pass = (EditText) findViewById(R.id.retype_password);
        layout = (RelativeLayout) findViewById(R.id.layout);
        handz_condition = (TextView) findViewById(R.id.handz_condition);
        feature = (TextView) findViewById(R.id.features);
        ImageView logo = (ImageView)findViewById(R.id.logo);

        handz_condition.setPaintFlags(handz_condition.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        feature.setPaintFlags(feature.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        deviceId = LoginActivity.deviceId;
        System.out.println("8888888:device:register:::"+deviceId);

        String fontPath = "fonts/calibri.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        check1.setTypeface(tf);
        check2.setTypeface(tf);

        String fontPath1 = "fonts/calibriItalic.ttf";
        Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
        feature.setTypeface(tf1);
        handz_condition.setTypeface(tf1);

        String fontPath2 = "fonts/calibri.ttf";
        Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
        u_name.setTypeface(tf2);
        pass.setTypeface(tf2);
        re_pass.setTypeface(tf2);

        String fontPath3 = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf3 = Typeface.createFromAsset(getAssets(), fontPath3);
        next.setTypeface(tf3);

        Intent i = getIntent();
        first = i.getStringExtra("firstname");
        System.out.println("ffffffff:" + first);
        last = i.getStringExtra("lastname");
        System.out.println("ffffffff:" + last);
        add1 = i.getStringExtra("address1");
        System.out.println("ffffffff:" + add1);
        add2 = i.getStringExtra("address2");
        System.out.println("ffffffff:" + add2);
        city = i.getStringExtra("city");
        System.out.println("ffffffff:" + city);
        state = i.getStringExtra("state");
        System.out.println("ffffffff:" + state);
        zip = i.getStringExtra("zip");
        System.out.println("ffffffff:" + zip);
        email = i.getStringExtra("email");
        System.out.println("ffffffff:" + email);
        re_email = i.getStringExtra("retype_email");
        System.out.println("ffffffff:" + re_email);

        address = add1 + add2;
        System.out.println("ffffffff:add:" + address);

       // permission();

        handz_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterPage3.this, ViewWeb.class);
                startActivity(i);
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterPage3.this,RegisterPage2.class);
                startActivity(i);
                finish();
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        /*u_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                u_name.setHint("");
            }
        });
        pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                pass.setHint("");
            }
        });
        re_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                re_pass.setHint("");
            }
        });*/

        feature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(RegisterPage3.this);
                dialog.setContentView(R.layout.popup);

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

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user_name = u_name.getText().toString().trim();
                password = pass.getText().toString().trim();
                retype_password = re_pass.getText().toString().trim();

                if (TextUtils.isEmpty(user_name)) {
                    // custom dialog
                    final Dialog dialog = new Dialog(RegisterPage3.this);
                    dialog.setContentView(R.layout.custom_dialog);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("Must Fill In \"User Name\" Box");
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
                }
                if (TextUtils.isEmpty(password)) {
                    // custom dialog
                    final Dialog dialog = new Dialog(RegisterPage3.this);
                    dialog.setContentView(R.layout.custom_dialog);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("Must Fill In \"Password\" Box");
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
                }
                if (password.length() < 8) {
                    // custom dialog
                    final Dialog dialog = new Dialog(RegisterPage3.this);
                    dialog.setContentView(R.layout.custom_dialog);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("Password is too short, Please input with 8-32 characters");
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
                }
                if (password.length() > 32) {
                    // custom dialog
                    final Dialog dialog = new Dialog(RegisterPage3.this);
                    dialog.setContentView(R.layout.custom_dialog);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("Password is too long, Please input with 8-32 characters");
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
                }
                if (TextUtils.isEmpty(retype_password)) {
                    // custom dialog
                    final Dialog dialog = new Dialog(RegisterPage3.this);
                    dialog.setContentView(R.layout.custom_dialog);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("Must Fill In \"Retype Password\" Box");
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
                }
                if (!password.equals(retype_password)) {
                    // custom dialog
                    final Dialog dialog = new Dialog(RegisterPage3.this);
                    dialog.setContentView(R.layout.custom_dialog);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("Password and Retype Password does not match");
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
                }
                if (check1.isChecked()) {
                    if (check2.isChecked()) {
                        registerUser();
                    } else {
                        // custom dialog
                        final Dialog dialog = new Dialog(RegisterPage3.this);
                        dialog.setContentView(R.layout.custom_dialog);

                        // set the custom dialog components - text, image and button
                        TextView text = (TextView) dialog.findViewById(R.id.text);
                        text.setText("Please agree to terms and conditions");
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
                } else {
                    // custom dialog
                    final Dialog dialog = new Dialog(RegisterPage3.this);
                    dialog.setContentView(R.layout.custom_dialog);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("Please provide access to camera and location");
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
            }
        });
    }

    private void registerUser()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(Registrationpage3.this,response,Toast.LENGTH_LONG).show();

                        System.out.println("eeeee:"+response);
                        onResponserecieved(response,1);
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
                            if(!status.equals(""))
                            {
                                // custom dialog
                                final Dialog dialog = new Dialog(RegisterPage3.this);
                                dialog.setContentView(R.layout.custom_dialog);

                                // set the custom dialog components - text, image and button
                                TextView text = (TextView) dialog.findViewById(R.id.text);
                                text.setText(status);
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

                                final Dialog dialog = new Dialog(RegisterPage3.this);
                                dialog.setContentView(R.layout.custom_dialog);

                                // set the custom dialog components - text, image and button
                                TextView text = (TextView) dialog.findViewById(R.id.text);
                                text.setText("Registration Failed");
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
                        /*try {
                            String responseBody = new String( error.networkResponse.data, "utf-8" );
                            JSONObject jsonObject = new JSONObject( responseBody );
                            System.out.println("eeeeeeeeeeeeror:"+jsonObject);

                        } catch ( JSONException e ) {
                            //Handle a malformed json response
                        } catch (
                                UnsupportedEncodingException error1){
                        }
*/
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();

                params.put(XAPP_KEY,value);
                params.put(USERNAME,user_name);
                params.put(PASS,password);
                params.put(EMAIL, email);
                params.put(FNAME,first);
                params.put(LNAME,last);
                params.put(ADDRESS,address);
                params.put(CITY,city);
                params.put(STATE,state);
                params.put(ZIPCODE,zip);
                params.put(USERTYPE,usertype);
                params.put(DEVICETOKEN,deviceId);
               /* params.put("X-APP-KEY","HandzForHire@~");
                params.put("username", "Parima");
                params.put("password", "Parima");
                params.put("email", "Parima@gmail.com");
                params.put("firstname","Parima");
                params.put("lastname","Velu");
                params.put("address", "Bangalore");
                params.put("city", "Bangalore");
                params.put("state", "TamilNadu");
                params.put("zipcode","600014");
                params.put("usertype","employee");
                params.put("devicetoken","352423060938733");*/
                return params;
            }

        };

        System.out.println("values::"+value+".."+user_name+".."+password+".."+email+".."+first+".."+last+".."+address+".."+city+".."+state+".."+zip+".."+usertype+".."+deviceId);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved(String jsonobject, int requesttype) {
        System.out.println("response from interface"+jsonobject);

        String status = null;
        String userdata = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");
            userdata = jResult.getString("user_data");

            if(status.equals("success"))
            {
                JSONObject object = new JSONObject(userdata);
                for(int n = 0; n < object.length(); n++)
                {
                    user_id = object.getString("id");
                    user_name = object.getString("username");
                    get_email = object.getString("email");
                    get_address = object.getString("address");
                    get_city = object.getString("city");
                    get_state = object.getString("state");
                    get_zipcode = object.getString("zipcode");
                    user_type = object.getString("usertype");
                    System.out.println("ressss:object::"+object);
                    System.out.println("ressss:iiiiid::"+user_id);
                    System.out.println("ressss::user_name:"+user_name);
                    System.out.println("ressss:email::"+get_email);
                    System.out.println("ressss:address::"+get_address);
                    System.out.println("ressss:city::"+get_city);
                    System.out.println("ressss:state::"+get_state);
                    System.out.println("ressss:zipcode::"+get_zipcode);
                }

                session.NeedLogin(get_email,get_password,user_name,usertype,user_id,get_address,get_city,get_state,get_zipcode,user_type);

                Intent i = new Intent(RegisterPage3.this,RegisterPage4.class);
               /* i.putExtra("userId",user_id);
                i.putExtra("username",user_name);
                i.putExtra("email",get_email);
                i.putExtra("address",get_address);
                i.putExtra("state",get_city);
                i.putExtra("city",get_state);
                i.putExtra("zipcode",get_zipcode);*/
                startActivity(i);
                finish();
            }
            else
            {
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

