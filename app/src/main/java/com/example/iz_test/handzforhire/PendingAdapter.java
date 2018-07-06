package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class PendingAdapter extends BaseAdapter {
    private static final String emp_reject = Constant.SERVER_URL + "employee_reject";
    private static final String job_list = Constant.SERVER_URL + "job_lists";

    public static String XAPP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    String type = "employee";
    String user_id;
    String get_jobid, get_emplrid, get_employeeid, get_status;
    public static String KEY_JOBID = "job_id";
    public static String KEY_EMPLOYERID = "employer_id";
    public static String KEY_EMPLOYEEID = "employee_id";
    public static String KEY_USERTYPE = "user_type";
    public static String KEY_USER = "user_id";
    ProgressDialog progress_dialog;
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;
    private LayoutInflater layoutInflater;
    LinearLayout lig,lir,lih;
    Dialog dialog;

    public PendingAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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

    public View getView(final int position, final View convertView, ViewGroup parent) {
        View vi = convertView;

        if (convertView == null)
            vi = inflater.inflate(R.layout.pending_job_list, null);

        TextView job_name = (TextView) vi.findViewById(R.id.job);
        TextView job_date = (TextView) vi.findViewById(R.id.whe);
        TextView pay = (TextView) vi.findViewById(R.id.pay);
        TextView job_type = (TextView) vi.findViewById(R.id.esti);
        TextView jobId = (TextView) vi.findViewById(R.id.job_id);
        ImageView gray = (ImageView) vi.findViewById(R.id.gray);
        ImageView red = (ImageView) vi.findViewById(R.id.red);
        ImageView green = (ImageView) vi.findViewById(R.id.green);
        final LinearLayout lig=(LinearLayout)vi.findViewById(R.id.hiree);
        final LinearLayout lir=(LinearLayout)vi.findViewById(R.id.refu);
        final LinearLayout lih=(LinearLayout)vi.findViewById(R.id.hol);



        gray.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                lih.setVisibility(View.VISIBLE);
                lih.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        lih.setVisibility(View.GONE);

                    }
                });


            }
        });
        red.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                lir.setVisibility(View.VISIBLE);
                lir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {

                        refusee();


                    }
                });

        }
        });


       green.setOnClickListener(new View.OnClickListener()
       {
           @Override
           public void onClick(View v)
           {

               lig.setVisibility(View.VISIBLE);
               lig.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v)
                   {
                       lig.setVisibility(View.GONE);
                   }
               });

           }
       });


        final LinearLayout lin_hold=(LinearLayout)vi.findViewById(R.id.lin_hold);
        final LinearLayout lin_hire=(LinearLayout)vi.findViewById(R.id.lin_hire);
        final LinearLayout lin_refuse=(LinearLayout)vi.findViewById(R.id.lin_refuse);

        final LinearLayout layout_refuse=(LinearLayout)vi.findViewById(R.id.layout_refuse);
        final LinearLayout layout_hold=(LinearLayout)vi.findViewById(R.id.layout_hold);
        final LinearLayout layout_hire=(LinearLayout)vi.findViewById(R.id.layout_hire);


        HashMap<String, String> items = new HashMap<String, String>();
        items = data.get(position);
        final String get_jobname = items.get("name");
        final String get_jobdate = items.get("date");
        final String get_pay = items.get("amount");
        final String get_esti = items.get("type");
        get_status = items.get("status");
        user_id = items.get("employeeid");

        if (get_status.equals("Hired")) {
            green.setVisibility(View.VISIBLE);
            gray.setVisibility(View.INVISIBLE);
            red.setVisibility(View.INVISIBLE);

        } else if (get_status.equals("Hold")) {
            gray.setVisibility(View.VISIBLE);
           green.setVisibility(View.INVISIBLE);
            red.setVisibility(View.INVISIBLE);

        } else {
            red.setVisibility(View.VISIBLE);
            green.setVisibility(View.INVISIBLE);
            gray.setVisibility(View.INVISIBLE);

        }


        layout_hold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_hold.setVisibility(View.GONE);
            }
        });

        layout_hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_hire.setVisibility(View.GONE);
            }
        });

        layout_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_refuse.setVisibility(View.GONE);
            }
        });




        gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_hold.setVisibility(View.VISIBLE);
                lin_hire.setVisibility(View.GONE);
                lin_refuse.setVisibility(View.GONE);
                return;
            }
        });
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_hold.setVisibility(View.GONE);
                lin_hire.setVisibility(View.GONE);
                lin_refuse.setVisibility(View.VISIBLE);
                HashMap<String, String> items = new HashMap<String, String>();
                items = data.get((Integer) v.getTag());
                get_jobid = items.get("jobId");
                get_emplrid = items.get("emrid");
                get_employeeid = items.get("employeeid");
                type=items.get("type");
                refusee();
                return;
            }

        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lin_hold.setVisibility(View.GONE);
                lin_hire.setVisibility(View.VISIBLE);
                lin_refuse.setVisibility(View.GONE);
                return;

            }
        });

        get_jobid = items.get("jobId");
        get_emplrid = items.get("emrid");
        get_employeeid = items.get("employeeid");

        DateFormat dateInstance = SimpleDateFormat.getDateInstance();
        DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat destDf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        try {
            java.util.Date dates = srcDf.parse(get_jobdate);
            System.out.println("date " + get_jobdate);
            System.out.println("converted " + destDf.format(dates));
            job_name.setText("" + get_jobname);
            job_date.setText("WHEN:" + destDf.format(dates));
            pay.setText("PAY:" + get_pay);
            job_type.setText("ESTIMATED DURATION:" + get_esti);
            //jobId.setText(get_id);
            //job_name.setText("PAY"+get_jobname);

        } catch (Exception e) {
            System.out.println("error " + e.getMessage());
        }
        System.out.println("today date " + dateInstance.format(Calendar.getInstance().getTime()));
        return vi;
    }

    private void refusee()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, emp_reject,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resss::emp_reject" + response);
                        onResponserecieved(response, 2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            System.out.println("error" + jsonObject);
                            String status = jsonObject.getString("msg");
                            if (status.equals("success")) {


                            }
                        } catch (JSONException e) {

                        } catch (UnsupportedEncodingException error1) {

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(KEY_JOBID, get_jobid);
                params.put(KEY_EMPLOYERID, get_emplrid);
                params.put(KEY_EMPLOYEEID, get_employeeid);
                params.put(KEY_USERTYPE, type);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);

    }

    private void onResponserecieved(String response, int i)
    {
        String status = null;

        try {
            JSONObject result = new JSONObject(response);
            status = result.getString("status");
            if (status.equals("success"))
            {

                Reload();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void Reload()
    {

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, job_list,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("reeeeeeeeeeeeeeeee:reload:::" + response);
                        onResponserecieved(response, 2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            System.out.println("error" + jsonObject);
                            String status = jsonObject.getString("msg");
                            if (status.equals("success"))
                            {

                                Toast.makeText(activity, "Job List Refreshed", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {

                        } catch (UnsupportedEncodingException error1)
                        {

                        }
                        dialog.dismiss();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(KEY_USER, user_id);
                params.put(KEY_USERTYPE, type);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }
}












