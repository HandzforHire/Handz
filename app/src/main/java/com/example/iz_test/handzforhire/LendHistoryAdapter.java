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
            TextView date = (TextView) vi.findViewById(R.id.date);
            TextView month = (TextView) vi.findViewById(R.id.month);
            TextView year = (TextView) vi.findViewById(R.id.year);
            final TextView job_id = (TextView) vi.findViewById(R.id.job_id);
            final TextView employer_id = (TextView) vi.findViewById(R.id.employer_id);
            final TextView employee_id = (TextView) vi.findViewById(R.id.employee_id);
            LinearLayout chat = (LinearLayout) vi.findViewById(R.id.lay1);

            LinearLayout leave_layout = (LinearLayout) vi.findViewById(R.id.leave_lay);
            LinearLayout edit_layout = (LinearLayout) vi.findViewById(R.id.edit_lay);

            String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
            Typeface font = Typeface.createFromAsset(activity.getAssets(), fontPath);
            String fontPath1 = "fonts/calibri.ttf";
            Typeface font1 = Typeface.createFromAsset(activity.getAssets(), fontPath1);

            HashMap<String, String> items = new HashMap<String, String>();
            items = data.get(position);
            final String get_name = items.get("name");
            final String get_image = items.get("image");
            final String get_profile = items.get("profile");
            final String get_amount = items.get("payment");
            final String get_id = items.get("user_id");
            final String get_jobid = items.get("jobId");
            final String get_employer = items.get("employer");
            final String get_employee = items.get("employee");
            final String channel_id=items.get("channel");
            final String rating_value=items.get("rating");

            job_name.setText(get_name);
            job_name.setTypeface(font);
            amount.setText(get_amount);
            job_id.setText(get_jobid);
            employer_id.setText(get_employer);
            employee_id.setText(get_employee);



            if(rating_value.equals(""))
            {
                edit_layout.setVisibility(View.GONE);
                leave_layout.setVisibility(View.VISIBLE);
            }
            else
            {
                leave_layout.setVisibility(View.GONE);
                edit_layout.setVisibility(View.VISIBLE);
            }

            chat.setTag(position);
            job_details.setTag(position);
            leave_layout.setTag(position);
            edit_layout.setTag(position);


            leave_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    HashMap<String, String> items = new HashMap<String, String>();
                    items = data.get((Integer) v.getTag());
                    Intent intent = new Intent(activity, LendRating.class);
                    intent.putExtra("jobId",items.get("jobId"));
                    intent.putExtra("employer_id", items.get("employer"));
                    intent.putExtra("employee_id",items.get("employee"));
                    intent.putExtra("user_id",items.get("user_id"));
                    intent.putExtra("profilename",items.get("profile"));
                    v.getContext().startActivity(intent);
                }
            });

            edit_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    HashMap<String, String> items = new HashMap<String, String>();
                    items = data.get((Integer) v.getTag());
                    Intent intent = new Intent(activity, EditRating.class);
                    intent.putExtra("jobId",items.get("jobId"));
                    intent.putExtra("employer_id", items.get("employer"));
                    intent.putExtra("employee_id",items.get("employee"));
                    intent.putExtra("user_id",items.get("user_id"));
                    intent.putExtra("profilename",items.get("profile"));
                    intent.putExtra("ratingId",items.get("ratingId"));
                    intent.putExtra("cat1",items.get("category1"));
                    intent.putExtra("cat2",items.get("category2"));
                    intent.putExtra("cat3",items.get("category3"));
                    intent.putExtra("cat4",items.get("category4"));
                    intent.putExtra("cat5",items.get("category5"));
                    v.getContext().startActivity(intent);
                }
            });

            chat.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {

                    int pos= (int) view.getTag();
                    HashMap<String, String> items =data.get(pos);
                    String username="";
                    if(items.get("profile").isEmpty())
                        username=items.get("user");
                    else
                        username= items.get("profile");

                    Intent i = new Intent(activity,ChatNeed.class);
                    i.putExtra("jobId",items.get("jobId"));
                    i.putExtra("channel",items.get("channel"));
                    i.putExtra("username",username);
                    i.putExtra("userId", items.get("user_id"));
                    view.getContext().startActivity(i);
                }
            });

            job_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos= (int) v.getTag();
                    HashMap<String, String> items =data.get(pos);

                    Intent i = new Intent(activity,JobDetails.class);
                    i.putExtra("jobId",items.get("jobId"));
                    i.putExtra("userId",items.get("user_id"));
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


    }