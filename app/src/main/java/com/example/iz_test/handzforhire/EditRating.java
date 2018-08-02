package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.Canvas;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;





public class EditRating extends Activity{

    Button nxt;
    private RatingBar rb1,rb2,rb3,rb4,rb5;
    TextView ra,pname;

    String job_id,employer_id,employee_id,user_id,image,profilename;
    String category1,category2,category3,category4,category5;
    String cat1,cat2,cat3,cat4,cat5;
    ImageView profile_image,default_image;
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

        profile_image = (ImageView) findViewById(R.id.profile_image);
        rating_lay = (RelativeLayout) findViewById(R.id.rating);

        Intent i = getIntent();
        job_id = i.getStringExtra("jobId");
        user_id = i.getStringExtra("user_id");
        employer_id = i.getStringExtra("employer_id");
        employee_id = i.getStringExtra("employee_id");
        image = i.getStringExtra("image");
        profilename = i.getStringExtra("profilename");
        cat1 = i.getStringExtra("cat1");
        cat2 = i.getStringExtra("cat2");
        cat3 = i.getStringExtra("cat3");
        cat4 = i.getStringExtra("cat4");
        cat5 = i.getStringExtra("cat5");
        //c1 = Integer.valueOf(cat1);
        pname.setText(profilename);
        System.out.println("jjjjjjjjjjjj:rating:jobid::"+job_id+".."+employer_id+".."+employee_id+profilename);
        System.out.println("jjjjjjjjjjjj:rating:image::"+image);
        System.out.println("jjjjjjjjjjjj:rating:cat::"+cat1+cat2+cat3+cat4+cat5);

        rb1.setNumStars(5);
        rb1.setMax(5);
        rb1.setStepSize(0.1f);
        rb1.setRating(Float.parseFloat(cat1));
        rb2.setRating(Float.parseFloat(cat2));
        rb3.setRating(Float.parseFloat(cat3));
        rb4.setRating(Float.parseFloat(cat4));
        rb5.setRating(Float.parseFloat(cat5));

        Glide.with(this).load(image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(profile_image);

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

                Intent i = new Intent(EditRating.this, NeedComments.class);
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
                Intent i = new Intent(EditRating.this,ReviewRating.class);
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

    /*    swipe = new Swipe();
        swipe.setListener(new SimpleSwipeListener() {

            @Override
            public boolean onSwipedLeft(MotionEvent event) {
                Intent i = new Intent(EditRating.this,ProfilePage.class);
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
                Intent j = new Intent(EditRating.this, SwitchingSide.class);
                startActivity(j);
                finish();
                return super.onSwipedRight(event);
            }
        });*/

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


    /*public boolean dispatchTouchEvent(MotionEvent event){

        swipe.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }
*/
}
