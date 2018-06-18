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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ActiveJobAdapter extends BaseAdapter {

        private Activity activity;
        private ArrayList<HashMap<String, String>> data;
        private static LayoutInflater inflater = null;
        public ActiveJobAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
                vi = inflater.inflate(R.layout.activejobs_list, null);

            TextView job_name = (TextView) vi.findViewById(R.id.text1);
            ImageView image = (ImageView)vi.findViewById(R.id.img2);
            ImageView image1 = (ImageView) vi.findViewById(R.id.img1);
            TextView make_payment = (TextView) vi.findViewById(R.id.payment);
            final TextView profile_name = (TextView) vi.findViewById(R.id.text3);
            Button job_details = (Button) vi.findViewById(R.id.btn);
            LinearLayout chat = (LinearLayout) vi.findViewById(R.id.lay1);
            final TextView job_id = (TextView) vi.findViewById(R.id.job_id);
            final TextView employer_id = (TextView) vi.findViewById(R.id.employer_id);
            final TextView employee_id = (TextView) vi.findViewById(R.id.employee_id);
            final TextView image_text = (TextView) vi.findViewById(R.id.image1);

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
            final String get_user = items.get("user");
            System.out.println("iiiiiiiiiiiiiiiiiiid:get_user::" + get_user);
            final String user_id = items.get("userId");
            System.out.println("iiiiiiiiiiiiiiiiiiid:userId::" + user_id);
            final String get_jobid = items.get("jobId");
            System.out.println("iiiiiiiiiiiiiiiiiiid:get_jobid::" + get_jobid);
            final String get_employer = items.get("employer");
            System.out.println("iiiiiiiiiiiiiiiiiiid:get_employer::" + get_employer);
            final String get_employee = items.get("employee");
            System.out.println("iiiiiiiiiiiiiiiiiiid:get_employee::" + get_employee);
            final String channel_id=items.get("channel");
            System.out.println("iiiiiiiiiiiiiii:get_channel_id"+channel_id);

            job_name.setText(get_name);
            job_name.setTypeface(font);
            job_id.setText(get_jobid);
            employer_id.setText(get_employer);
            employee_id.setText(get_employee);
            image_text.setText(get_image);

            if(get_profile.equals(""))
            {
                profile_name.setText(get_user);
                profile_name.setTypeface(font1);
            }
            else
            {
                profile_name.setText(get_profile);
                profile_name.setTypeface(font1);

            }

            make_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity,MakePayment.class);
                    i.putExtra("job_id",get_jobid);
                    i.putExtra("userId",user_id);
                    i.putExtra("job_name",get_name);
                    i.putExtra("image",get_image);
                    i.putExtra("profilename",get_profile);
                    i.putExtra("username",get_user);
                    v.getContext().startActivity(i);
                }
            });

            chat.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                   /* String jobId = job_id.getText().toString();
                    System.out.println("jjjjjjjjjjjj:jobhistory:jobid::"+jobId);
                    Intent i = new Intent(activity,ChatNeed.class);
                    i.putExtra("jobId",jobId);
                    i.putExtra("channel",channel_id);
                    i.putExtra("username",get_user);
                    view.getContext().startActivity(i);*/
                    int pos= (int) view.getTag();
                    HashMap<String, String> items =data.get(pos);
                    String username="";
                    String  jobId =  items.get("jobId");;
                    String channel_id=items.get("channel");
                    if(items.get("profile").isEmpty())
                        username=items.get("user");
                    else
                        username= items.get("profile");;
                   String  userId=items.get("userId");


                    Intent i = new Intent(activity,ChatNeed.class);
                    i.putExtra("jobId",jobId);
                    i.putExtra("channel",channel_id);
                    i.putExtra("username",username);
                    i.putExtra("userId",userId);
                    view.getContext().startActivity(i);
                }
            });

            job_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(activity,JobDetails.class);
                    i.putExtra("jobId",get_jobid);
                    i.putExtra("userId",user_id);
                    v.getContext().startActivity(i);
                }
            });

            System.out.println("iiiiiiiiiiiiiiiiiiid:get_image11::" + get_image);

            if(get_image.equals(""))
            {
                image1.setVisibility(View.VISIBLE);
                System.out.println("iiiiiiiiiiiiiiiiiiid:get_image22::" + get_image);
            }
            else {
                System.out.println("iiiiiiiiiiiiiiiiiiid:get_image33::" + get_image);

                URL url = null;
                try {
                    url = new URL(get_image);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Bitmap bmp = null;
                try {
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    if(bmp == null)
                    {
                        Log.e("ERR","Failed to decode resource");
                        System.out.println("iiiiiiiiiiiiiiiiiiid:Failed to decode resource::" + get_image);
                        return null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                bmp = addBorderToBitmap(bmp, 10, Color.BLACK);
                bmp = addBorderToBitmap(bmp, 3, Color.BLACK);
                image1.setVisibility(View.INVISIBLE);
                image.setImageBitmap(bmp);

               /* URL url = null;
                try {
                    url = new URL(get_image);
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
                image1.setVisibility(View.INVISIBLE);
                image.setImageBitmap(bmp);*/
            }
     return  vi;
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