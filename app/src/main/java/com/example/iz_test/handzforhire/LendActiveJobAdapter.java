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

        final String get_image = items.get("image");
        final String get_user = items.get("user");
        final String get_job_id = items.get("jobId");
        final String user_id = items.get("userId");
        final String jobDate = items.get("jobDate");
        final String start_time = items.get("start_time");
        final String end_time = items.get("end_time");
        final String payment_amount = items.get("payment_amount");
        final String payment_type = items.get("payment_type");
        final String get_jobid = items.get("jobId");
        final String get_employer = items.get("employer");
        final String get_employee = items.get("employee");
        final String channel_id=items.get("channel");

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


        payment.setTag(position);
        job_details.setTag(position);
        chat.setTag(position);

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int pos= (int) v.getTag();
                HashMap<String, String> items =data.get(pos);
                String username="";

                Intent i = new Intent(activity,MakePayment.class);
                i.putExtra("job_id", items.get("jobId"));
                i.putExtra("userId",items.get("userId"));
                i.putExtra("job_name",items.get("name"));
                v.getContext().startActivity(i);
            }
        });

        job_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos= (int) v.getTag();
                HashMap<String, String> items =data.get(pos);
                Intent i = new Intent(activity,JobDetails.class);
                i.putExtra("jobId", items.get("jobId"));
                i.putExtra("userId",items.get("userId"));
                v.getContext().startActivity(i);
            }
        });

        chat.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                int pos= (int) view.getTag();
                HashMap<String, String> items =data.get(pos);
                String username="";
                String  jobId =  items.get("jobId");;
                String channel_id=items.get("channel");

                if(items.get("profile").isEmpty())
                    username=items.get("user");
                else
                    username= items.get("profile");

                String  userId=items.get("userId");

                Intent i = new Intent(activity,ChatNeed.class);
                i.putExtra("jobId",jobId);
                i.putExtra("channel",channel_id);
                i.putExtra("username",username);
                view.getContext().startActivity(i);

            }
        });

        if(get_image.equals(""))
        {
            image1.setVisibility(View.VISIBLE);
        }
        else {
            Glide.with(activity).load(get_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(activity,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image1);

        }

        return vi;
    }

}
