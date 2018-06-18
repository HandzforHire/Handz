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
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class NeedRating extends Activity{

    Button nxt;
    private RatingBar rb1,rb2,rb3,rb4,rb5;
    TextView ra,pname;
    float average;
    String job_id,employer_id,employee_id,user_id,image,profilename;
    String category1,category2,category3,category4,category5;
   ImageView profile,default_image;
    RelativeLayout rating_lay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.need_rating);

        nxt = (Button) findViewById(R.id.next);
        rb1 = (RatingBar) findViewById(R.id.ratingBar1);
        rb2 = (RatingBar) findViewById(R.id.ratingBar2);
        rb3 = (RatingBar) findViewById(R.id.ratingBar3);
        rb4 = (RatingBar) findViewById(R.id.ratingBar4);
        rb5 = (RatingBar) findViewById(R.id.ratingBar5);
        ra = (TextView) findViewById(R.id.text3);
        pname=(TextView)findViewById(R.id.text1);
        profile = (ImageView) findViewById(R.id.profile_image);
        default_image = (ImageView) findViewById(R.id.default_image);
        rating_lay = (RelativeLayout) findViewById(R.id.rating);

        Intent i = getIntent();
        job_id = i.getStringExtra("jobId");
        user_id = i.getStringExtra("user_id");
        employer_id = i.getStringExtra("employer_id");
        employee_id = i.getStringExtra("employee_id");
        image = i.getStringExtra("image");
        profilename = i.getStringExtra("profilename");
        pname.setText(profilename);
        System.out.println("jjjjjjjjjjjj:rating:jobid::"+job_id+".."+employer_id+".."+employee_id+profilename);
        System.out.println("jjjjjjjjjjjj:rating:image::"+image);

        if(image.equals(""))
        {
            default_image.setVisibility(View.VISIBLE);
        }
        else {
            URL url = null;
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
            profile.setImageBitmap(bmp);
        }

        nxt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                category1 = String.valueOf(rb1.getRating());
                System.out.println("rrrrrrrrrrrr:category1::" + category1);
                category2 = String.valueOf(rb2.getRating());
                System.out.println("rrrrrrrrrrrr::category2::" + category2);
                category3 = String.valueOf(rb3.getRating());
                System.out.println("rrrrrrrrrrrr:category3::" + category3);
                category4 = String.valueOf(rb4.getRating());
                System.out.println("rrrrrrrrrrrr::category4::" + category4);
                category5 = String.valueOf(rb5.getRating());
                System.out.println("rrrrrrrrrrrr:category5::" + category5);
                float total = 0;
                total += rb1.getRating();
                total += rb2.getRating();
                total += rb3.getRating();
                total += rb4.getRating();
                total += rb5.getRating();
                float average = total / 5;
                average = Math.round(average);
                ra.setText(String.valueOf(average));
                System.out.println("rrrrrrrrrrrr" + average);
                TextView ra = (TextView) findViewById(R.id.text3);
                String rating = ra.getText().toString();

                Intent i = new Intent(NeedRating.this, NeedComments.class);
                i.putExtra("rating", rating);
                i.putExtra("userId", user_id);
                i.putExtra("jobId", job_id);
                i.putExtra("image",image);
                i.putExtra("employerId", employer_id);
                i.putExtra("category1",category1);
                i.putExtra("category2",category2);
                i.putExtra("category3",category3);
                i.putExtra("category4",category4);
                i.putExtra("category5",category5);
                i.putExtra("employeeId", employee_id);
                i.putExtra("name", profilename);
                startActivity(i);

            }
        });

        rating_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NeedRating.this,ReviewRating.class);
                i.putExtra("userId", user_id);
                i.putExtra("image",image);
                i.putExtra("name", profilename);
                startActivity(i);
            }
        });

        rb1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating1,
                                        boolean fromUser) {


            }
        });
        rb2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating2,
                                        boolean fromUser) {


            }
        });
        rb3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating3,
                                        boolean fromUser) {


            }
        });
        rb4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating4,
                                        boolean fromUser) {


            }
        });
        rb5.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()

        {
            public void onRatingChanged(RatingBar ratingBar, float rating5,
                                        boolean fromUser) {


            }
        });

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
