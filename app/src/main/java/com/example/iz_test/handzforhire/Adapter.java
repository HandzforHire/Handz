
package com.example.iz_test.handzforhire;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Adapter extends BaseAdapter implements Filterable {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<WorldPopulation> worldpopulationlist = null;
    private ArrayList<WorldPopulation> arraylist;
    String jobId;

    String userId,employeeId;;
    String profile_name,channel_id,username,rating_value,msg_notification,star_notification;


    HistoryFilter filter;

    public Adapter(Context context, List<WorldPopulation> worldpopulationlist)  {
        mContext = context;
        this.worldpopulationlist = worldpopulationlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<WorldPopulation>();
        this.arraylist.addAll(worldpopulationlist);
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new HistoryFilter();
        }

        return filter;
    }

    public class ViewHolder {
        TextView leave_rating_btn;
        Button job_details;
        RelativeLayout chat;
        TextView job_name;
        TextView job_id;
        TextView employer_id;
        TextView employee_id;
        TextView user_name;
        TextView image_text,message,star;
        ImageView image;
        ImageView image1;
        LinearLayout leave_layout,edit_layout,rehire_layout;
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
            holder.job_name = (TextView) view.findViewById(R.id.text1);
            holder.job_id = (TextView) view.findViewById(R.id.job_id);
            holder.employer_id = (TextView) view.findViewById(R.id.employer_id);
            holder.employee_id = (TextView) view.findViewById(R.id.employee_id);
            holder.image_text = (TextView) view.findViewById(R.id.image1);
            holder.user_name = (TextView) view.findViewById(R.id.text3);
            holder.image = (ImageView)view.findViewById(R.id.img1);
            holder.leave_rating_btn = (TextView) view.findViewById(R.id.leave_rating);
            holder.job_details = (Button) view.findViewById(R.id.btn);
            holder.chat = (RelativeLayout) view.findViewById(R.id.lay1);
            holder.leave_layout = (LinearLayout) view.findViewById(R.id.leave_lay);
            holder.edit_layout = (LinearLayout) view.findViewById(R.id.edit_lay);
            holder.rehire_layout = (LinearLayout) view.findViewById(R.id.rehire_lay);
            holder.message = (TextView) view.findViewById(R.id.msg_notify);
            holder.star = (TextView) view.findViewById(R.id.star_notify);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (position % 2 == 1) {
            // Set a background color for ListView regular row/item
            view.setBackgroundColor(Color.parseColor("#BF178487"));
        } else {
            // Set the background color for alternate row/item
            view.setBackgroundColor(Color.parseColor("#BFE8C64B"));
        }
        // Set the results into TextViews
        holder.job_name.setText(worldpopulationlist.get(position).getName());
        holder.job_id.setText(worldpopulationlist.get(position).getJobId());
        holder.employer_id.setText(worldpopulationlist.get(position).getEmployerId());
        holder.employee_id.setText(worldpopulationlist.get(position).getEmployeeId());
        holder.image_text.setText(worldpopulationlist.get(position).getImage());
        holder.user_name.setText(worldpopulationlist.get(position).getUsername());
        msg_notification=worldpopulationlist.get(position).getMsg_notification();
        star_notification=worldpopulationlist.get(position).getStar_notification();
        userId = worldpopulationlist.get(position).getUserid();
        profile_name = worldpopulationlist.get(position).getProfilename();
        channel_id = worldpopulationlist.get(position).getChannel();
        username = worldpopulationlist.get(position).getUsername();
        rating_value=worldpopulationlist.get(position).getRatingValue();
        employeeId = worldpopulationlist.get(position).getEmployeeId();
        holder.chat.setTag(position);
        holder.leave_rating_btn.setTag(position);
        holder.job_details.setTag(position);
        holder.edit_layout.setTag(position);
        holder.rehire_layout.setTag(position);

        Glide.with(mContext).load(worldpopulationlist.get(position).getImage()).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(mContext,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(holder.image);
        if(msg_notification.equals("0"))
        {
            holder.message.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.message.setVisibility(View.VISIBLE);
            holder.message.setText(msg_notification);
        }

        if(star_notification.equals("0"))
        {
            holder.star.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.star.setVisibility(View.VISIBLE);
            holder.star.setText(star_notification);

        }

        if(rating_value.equals(""))
        {
            holder.edit_layout.setVisibility(View.GONE);
            holder.leave_layout.setVisibility(View.VISIBLE);
        }


        else
        {
            holder.leave_layout.setVisibility(View.GONE);
            holder.edit_layout.setVisibility(View.VISIBLE);
        }

        holder.leave_rating_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos= (int) v.getTag();
                WorldPopulation item=worldpopulationlist.get(pos);

                if(item.getProfilename().isEmpty())
                    username=item.getUsername();
                else
                    username=item.getProfilename();

                Intent intent = new Intent(mContext, LeaveRating.class);
                intent.putExtra("jobId", item.getJobId());
                intent.putExtra("employer_id",item.getEmployerId());
                intent.putExtra("employee_id",item.getEmployeeId());
                intent.putExtra("user_id",item.getUserid());
                intent.putExtra("image",item.getImage());
                intent.putExtra("profilename",username);
                intent.putExtra("ratingId",item.getRatingId());
                v.getContext().startActivity(intent);
            }
        });


        holder.edit_layout.setOnClickListener(new View.OnClickListener() {
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

        holder.rehire_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos= (int) v.getTag();
                WorldPopulation item=worldpopulationlist.get(pos);
                System.out.println("jjjjjjjjjjjj:jobhistory:jobid::"+jobId);
                Intent i = new Intent(mContext,RehireJob.class);
                i.putExtra("userId",item.getUserid());
                i.putExtra("jobId", item.getJobId());
                i.putExtra("employeeId", item.getEmployeeId());
                v.getContext().startActivity(i);
            }
        });
        holder.chat.setOnClickListener(new View.OnClickListener()
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
                i.putExtra("message_type","job_history");
                i.putExtra("user_type","employer");
                i.putExtra("receiverid",item.getEmployeeId());
                view.getContext().startActivity(i);


                System.out.println("channel id "+channel_id);

            }
        });

        holder.job_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos= (int) v.getTag();
                WorldPopulation item=worldpopulationlist.get(pos);
                Intent i = new Intent(mContext,JobDetails.class);
                i.putExtra("jobId",item.getJobId());
                i.putExtra("userId",item.getUserid());
                i.putExtra("employeeId", item.getEmployeeId());
                v.getContext().startActivity(i);
            }
        });

        return view;
    }

  /*      // Filter Class
        public void filter(String charText,List<WorldPopulation> populationlist) {
            charText = charText.toLowerCase(Locale.getDefault());
            System.out.println(populationlist.size());
            worldpopulationlist.clear();
            if (charText.length() == 0) {
                worldpopulationlist.addAll(arraylist);
            }
            else
            {
                for (WorldPopulation wp : arraylist)
                {

                    System.out.println("charText "+charText);
                    if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)||wp.getTransaction_date().toLowerCase(Locale.getDefault()).contains(charText)||wp.getProfilename().toLowerCase(Locale.getDefault()).contains(charText)||wp.getJob_category().toLowerCase(Locale.getDefault()).contains(charText)||wp.getUsername().toLowerCase(Locale.getDefault()).contains(charText)||wp.getDescription().toLowerCase(Locale.getDefault()).contains(charText))
                    {
                        System.out.println("job name "+wp.getName());
                        worldpopulationlist.add(wp);
                    }
                }
            }
        *//*    adapter = new Adapter(mContext, worldpopulationlist) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    // Get the current item from ListView
                    View view = super.getView(position, convertView, parent);
                    if (position % 2 == 1) {
                        // Set a background color for ListView regular row/item
                        view.setBackgroundColor(Color.parseColor("#BF178487"));
                    } else {
                        // Set the background color for alternate row/item
                        view.setBackgroundColor(Color.parseColor("#BFE8C64B"));
                    }
                    return view;
                }
            };
             list.setAdapter(adapter);
            System.out.println("length "+charText.length());
            System.out.println("size "+worldpopulationlist.size());*//*
            // DataBind ListView with items from ArrayAdapter

           this.notifyDataSetChanged();
        }*/

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }



    private class HistoryFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charText) {
            FilterResults results = new FilterResults();
// We implement here the filter logic
            if (charText == null || charText.length() == 0) {
// No filter implemented we return all the list
                results.values = arraylist;
                results.count = arraylist.size();
            }
            else {
// We perform filtering operation
                List nPlanetList = new ArrayList();

                for (int i=0;i<arraylist.size();i++) {
                    WorldPopulation wp=arraylist.get(i);
                    // if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)||wp.getTransaction_date().toLowerCase(Locale.getDefault()).contains(charText)||wp.getProfilename().toLowerCase(Locale.getDefault()).contains(charText)||wp.getJob_category().toLowerCase(Locale.getDefault()).contains(charText)||wp.getUsername().toLowerCase(Locale.getDefault()).contains(charText)||wp.getDescription().toLowerCase(Locale.getDefault()).contains(charText))
                    if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText))

                        nPlanetList.add(wp);
                }

                results.values = nPlanetList;
                results.count = nPlanetList.size();

            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

// Now we have to inform the adapter about the new list filtered
            if (results.count == 0)
                notifyDataSetInvalidated();
            else {
                worldpopulationlist.clear();
                worldpopulationlist.addAll((List)results.values);
                notifyDataSetChanged();
                //worldpopulationlist = (List) results.values;
                for(int i=0;i<worldpopulationlist.size();i++)
                {
                    WorldPopulation wp=worldpopulationlist.get(i);
                    System.out.println("wp "+wp.getName());
                }

            }
        }
    }

}
