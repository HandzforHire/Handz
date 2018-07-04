package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class LendHistoryAdapter extends BaseAdapter{

        private Activity activity;
        private ArrayList<HashMap<String, String>> data;
        private static LayoutInflater inflater = null;
    String jobId;
        public LendHistoryAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
            activity = a;
            data = d;
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return data.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            if (convertView == null)
                vi = inflater.inflate(R.layout.lend_history_list, null);

            TextView job_name = (TextView) vi.findViewById(R.id.name);
            TextView amount = (TextView) vi.findViewById(R.id.amount);
            ImageView image1 = (ImageView) vi.findViewById(R.id.img1);
            Button job_details = (Button) vi.findViewById(R.id.btn);
            LinearLayout leave_rating = (LinearLayout) vi.findViewById(R.id.lay2);
            TextView date = (TextView) vi.findViewById(R.id.date);
            TextView month = (TextView) vi.findViewById(R.id.month);
            TextView year = (TextView) vi.findViewById(R.id.year);
            final TextView job_id = (TextView) vi.findViewById(R.id.job_id);
            final TextView employer_id = (TextView) vi.findViewById(R.id.employer_id);
            final TextView employee_id = (TextView) vi.findViewById(R.id.employee_id);
            LinearLayout chat = (LinearLayout) vi.findViewById(R.id.lay1);

            String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
            Typeface font = Typeface.createFromAsset(activity.getAssets(), fontPath);
            String fontPath1 = "fonts/calibri.ttf";
            Typeface font1 = Typeface.createFromAsset(activity.getAssets(), fontPath1);

            HashMap<String, String> items = new HashMap<String, String>();
            items = data.get(position);
            final String get_name = items.get("name");
            System.out.println("iiiiiiiiiiiiiiiiiiid:get_name::" + get_name);
            final String get_image = items.get("image");
            System.out.println("iiiiiiiiiiiiiiiiiiid:get_image::" + get_image);
            final String get_profile = items.get("profile");
            System.out.println("iiiiiiiiiiiiiiiiiiid:get_profile::" + get_profile);
            final String get_amount = items.get("payment");
            System.out.println("iiiiiiiiiiiiiiiiiiid:get_amount::" + get_amount);
            final String get_id = items.get("user_id");
            System.out.println("iiiiiiiiiiiiiiiiiiid:get_id::" + get_id);
            final String get_jobid = items.get("jobId");
            System.out.println("iiiiiiiiiiiiiiiiiiid:get_jobid::" + get_jobid);
            final String get_employer = items.get("employer");
            System.out.println("iiiiiiiiiiiiiiiiiiid:get_employer::" + get_employer);
            final String get_employee = items.get("employee");
            System.out.println("iiiiiiiiiiiiiiiiiiid:get_employee::" + get_employee);
            final String channel_id=items.get("channel");
            System.out.println("iiiiiiiiiiiiiii:channel_Id"+channel_id);

            job_name.setText(get_name);
            job_name.setTypeface(font);
            amount.setText(get_amount);
            job_id.setText(get_jobid);
            employer_id.setText(get_employer);
            employee_id.setText(get_employee);

            leave_rating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    jobId = job_id.getText().toString();
                    System.out.println("jjjjjjjjjjjj:jobhistory:jobid::"+jobId);
                    String employerId = employer_id.getText().toString();
                    System.out.println("jjjjjjjjjjjj:jobhistory:employerId::"+employerId);
                    String employeeId = employee_id.getText().toString();
                    System.out.println("jjjjjjjjjjjj:jobhistory:employeeId::"+employeeId);
                    Intent intent = new Intent(activity, LendRating.class);
                    intent.putExtra("jobId",jobId);
                    intent.putExtra("employer_id",employerId);
                    intent.putExtra("employee_id",employeeId);
                    intent.putExtra("user_id",get_id);
                    intent.putExtra("profilename",get_profile);
                    v.getContext().startActivity(intent);
                }
            });

            chat.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    jobId = job_id.getText().toString();
                    System.out.println("jjjjjjjjjjjj:jobhistory:jobid::"+jobId);
                    Intent i = new Intent(activity,ChatNeed.class);
                    i.putExtra("jobId",jobId);
                    i.putExtra("channel",channel_id);
                    i.putExtra("username",get_name);
                    i.putExtra("userId",get_id);
                    view.getContext().startActivity(i);
                }
            });

            job_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String jobId = job_id.getText().toString();
                    System.out.println("jjjjjjjjjjjj:jobhistory:jobid::"+jobId);
                    Intent i = new Intent(activity,JobDetails.class);
                    i.putExtra("jobId",jobId);
                    i.putExtra("userId",get_id);
                    v.getContext().startActivity(i);
                }
            });

            if(get_image.equals(""))
            {

            }
            else {
                Glide.with(activity).load(get_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(activity,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image1);

            }


            return vi;
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