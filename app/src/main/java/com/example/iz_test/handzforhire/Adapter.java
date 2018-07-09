package com.example.iz_test.handzforhire;


import android.graphics.Color;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;

public class Adapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<WorldPopulation> worldpopulationlist = null;
    private ArrayList<WorldPopulation> arraylist;
    String jobId;
    TextView leave_rating_btn;
    Button job_details;
    LinearLayout chat;
    TextView job_name;
    TextView job_id;
    TextView employer_id;
    TextView employee_id;
    TextView user_name;
    TextView image_text;
    ImageView image;
    ImageView image1;
    String userId;
    String profile_name,channel_id,username,rating_value;
    LinearLayout leave_layout,edit_layout,rehire_layout;

    public Adapter(Context context, List<WorldPopulation> worldpopulationlist) {
        mContext = context;
        this.worldpopulationlist = worldpopulationlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<WorldPopulation>();
        this.arraylist.addAll(worldpopulationlist);
    }

    public class ViewHolder {

    }

    @Override
    public int getCount() {
        return worldpopulationlist.size();
    }

    @Override
    public WorldPopulation getItem(int position) {
        return worldpopulationlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.job_history_list, null);
            // Locate the TextViews in listview_item.xml
            job_name = (TextView) view.findViewById(R.id.text1);
            job_id = (TextView) view.findViewById(R.id.job_id);
            employer_id = (TextView) view.findViewById(R.id.employer_id);
            employee_id = (TextView) view.findViewById(R.id.employee_id);
            image_text = (TextView) view.findViewById(R.id.image1);
            user_name = (TextView) view.findViewById(R.id.text3);
            image = (ImageView)view.findViewById(R.id.img1);
            leave_rating_btn = (TextView) view.findViewById(R.id.leave_rating);
            job_details = (Button) view.findViewById(R.id.btn);
            chat = (LinearLayout) view.findViewById(R.id.lay1);
            leave_layout = (LinearLayout) view.findViewById(R.id.leave_lay);
            edit_layout = (LinearLayout) view.findViewById(R.id.edit_lay);
            rehire_layout = (LinearLayout) view.findViewById(R.id.rehire_lay);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        job_name.setText(worldpopulationlist.get(position).getName());
        job_id.setText(worldpopulationlist.get(position).getJobId());
        employer_id.setText(worldpopulationlist.get(position).getEmployerId());
        employee_id.setText(worldpopulationlist.get(position).getEmployeeId());
        image_text.setText(worldpopulationlist.get(position).getImage());
        user_name.setText(worldpopulationlist.get(position).getUsername());
        userId = worldpopulationlist.get(position).getUserid();
        profile_name = worldpopulationlist.get(position).getProfilename();
        channel_id = worldpopulationlist.get(position).getChannel();
        username = worldpopulationlist.get(position).getUsername();
        rating_value=worldpopulationlist.get(position).getRatingValue();

        chat.setTag(position);
        leave_rating_btn.setTag(position);
        job_details.setTag(position);
        edit_layout.setTag(position);
        rehire_layout.setTag(position);

        Glide.with(mContext).load(worldpopulationlist.get(position).getImage()).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image);


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

        leave_rating_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos= (int) v.getTag();
                WorldPopulation item=worldpopulationlist.get(pos);

                if(item.getProfilename().isEmpty())
                    username=item.getUsername();
                else
                    username=item.getProfilename();

                Intent intent = new Intent(mContext, NeedRating.class);
                intent.putExtra("jobId", item.getJobId());
                intent.putExtra("employer_id",item.getEmployerId());
                intent.putExtra("employee_id",item.getEmployeeId());
                intent.putExtra("user_id",item.getUserid());
                intent.putExtra("image",item.getImage());
                intent.putExtra("profilename",username);
                v.getContext().startActivity(intent);
            }
        });


        edit_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos= (int) v.getTag();
                WorldPopulation item=worldpopulationlist.get(pos);
                jobId = item.getJobId();
                String employerId = item.getEmployerId();
                String employeeId = item.getEmployeeId();
                String profile_image =item.getImage();
                Intent intent = new Intent(mContext, EditRating.class);
                intent.putExtra("jobId",jobId);
                intent.putExtra("employer_id",employerId);
                intent.putExtra("employee_id",employeeId);
                intent.putExtra("user_id",item.getUserid());
                intent.putExtra("image",profile_image);
                intent.putExtra("ratingId",item.getRatingId());
                intent.putExtra("cat1",item.getCategory1());
                intent.putExtra("cat2",item.getCategory2());
                intent.putExtra("cat3",item.getCategory3());
                intent.putExtra("cat4",item.getCategory4());
                intent.putExtra("cat5",item.getCategory5());
                intent.putExtra("profilename",profile_name);
                v.getContext().startActivity(intent);
            }
        });

        rehire_layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos= (int) v.getTag();
                WorldPopulation item=worldpopulationlist.get(pos);
                jobId = job_id.getText().toString();
                System.out.println("jjjjjjjjjjjj:jobhistory:jobid::"+jobId);
                Intent i = new Intent(mContext,RehireJob.class);
                i.putExtra("userId",item.getUserid());
                i.putExtra("jobId", item.getJobId());
                v.getContext().startActivity(i);
            }
        });
        chat.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view) {
                int pos= (int) view.getTag();
               WorldPopulation item=worldpopulationlist.get(pos);

                jobId = item.getJobId();
                channel_id=item.getChannel();
                if(item.getProfilename().isEmpty())
                    username=item.getUsername();
                else
                    username=item.getProfilename();
                userId=item.getUserid();

                Intent i = new Intent(mContext,ChatNeed.class);
                i.putExtra("jobId",jobId);
                i.putExtra("channel",channel_id);
                i.putExtra("username",username);
                i.putExtra("userId",userId);
                view.getContext().startActivity(i);


                System.out.println("channel id "+channel_id);

            }
        });

        job_details.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos= (int) v.getTag();
                WorldPopulation item=worldpopulationlist.get(pos);
                Intent i = new Intent(mContext,JobDetails.class);
                i.putExtra("jobId",item.getJobId());
                i.putExtra("userId",item.getUserid());
                v.getContext().startActivity(i);
            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        worldpopulationlist.clear();
        if (charText.length() == 0) {
            worldpopulationlist.addAll(arraylist);
        }
        else
        {
            for (WorldPopulation wp : arraylist)
            {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    worldpopulationlist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
