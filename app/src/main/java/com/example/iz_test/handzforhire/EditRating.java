package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Intent;






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





public class EditRating extends Activity implements SimpleGestureFilter.SimpleGestureListener{

    Button nxt;
    private RatingBar rb1,rb2,rb3,rb4,rb5;
    TextView ra,pname;

    String job_id,employer_id,employee_id,user_id,image,profilename,username;
    String category1,category2,category3,category4,category5;
    String cat1,cat2,cat3,cat4,cat5,rating_id;
    ImageView profile_image;
    RelativeLayout rating_lay;
    private SimpleGestureFilter detector;

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
        username = i.getStringExtra("username");
        cat1 = i.getStringExtra("cat1");
        cat2 = i.getStringExtra("cat2");
        cat3 = i.getStringExtra("cat3");
        cat4 = i.getStringExtra("cat4");
        cat5 = i.getStringExtra("cat5");
        rating_id = i.getStringExtra("ratingId");

        System.out.println("on edit rating");

      //  pname.setText(profilename);

        if(profilename.equals(""))
        {
            pname.setText(username);
        }
        else {
            pname.setText(profilename);
        }
        detector = new SimpleGestureFilter(this,this);

        rb1.setNumStars(5);
        rb1.setMax(5);
        rb1.setStepSize(0.1f);
        rb2.setStepSize(0.1f);
        rb3.setStepSize(0.1f);
        rb4.setStepSize(0.1f);
        rb5.setStepSize(0.1f);
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

                System.out.println("on edit rating next btn click");
                category1 = String.valueOf(rb1.getRating());
                category2 = String.valueOf(rb2.getRating());
                category3 = String.valueOf(rb3.getRating());
                category4 = String.valueOf(rb4.getRating());
                category5 = String.valueOf(rb5.getRating());

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

                String rating = ra.getText().toString();

                Intent i = new Intent(EditRating.this, EditComments.class);
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
                i.putExtra("ratingId", rating_id);
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
                category1 = String.valueOf(rb1.getRating());
                category2 = String.valueOf(rb2.getRating());
                category3 = String.valueOf(rb3.getRating());
                category4 = String.valueOf(rb4.getRating());
                category5 = String.valueOf(rb5.getRating());
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
            }
        });
        rb2.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating2,
                                        boolean fromUser) {
                category1 = String.valueOf(rb1.getRating());
                category2 = String.valueOf(rb2.getRating());
                category3 = String.valueOf(rb3.getRating());
                category4 = String.valueOf(rb4.getRating());
                category5 = String.valueOf(rb5.getRating());
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
            }
        });
        rb3.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating3,
                                        boolean fromUser) {
                category1 = String.valueOf(rb1.getRating());
                category2 = String.valueOf(rb2.getRating());
                category3 = String.valueOf(rb3.getRating());
                category4 = String.valueOf(rb4.getRating());
                category5 = String.valueOf(rb5.getRating());
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
            }
        });
        rb4.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating4,
                                        boolean fromUser) {
                category1 = String.valueOf(rb1.getRating());
                category2 = String.valueOf(rb2.getRating());
                category3 = String.valueOf(rb3.getRating());
                category4 = String.valueOf(rb4.getRating());
                category5 = String.valueOf(rb5.getRating());
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
            }
        });
        rb5.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener()

        {
            public void onRatingChanged(RatingBar ratingBar, float rating5,
                                        boolean fromUser) {
                category1 = String.valueOf(rb1.getRating());
                category2 = String.valueOf(rb2.getRating());
                category3 = String.valueOf(rb3.getRating());
                category4 = String.valueOf(rb4.getRating());
                category5 = String.valueOf(rb5.getRating());
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
