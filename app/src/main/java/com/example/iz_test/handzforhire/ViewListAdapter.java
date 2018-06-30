package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ViewListAdapter extends BaseAdapter {

    private Activity activity;
    String dlist,job_id;
    String value = "HandzForHire@~";
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    public ViewListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
            vi = inflater.inflate(R.layout.view_listview, null);

        TextView job_name = (TextView) vi.findViewById(R.id.text1);
        TextView when = (TextView) vi.findViewById(R.id.text2);
        TextView pay = (TextView) vi.findViewById(R.id.text6);
        TextView date = (TextView) vi.findViewById(R.id.text3);
        TextView amount = (TextView) vi.findViewById(R.id.text7);
        TextView type = (TextView) vi.findViewById(R.id.text8);
        final TextView jobId = (TextView) vi.findViewById(R.id.job_id);
        final TextView applicants = (TextView) vi.findViewById(R.id.no_applicants);
        ImageView checked=(ImageView)vi.findViewById(R.id.img);
        ImageView unchecked=(ImageView)vi.findViewById(R.id.img1);


        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface font = Typeface.createFromAsset(activity.getAssets(), fontPath);
        String fontPath1 = "fonts/calibri.ttf";
        Typeface font1 = Typeface.createFromAsset(activity.getAssets(), fontPath1);

        HashMap<String, String> items = new HashMap<String, String>();
        items = data.get(position);
        final String get_name = items.get("name");
        System.out.println("iiiiiiiiiiiiiiiiiiid:get_name::" + get_name);
        final String get_date = items.get("date");
        System.out.println("iiiiiiiiiiiiiiiiiiid:get_date::" + get_date);
        String get_amount = items.get("amount");
        System.out.println("iiiiiiiiiiiiiiiiiiid:get_amount::" + get_amount);
        String get_type = items.get("type");
        System.out.println("iiiiiiiiiiiiiiiiiiid:get_recur::" + get_type);
        job_id = items.get("jobId");
        System.out.println("iiiiiiiiiiiiiiiiiiid:get_id::" + job_id);
        final String get_applicants = items.get("no_of_applicants");
        System.out.println("iiiiiiiiiiiiiiiiiiid:get_applicants::" + get_applicants);
        final String user_id = items.get("userId");
        System.out.println("iiiiiiiiiiiiiiiiiiid:user_id::" + user_id);
        final String address = items.get("address");
        System.out.println("iiiiiiiiiiiiiiiiiiid:address::" + address);
        final String city = items.get("city");
        System.out.println("iiiiiiiiiiiiiiiiiiid:city::" + city);
        final String state = items.get("state");
        System.out.println("iiiiiiiiiiiiiiiiiiid:state::" + state);
        final String zipcode = items.get("zipcode");
        System.out.println("iiiiiiiiiiiiiiiiiiid:zipcode::" + zipcode);
        dlist= items.get("d_list");
        System.out.println("iiiiiidlist::"+dlist);

        if (dlist.equals("no"))
        {
            unchecked.setVisibility(View.VISIBLE);
            checked.setVisibility(View.INVISIBLE);
            unchecked.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    uncheck();

                }
            });

        }else
        {
            checked.setVisibility(View.VISIBLE);
            unchecked.setVisibility(View.INVISIBLE);
            checked.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    check();

                }
            });

        }
        DateFormat dateInstance = SimpleDateFormat.getDateInstance();
        DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat destDf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        try {
            java.util.Date dates = srcDf.parse(get_date);
            System.out.println("date " + get_date);
            System.out.println("converted " + destDf.format(dates));
            date.setText("" + destDf.format(dates));

        } catch (Exception e)
        {
            System.out.println("error " + e.getMessage());
        }


        if(get_applicants.equals("0"))
        {
            applicants.setVisibility(View.INVISIBLE);
        }
        else
        {
            applicants.setText(get_applicants);
        }

        job_name.setText(get_name);
        job_name.setTypeface(font);
        when.setTypeface(font);
        pay.setTypeface(font);
        //date.setText(get_date);
        //date.setTypeface(font1);
        amount.setText(get_amount);
        amount.setTypeface(font1);
        type.setText(get_type);
        type.setTypeface(font1);
        jobId.setText(job_id);

        applicants.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (get_applicants.equals("0")) {
                    final Dialog dialog = new Dialog(activity);
                    dialog.setContentView(R.layout.custom_dialog);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("No Job Applied");
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
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    return;
                } else {
                    Intent i = new Intent(activity, ViewApplicant.class);
                    i.putExtra("jobId", job_id);
                    i.putExtra("userId",user_id);
                    i.putExtra("address", address);
                    i.putExtra("city",city);
                    i.putExtra("zipcode", zipcode);
                    i.putExtra("state",state);
                    i.putExtra("jobname",get_name);
                    v.getContext().startActivity(i);
                }
            }
        });

        return vi;
    }

    private void uncheck()
    {

        final String url = "http://162.144.41.156/~izaapinn/handzforhire/service/remove_job?X-APP-KEY="+value+"&delist="+dlist+"&job_id="+job_id;

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {

                        Log.d("Response", response.toString());
                        System.out.println("resssssssssssssssss:dlist::service:::" + response);
                        onResponserecieved1(response, 1);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {



                    }
                }
        );


        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(getRequest);
    }

    public void onResponserecieved2(JSONObject jsonobject, int i)
    {
        String status = null;

        try {

            JSONObject jResult = new JSONObject(String.valueOf(jsonobject));

            status = jResult.getString("status");

            if(status.equals("success"))
            {
                /*Intent intent=new Intent(activity,ProfilePage.class);
                activity.startActivity(intent);*/
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    private void check()
    {

        final String url = "http://162.144.41.156/~izaapinn/handzforhire/service/remove_job?X-APP-KEY="+value+"&delist="+dlist+"&job_id="+job_id;

                JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {

                        Log.d("Response", response.toString());
                        System.out.println("resssssssssssssssss:dlist::service:::" + response);
                        onResponserecieved1(response, 1);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                }
        );


        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(getRequest);
    }

    public void onResponserecieved1(JSONObject jsonobject, int i)
    {
        String status = null;

        try {

            JSONObject jResult = new JSONObject(String.valueOf(jsonobject));

            status = jResult.getString("status");

            if(status.equals("success"))
            {
                Intent intent=new Intent(activity,PostedJobs.class);
                activity.startActivity(intent);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

