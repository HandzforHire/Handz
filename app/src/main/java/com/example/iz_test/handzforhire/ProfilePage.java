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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

public class ProfilePage extends Activity implements SimpleGestureFilter.SimpleGestureListener{

    TextView profile_name,rating,rating_value,posted_circle,active_circle,history_circle;
    private static final String USERNAME_URL = Constant.SERVER_URL+"get_username";
    private static final String GET_URL = Constant.SERVER_URL+"get_profile_image";
    private static final String PAYMENT_URL = Constant.SERVER_URL+"check_if_payment_mode";
    String id,user_name,email,employer_rating,posted_notification,pending_notification,active_notification,jobhistory_notification;
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    Button create,edit,need_help;
    private SimpleGestureFilter detector;
    String address,city,state,zipcode,profile_image,profilename,type;
    ImageView image,profile,logo,menu;
    ProgressDialog progress_dialog;
    ProgressBar progress;
    RelativeLayout rating_lay;
    SessionManager session;
    LinearLayout posted,history,active;
    ProgressBar pb;
    Dialog dialog;


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

        detector = new SimpleGestureFilter(this,this);

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
        image = (ImageView)findViewById(R.id.default_image);
        logo = (ImageView)findViewById(R.id.logo);
        profile = (ImageView)findViewById(R.id.profile_image);
        menu = (ImageView)findViewById(R.id.menu);
        rating = (TextView) findViewById(R.id.text2);
        posted_circle = (TextView) findViewById(R.id.circle1);
        active_circle = (TextView) findViewById(R.id.circle2);
        history_circle = (TextView) findViewById(R.id.circle3);


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
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ggggggggget:profile:" + response);
                        onResponserecieved2(response, 2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String( error.networkResponse.data, "utf-8" );
                            JSONObject jsonObject = new JSONObject( responseBody );
                            System.out.println("eeeeeeeeeeeeeeeror:"+jsonObject);
                            String status = jsonObject.getString("msg");
                            if(!status.equals(""))
                            {
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
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                return;
                            }
                            else
                            {
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
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                return;
                            }
                        } catch ( JSONException e ) {
                            //Handle a malformed json response
                        } catch (UnsupportedEncodingException error1){

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
                    posted_circle.setText(posted_notification);
                }
                if(!active_notification.equals("0"))
                {
                    active_circle.setText(active_notification);
                }
                if(!jobhistory_notification.equals("0"))
                {
                    history_circle.setText(jobhistory_notification);
                }
                if(!profile_image.equals("")&&!profilename.equals("null"))
                {
                    profile_name.setText(profilename);
                    /* URL url = new URL(profile_image);
                      Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    bmp = addBorderToBitmap(bmp, 10, Color.BLACK);
                    bmp = addBorderToBitmap(bmp, 3, Color.BLACK);*/
                    /* Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);*/
                   // image.setVisibility(View.INVISIBLE);
                   // profile.setImageBitmap(bmp);
                   // profile.setVisibility(View.VISIBLE);
                  /* Glide.with(ProfilePage.this).load(profile_image).apply(RequestOptions.bitmapTransform(
                           new RoundedCornersTransformation(ProfilePage.this, Glideconstants.sCorner, Glideconstants.sColor, Glideconstants.sBorder))).into(image);
                   */
                    Glide.with(this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image);
                  dialog.dismiss();

                }
                else if(!profile_image.equals("")&&profilename.equals("null"))
                {
                    /*URL url = new URL(profile_image);
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    bmp = addBorderToBitmap(bmp, 10, Color.BLACK);
                    bmp = addBorderToBitmap(bmp, 3, Color.BLACK);*/
                    /* Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);*/
                    //image.setVisibility(View.INVISIBLE);
                    //profile.setImageBitmap(bmp);
                    Glide.with(this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image);
                    // Glide.with(ProfilePage.this).load(profile_image).error(R.drawable.default_profile).into(image);
                    dialog.dismiss();
                }
                else if(!profilename.equals("null")&&profile_image.equals(""))
                {
                    profile_name.setText(profilename);
                    dialog.dismiss();
                }
                else if(profilename.equals("null")&&profile_image.equals(""))
                {
                    dialog.dismiss();
                }
                else
                {
                    dialog.dismiss();
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USERNAME_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:" + response);
                        onResponserecieved1(response, 2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String( error.networkResponse.data, "utf-8" );
                            JSONObject jsonObject = new JSONObject( responseBody );
                            System.out.println("eeeeeeeeeeeeeeeror:"+jsonObject);
                            /*String status = jsonObject.getString("msg");
                            if(!status.equals(""))
                            {
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
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                return;
                            }
                            else
                            {
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
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                return;
                            }*/
                        } catch ( JSONException e ) {
                            //Handle a malformed json response
                        } catch (UnsupportedEncodingException error1){

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){

        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

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
}
