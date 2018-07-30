package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.pwittchen.swipe.library.rx2.SimpleSwipeListener;
import com.github.pwittchen.swipe.library.rx2.Swipe;
import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import okio.Timeout;

public class ProfilePage extends Activity  {

    TextView profile_name,rating,rating_value;
    TextView txt_postedjobcnt,txt_activejobscnt,job_historycnt;
    private static final String USERNAME_URL = Constant.SERVER_URL+"get_username";
    private static final String GET_URL = Constant.SERVER_URL+"get_profile_image";
    private static final String PAYMENT_URL = Constant.SERVER_URL+"check_if_payment_mode";
    String id,user_name,email,employer_rating,posted_notification,pending_notification,active_notification,jobhistory_notification;
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    Button create,edit,need_help;
    String address,city,state,zipcode,profile_image,profilename,type;
    ImageView profile,logo,menu,share_need;
    ProgressDialog progress_dialog;
    ProgressBar progress;
    RelativeLayout rating_lay;
    SessionManager session;
    LinearLayout posted,history,active;
    ProgressBar pb;
    Dialog dialog;

    Activity activity;
    String description="https://www.handzforhire.com";
    String tittle="Whether you need a hand or would like to lend a hand, Handz for Hire is built to connect you and your neighbors looking to get jobs done. Visit HandzForHire.com or download the app in the App Store or Google Play.\"\n" +
            "along with that website url and logo";
    Swipe swipe;

    float x1,x2;
    float y1, y2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        dialog = new Dialog(ProfilePage.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        swipe = new Swipe();

        Button edit_profile = (Button) findViewById(R.id.edit_user_profile);
        rating_lay = (RelativeLayout) findViewById(R.id.rating);
        profile_name = (TextView) findViewById(R.id.text1);
        rating_value = (TextView) findViewById(R.id.text3);
        need_help = (Button) findViewById(R.id.need_help);
        create = (Button) findViewById(R.id.create_job);
        edit = (Button) findViewById(R.id.edit_posted_job);
        posted = (LinearLayout) findViewById(R.id.posted_job);
        active = (LinearLayout) findViewById(R.id.active_job);
        history = (LinearLayout) findViewById(R.id.job_history);
        logo = (ImageView)findViewById(R.id.logo);
        profile = (ImageView)findViewById(R.id.profile_image);
        menu = (ImageView)findViewById(R.id.menu);
        rating = (TextView) findViewById(R.id.text2);
        txt_postedjobcnt = (TextView) findViewById(R.id.txt_postedjobcnt);
        txt_activejobscnt = (TextView) findViewById(R.id.txt_activejobscnt);
        job_historycnt = (TextView) findViewById(R.id.job_historycnt);


        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // get name
        user_name = user.get(SessionManager.KEY_USERNAME);
        id = user.get(SessionManager.KEY_ID);
        email = user.get(SessionManager.KEY_EMAIL);
        address = user.get(SessionManager.KEY_ADDRESS);
        city = user.get(SessionManager.KEY_CITY);
        state = user.get(SessionManager.KEY_STATE);
        zipcode = user.get(SessionManager.KEY_ZIPCODE);
        type = user.get(SessionManager.TYPE);

        Profilevalues.user_id=id;
        Profilevalues.email=email;
        Profilevalues.address=address;
        Profilevalues.city=city;
        Profilevalues.state=state;
        Profilevalues.zipcode=zipcode;


        share_need=(ImageView)findViewById(R.id.sha_need);
        share_need.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                share();


            }
        });


        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        create.setTypeface(tf);
        edit.setTypeface(tf);
        edit_profile.setTypeface(tf);
        need_help.setTypeface(tf);

        String fontPath1 = "fonts/LibreFranklin-SemiBoldItalic.ttf";
        Typeface tf1 = Typeface.createFromAsset(getAssets(), fontPath1);
        rating.setTypeface(tf1);

        String fontPath2 = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
        profile_name.setTypeface(tf2);

        //paymentCheck();
        getProfileimage();
        getUsername();

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfilePage.this, EditUserProfile.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
                finish();
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this,EditPostedJobs.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
                finish();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this,SwitchingSide.class);
                startActivity(i);
            }
        });

        posted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this,PostedJobs.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
                finish();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this, CreateJob.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this,JobHistory.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

        rating_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this,ReviewRating.class);
                i.putExtra("userId", id);
                i.putExtra("image",profile_image);
                i.putExtra("name", profilename);
                startActivity(i);
            }
        });

        need_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this,NeedHelp.class);
                i.putExtra("userId", id);
                i.putExtra("email",email);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilePage.this,ActiveJobs.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
            }
        });

/*        swipe.setListener(new SimpleSwipeListener() {

            @Override
            public boolean onSwipedLeft(MotionEvent event) {

                Intent i = new Intent(ProfilePage.this, ProfilePage.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
                finish();

                return super.onSwipedLeft(event);
            }

            @Override
            public boolean onSwipedRight(MotionEvent event) {
                Intent j = new Intent(ProfilePage.this, SwitchingSide.class);
                startActivity(j);
                finish();
                return super.onSwipedRight(event);
            }
        });*/
    }

    public void paymentCheck()
    {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PAYMENT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("cccccccccccccheck:payment:" + response);
                        onResponserecieved3(response, 3);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        try {
                            String responseBody = new String( error.networkResponse.data, "utf-8" );
                            JSONObject jsonObject = new JSONObject( responseBody );
                            System.out.println("cccccccccccccheck:payment:error" + jsonObject);
                            String status = jsonObject.getString("status");
                            if(status.equals("error"))
                            {
                                create.setEnabled(false);
                                create.setAlpha(0.65F);
                                edit.setEnabled(false);
                                edit.setAlpha(0.65F);
                                posted.setEnabled(false);
                                posted.setAlpha(0.65F);
                                active.setEnabled(false);
                                active.setAlpha(0.65F);
                                history.setEnabled(false);
                                history.setAlpha(0.65F);
                            }
                        } catch ( JSONException e ) {
                            //Handle a malformed json response
                        } catch (UnsupportedEncodingException error1){

                        }
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

    public void onResponserecieved3(String jsonobject, int requesttype) {
        String status = null;

        String user_name = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");

            if(status.equals("success"))
            {
                create.setEnabled(true);
                edit.setEnabled(true);
                posted.setEnabled(true);
                active.setEnabled(true);
                history.setEnabled(true);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void getProfileimage()
    {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ggggggggget:profile:" + response);
                        onResponserecieved2(response, 2);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();

                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(),"Not Connected",Toast.LENGTH_LONG).show();
                        }else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),"Authentication Failure while performing the request",Toast.LENGTH_LONG).show();
                        }else if (error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(),"Server responded with a error response",Toast.LENGTH_LONG).show();
                        }else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"Network error while performing the request",Toast.LENGTH_LONG).show();
                        }else {
                            try {

                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                String status = jsonObject.getString("msg");
                                if (!status.equals("")) {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(ProfilePage.this);
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
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    return;
                                } else {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(ProfilePage.this);
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
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    return;
                                }
                            } catch (JSONException e) {
                                //Handle a malformed json response
                            } catch (UnsupportedEncodingException error1) {

                            }
                        }
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
                employer_rating = jResult.getString("employer_rating");
                posted_notification = jResult.getString("notificationCountPosted");
                pending_notification = jResult.getString("notificationCountPending");
                active_notification = jResult.getString("notificationCountActive");
                System.out.println("resssssss:employer_rating:" + active_notification);
                jobhistory_notification = jResult.getString("notificationCountJobHistory");
                System.out.println("resssssss:employer_rating:" + jobhistory_notification);
                rating_value.setText(employer_rating);
                if(!posted_notification.equals("0"))
                {
                    txt_postedjobcnt.setText(posted_notification);
                    txt_postedjobcnt.setVisibility(View.VISIBLE);
                }else{
                    txt_postedjobcnt.setVisibility(View.INVISIBLE);
                }
                if(!active_notification.equals("0"))
                {
                    txt_activejobscnt.setText(active_notification);
                    txt_activejobscnt.setVisibility(View.VISIBLE);
                 }else{
                    txt_activejobscnt.setVisibility(View.INVISIBLE);
                }
                if(!jobhistory_notification.equals("0"))
                {
                    job_historycnt.setText(jobhistory_notification);
                    job_historycnt.setVisibility(View.VISIBLE);
                }else{
                    job_historycnt.setVisibility(View.INVISIBLE);
                }
                if(!profile_image.equals("")&&!profilename.equals("null"))
                {
                    profile_name.setText(profilename);
                    Glide.with(this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(profile);

                }
                else if(!profile_image.equals("")&&profilename.equals("null"))
                {

                    Glide.with(this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(profile);
                }
                else if(!profilename.equals("null")&&profile_image.equals(""))
                {
                    profile_name.setText(profilename);
                }
                else if(profilename.equals("null")&&profile_image.equals(""))
                {

                }
                else
                {
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } /*catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    public static Bitmap rotateImageIfRequired(Bitmap img, Context context, Uri selectedImage) throws IOException {

        if (selectedImage.getScheme().equals("content")) {
            String[] projection = {MediaStore.Images.ImageColumns.ORIENTATION};
            Cursor c = context.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c.moveToFirst()) {
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {
            ExifInterface ei = new ExifInterface(selectedImage.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            //Timber.d("orientation: %s", orientation);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }


    public void getUsername() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USERNAME_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:" + response);
                        onResponserecieved1(response, 2);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(),"Not Connected",Toast.LENGTH_LONG).show();
                        }else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(),"Authentication Failure while performing the request",Toast.LENGTH_LONG).show();
                        }else if (error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(),"Server responded with a error response",Toast.LENGTH_LONG).show();
                        }else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"Network error while performing the request",Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                System.out.println("eeeeeeeeeeeeeeeror:" + jsonObject);

                            } catch (JSONException e) {
                                //Handle a malformed json response
                            } catch (UnsupportedEncodingException error1) {

                            }
                        }
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

    public void onResponserecieved1(String jsonobject, int requesttype) {
        String status = null;

        String employer_rating = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");

            if(status.equals("success"))
            {
                //profile_name.setText(user_name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

   /* @Override
    public boolean dispatchTouchEvent(MotionEvent event){

        swipe.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }
*/

/*
    @Override
    public void onSwipe(int direction) {
        String str = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT:

                Intent i = new Intent(ProfilePage.this, ProfilePage.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
                finish();
                break;

            case SimpleGestureFilter.SWIPE_LEFT:

                Intent j = new Intent(ProfilePage.this, SwitchingSide.class);
                startActivity(j);
                finish();
                break;
        }

    }

    @Override
    public void onDoubleTap() {

    }
*/


    private void share()
    {

        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT,"HandzForHire");
        share.putExtra(Intent.EXTRA_TEXT,tittle);
        startActivity(Intent.createChooser(share, "Share link!"));

    }


    protected Bitmap addBorderToBitmap(Bitmap srcBitmap, int borderWidth, int borderColor){
        // Initialize a new Bitmap to make it bordered bitmap
        Bitmap dstBitmap = Bitmap.createBitmap(
                srcBitmap.getWidth() + borderWidth*2, // Width
                srcBitmap.getHeight() + borderWidth*2, // Height
                Bitmap.Config.ARGB_8888 // Config
        );
        Canvas canvas = new Canvas(dstBitmap);

        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setAntiAlias(true);
        Rect rect = new Rect(
                borderWidth / 2,
                borderWidth / 2,
                canvas.getWidth() - borderWidth / 2,
                canvas.getHeight() - borderWidth / 2
        );
        canvas.drawRect(rect,paint);
        canvas.drawBitmap(srcBitmap, borderWidth, borderWidth, null);
        srcBitmap.recycle();

        // Return the bordered circular bitmap
        return dstBitmap;
    }



    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN:
            {
                x1 = touchevent.getX();
                y1 = touchevent.getY();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                x2 = touchevent.getX();
                y2 = touchevent.getY();

                if (x1 < x2)
                {
                    Toast.makeText(this, "Left to Right Swap Performed", Toast.LENGTH_LONG).show();
                }

                // if right to left sweep event on screen
                if (x1 > x2)
                {
                    Toast.makeText(this, "Right to Left Swap Performed", Toast.LENGTH_LONG).show();
                }

                // if UP to Down sweep event on screen
                if (y1 < y2)
                {
                    Toast.makeText(this, "UP to Down Swap Performed", Toast.LENGTH_LONG).show();
                }
                if (y1 > y2)
                {
                    Toast.makeText(this, "Down to UP Swap Performed", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
        return false;
    }





}
