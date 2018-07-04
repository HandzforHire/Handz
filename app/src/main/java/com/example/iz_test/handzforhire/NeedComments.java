package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class NeedComments extends Activity{

    Button b1;
    String result;
    TextView t3;
    private static final String URL = Constant.SERVER_URL+"add_rating";
    public static String KEY_JOBID = "job_id";
    public static String KEY_USERID = "user_id";
    public static String KEY_RATING = "rating";
    public static String KEY_COMMENTS = "comments";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String KEY_TYPE = "type";
    public static String KEY_USER = "login_user_id";
    public static String CATEGORY1 = "category1";
    public static String CATEGORY2 = "category2";
    public static String CATEGORY3 = "category3";
    public static String CATEGORY4 = "category4";
    public static String CATEGORY5 = "category5";
    public static String EMPLOYERID = "employer_id";
    public static String EMPLOYEEID = "employee_id";
    public static String RATING_ID = "rating_id";
    String value = "HandzForHire@~";
    String job_id, employer_id, employee_id, rating, comments,user_id,image,profilename;
    String category1,category2,category3,category4,category5;
    EditText comment;
    String type = "employee";
    ImageView profile,default_image;
    RelativeLayout rating_lay;
    TextView pn_needcmd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.need_comments);

        b1 = (Button) findViewById(R.id.next1);
        t3 = (TextView) findViewById(R.id.text3);
        comment = (EditText) findViewById(R.id.edit_text);
        profile = (ImageView) findViewById(R.id.profile_image);
        default_image = (ImageView) findViewById(R.id.default_image);
        rating_lay = (RelativeLayout) findViewById(R.id.rating);
        pn_needcmd=(TextView)findViewById(R.id.text1);

        Intent i = getIntent();
        rating = i.getStringExtra("rating");
        user_id = i.getStringExtra("userId");
        job_id = i.getStringExtra("jobId");
        employer_id = i.getStringExtra("employerId");
        employee_id = i.getStringExtra("employeeId");
        category1 = i.getStringExtra("category1");
        category2 = i.getStringExtra("category2");
        category3 = i.getStringExtra("category3");
        category4 = i.getStringExtra("category4");
        category5 = i.getStringExtra("category5");
        image = i.getStringExtra("image");
        profilename = i.getStringExtra("name");
        pn_needcmd.setText(profilename);


        t3.setText(rating);
        System.out.println("rrrrrrrrrrrr" + rating+"..."+image);

        /*System.out.println("1111111"+value);
        System.out.println("2222222"+job_id);
        System.out.println("3333333"+employer_id);
        System.out.println("0444444"+rating);
        System.out.println("5555555"+comments);
        System.out.println("6666666"+type);
        System.out.println("7777777"+employee_id);
        System.out.println("8888888"+category1);
        System.out.println("9999999"+category2);
        System.out.println("aaaaaaa"+category3);
        System.out.println("bbbbbbb"+category4);
        System.out.println("ccccccc"+category5);
        System.out.println("ddddddd"+employer_id);
        System.out.println("eeeeeee"+employee_id);
        System.out.println("fffffff"+rating);*/


        if(image.equals(""))
        {
            default_image.setVisibility(View.VISIBLE);
        }
        else {
           /* java.net.URL url = null;
            try {
                url = new URL(image);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap bmp = null;
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            bmp = addBorderToBitmap(bmp, 10, Color.BLACK);
            bmp = addBorderToBitmap(bmp, 3, Color.BLACK);
            default_image.setVisibility(View.INVISIBLE);
            profile.setImageBitmap(bmp);*/
            //Glide.with(NeedComments.this).load(image).error(R.drawable.default_profile).into(default_image);
            profile.setVisibility(View.INVISIBLE);
            Glide.with(this).load(image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(default_image);

        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comments = comment.getText().toString().trim();
                post();
            }

        });

        rating_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NeedComments.this,ReviewRating.class);
                i.putExtra("userId", user_id);
                i.putExtra("image",image);
                i.putExtra("name", profilename);
                startActivity(i);
            }
        });
    }

    private void post() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("rrrrrrrrrrrrr:addrating:::"+response);
                        onResponserecieved(response, 2);

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String( error.networkResponse.data, "utf-8" );
                            JSONObject jsonObject = new JSONObject( responseBody );
                            System.out.println("error"+jsonObject);

                        } catch (JSONException e) {

                        } catch (UnsupportedEncodingException error1) {

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(XAPP_KEY, value);
                map.put(KEY_JOBID, job_id);
                map.put(KEY_USERID, employee_id);
                map.put(KEY_RATING, rating);
                map.put(KEY_COMMENTS, comments);
                map.put(KEY_TYPE, type);
                map.put(KEY_USER,employer_id);
                map.put(CATEGORY1,category1);
                map.put(CATEGORY2,category2);
                map.put(CATEGORY3,category3);
                map.put(CATEGORY4,category4);
                map.put(CATEGORY5,category5);
                map.put(EMPLOYERID,employer_id);
                map.put(EMPLOYEEID,employee_id);
                map.put(RATING_ID,rating);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void onResponserecieved(String jsonobject, int requesttype)
    {
        String status = null;

        try {

            JSONObject jResult = new JSONObject(jsonobject);
            System.out.println("rrrr"+jResult);
            status = jResult.getString("status");

            if (status.equals("success"))
            {
                Intent intent=new Intent(NeedComments.this,ProfilePage.class);
                intent.putExtra("userId",employer_id);
                startActivity(intent);

            }

        } catch (Exception e) {
        }
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
