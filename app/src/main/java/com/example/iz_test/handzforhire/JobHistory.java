package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.pwittchen.swipe.library.rx2.SimpleSwipeListener;
import com.github.pwittchen.swipe.library.rx2.Swipe;
import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JobHistory extends Activity {

        private static final String URL = Constant.SERVER_URL+"job_history_listing";
        ArrayList<HashMap<String, String>> job_list = new ArrayList<HashMap<String, String>>();
        ImageView logo;
        public static String KEY_USERID = "user_id";
        public static String XAPP_KEY = "X-APP-KEY";
        public static String TYPE = "type";
        String value = "HandzForHire@~";
        String address,city,state,zipcode,user_id,job_id;
        TextView profile_name;
        Button posted_job,active_job;
        ListView list;
        ProgressDialog progress_dialog;
        String usertype = "employer";
    int timeout = 60000;
    EditText editsearch;
    ArrayList<WorldPopulation> arraylist = new ArrayList<WorldPopulation>();
    String rating_value,rating_id,category1,category2,category3,category4,category5;
    Adapter adapter;
    Dialog dialog;
    Swipe swipe;
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.job_history);

            /*progress_dialog = new ProgressDialog(this);
            progress_dialog.setMessage("Loading.Please wait....");
            progress_dialog.show();*/

            dialog = new Dialog(JobHistory.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progressbar);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


            posted_job = (Button) findViewById(R.id.btn1);
            active_job = (Button)findViewById(R.id.btn2);
            logo = (ImageView)findViewById(R.id.logo);
            list = (ListView) findViewById(R.id.listview);
            editsearch = (EditText) findViewById(R.id.search);

            Intent i = getIntent();
            user_id = i.getStringExtra("userId");
            address = i.getStringExtra("address");
            city = i.getStringExtra("city");
            state = i.getStringExtra("state");
            zipcode = i.getStringExtra("zipcode");

            activeJobs();
            adapter = new Adapter(this, arraylist);

            editsearch.addTextChangedListener(new TextWatcher() {
                private List<WorldPopulation> worldpopulationlist =  new ArrayList<WorldPopulation>();
                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                    String charText = editsearch.getText().toString().toLowerCase(Locale.getDefault());
                    adapter.getFilter().filter(charText);
                  //
                 //   adapter.notifyDataSetChanged();
                    //JobHistory.this.
                    System.out.println("array list "+arraylist.size());
                 /*   if (charText.length() == 0) {
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
                    adapter = new Adapter(getApplicationContext(), worldpopulationlist);
                    list.setAdapter(adapter);*/
                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1,
                                              int arg2, int arg3) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                    // TODO Auto-generated method stub
                }
            });

       logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(JobHistory.this,ProfilePage.class);
                    i.putExtra("userId", user_id);
                    i.putExtra("address", address);
                    i.putExtra("city", city);
                    i.putExtra("state", state);
                    i.putExtra("zipcode", zipcode);
                    startActivity(i);
                    finish();
                }
            });

            posted_job.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(JobHistory.this,PostedJobs.class);
                    i.putExtra("userId", user_id);
                    i.putExtra("address", address);
                    i.putExtra("city", city);
                    i.putExtra("state", state);
                    i.putExtra("zipcode", zipcode);
                    startActivity(i);
                }
            });

            active_job.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(JobHistory.this,ActiveJobs.class);
                    i.putExtra("userId", user_id);
                    i.putExtra("address", address);
                    i.putExtra("city", city);
                    i.putExtra("state", state);
                    i.putExtra("zipcode", zipcode);
                    startActivity(i);
                }
            });

            swipe = new Swipe();
            swipe.setListener(new SimpleSwipeListener() {

                @Override
                public boolean onSwipedLeft(MotionEvent event) {
                    Intent i = new Intent(JobHistory.this,ProfilePage.class);
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
                    Intent j = new Intent(JobHistory.this, SwitchingSide.class);
                    startActivity(j);
                    finish();
                    return super.onSwipedRight(event);
                }
            });
        }

        public void activeJobs() {
            dialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("resssssssssssssssss:job_history::" + response);
                            onResponserecieved(response, 1);
                            dialog.dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();
                            try {
                                String responseBody = new String( error.networkResponse.data, "utf-8" );
                                JSONObject jsonObject = new JSONObject( responseBody );
                                System.out.println("error"+jsonObject);
                                String status = jsonObject.getString("msg");
                                if(status.equals("No Jobs Found"))
                                {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(JobHistory.this);
                                    dialog.setContentView(R.layout.custom_dialog);

                                    // set the custom dialog components - text, image and button
                                    TextView text = (TextView) dialog.findViewById(R.id.text);
                                    text.setText("No Jobs Found");
                                    Button dialogButton = (Button) dialog.findViewById(R.id.ok);
                                    // if button is clicked, close the custom dialog
                                    dialogButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });

                                    dialog.show();
                                    Window window = dialog.getWindow();
                                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                    window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                }
                            } catch ( JSONException e ) {
                                //Handle a malformed json response
                                System.out.println("volley error ::"+e.getMessage());
                            } catch (UnsupportedEncodingException errors){
                                System.out.println("volley error ::"+errors.getMessage());
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(XAPP_KEY, value);
                    params.put(KEY_USERID, user_id);
                    params.put(TYPE, usertype);
                    return params;
                }
            };
            System.out.println("vvvvvvv"+ value+"."+user_id+"."+ usertype);
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }

        public void onResponserecieved(String jsonobject, int i) {
            System.out.println("response from interface"+jsonobject);

            String status = null;
            String jobList = null;

            try {
                JSONObject jResult = new JSONObject(jsonobject);
                status = jResult.getString("status");
                jobList = jResult.getString("job_lists");

                if(status.equals("success"))
                {
                    JSONArray array = new JSONArray(jobList);
                    for(int n = 0; n < array.length(); n++) {
                        JSONObject object = (JSONObject) array.get(n);
                        final String job_name = object.getString("job_name");
                        final String image = object.getString("profile_image");
                        final String profilename = object.getString("profile_name");
                        final String username = object.getString("username");
                        final String jobId = object.getString("job_id");
                        final String employerId = object.getString("employer_id");
                        final String employeeId = object.getString("employee_id");
                        final String channelid=object.getString("channel");
                        final String tran_date=object.getString("transaction_date");
                        final String job_category=object.getString("job_category");
                        final String description=object.getString("description");
                        final String msg_notification =object.getString("employer_notificationCountMsgJobhistory");
                        final String star_notification =object.getString("employer_notificationCountStarRating");


                        String rating=object.getString("rating");

                        if(rating.equals("null"))
                        {
                            rating_value = "";
                            rating_id = "";
                            category1 = "";
                            category2 = "";
                            category3 = "";
                            category4 = "";
                            category5 = "";
                        }
                        else
                        {
                            JSONObject Result = new JSONObject(rating);
                            rating_value = Result.getString("rating");
                            rating_id = Result.getString("id");
                            category1 = Result.getString("category1");
                            category2 = Result.getString("category2");
                            category3 = Result.getString("category3");
                            category4 = Result.getString("category4");
                            category5 = Result.getString("category5");
                        }

                        WorldPopulation wp = new WorldPopulation(job_name,image,profilename,username,jobId,employerId,employeeId,channelid,user_id,rating_id,rating_value,category1,category2,category3,category4,category5,tran_date,job_category,description,msg_notification,star_notification);
                        // Binds all strings into an array
                        arraylist.add(wp);

                    }

                    adapter = new Adapter(this, arraylist);


                    // DataBind ListView with items from ArrayAdapter
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            view.setSelected(true);
                            job_id = ((TextView) view.findViewById(R.id.job_id)).getText().toString();
                            System.out.println("ssssssssssselected:item:" + job_id);
                        }
                    });

                }
                else
                {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    public boolean dispatchTouchEvent(MotionEvent event){

        swipe.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }



    public class Adapter extends BaseAdapter implements Filterable {

        // Declare Variables
        Context mContext;
        LayoutInflater inflater;
        private List<WorldPopulation> worldpopulationlist = null;
        private ArrayList<WorldPopulation> arraylist;
        String jobId;

        String userId;
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
            LinearLayout chat;
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
                holder.chat = (LinearLayout) view.findViewById(R.id.lay1);
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


}
