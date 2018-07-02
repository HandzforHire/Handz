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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class LendActiveJobAdapter extends BaseAdapter{

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    public LendActiveJobAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
            vi = inflater.inflate(R.layout.lend_active_list, null);

        TextView job_name = (TextView) vi.findViewById(R.id.job_name_text);
        TextView tv1 = (TextView) vi.findViewById(R.id.text3);
        TextView tv2 = (TextView) vi.findViewById(R.id.text4);
        TextView tv3 = (TextView) vi.findViewById(R.id.text5);
        TextView tv4 = (TextView) vi.findViewById(R.id.text7);
        TextView tv5 = (TextView) vi.findViewById(R.id.text8);
        ImageView image = (ImageView)vi.findViewById(R.id.img2);
        ImageView image1 = (ImageView) vi.findViewById(R.id.img1);
        TextView payment = (TextView) vi.findViewById(R.id.payment);
        LinearLayout message = (LinearLayout) vi.findViewById(R.id.lay1);
        TextView jobId = (TextView) vi.findViewById(R.id.text4);
        Button job_details = (Button) vi.findViewById(R.id.btn);
        final TextView job_id = (TextView) vi.findViewById(R.id.job_id);
        final TextView employer_id = (TextView) vi.findViewById(R.id.employer_id);
        final TextView employee_id = (TextView) vi.findViewById(R.id.employee_id);
        final TextView image_text = (TextView) vi.findViewById(R.id.image1);
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
        final String get_user = items.get("user");
        System.out.println("iiiiiiiiiiiiiiiiiiid:get_user::" + get_user);
        final String get_job_id = items.get("jobId");
        System.out.println("iiiiiiiiiiiiiiiiiiid:get_job_id::" + get_job_id);
        final String user_id = items.get("userId");
        System.out.println("iiiiiiiiiiiiiiiiiiid:userId::" + user_id);
        final String jobDate = items.get("jobDate");
        final String start_time = items.get("start_time");
        final String end_time = items.get("end_time");
        final String payment_amount = items.get("payment_amount");
        final String payment_type = items.get("payment_type");
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
        jobId.setText(get_job_id);
        tv1.setText(jobDate);
        tv2.setText(start_time);
        tv3.setText(end_time);
        tv4.setText(payment_amount);
        tv5.setText(payment_type);
        job_id.setText(get_jobid);
        employer_id.setText(get_employer);
        employee_id.setText(get_employee);
        image_text.setText(get_image);

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity,MakePayment.class);
                i.putExtra("job_id",get_job_id);
                i.putExtra("userId",user_id);
                i.putExtra("job_name",get_name);
                v.getContext().startActivity(i);
            }
        });

        job_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity,JobDetails.class);
                i.putExtra("jobId",get_job_id);
                i.putExtra("userId",user_id);
                v.getContext().startActivity(i);
            }
        });

        chat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                String jobId = job_id.getText().toString();
                System.out.println("jjjjjjjjjjjj:jobhistory:jobid::"+jobId);
                Intent i = new Intent(activity,ChatNeed.class);
                i.putExtra("jobId",jobId);
                i.putExtra("channel",channel_id);
                i.putExtra("username",get_user);
                view.getContext().startActivity(i);

            }
        });

        if(get_image.equals(""))
        {
            image1.setVisibility(View.VISIBLE);
        }
        else {
            URL url = null;
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
            image.setImageBitmap(bmp);
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
