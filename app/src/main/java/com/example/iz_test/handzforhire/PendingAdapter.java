package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class PendingAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;


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

    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.pending_job_list, null);

        TextView job_name = (TextView) vi.findViewById(R.id.job);
        TextView job_date = (TextView) vi.findViewById(R.id.whe);
        TextView pay = (TextView) vi.findViewById(R.id.pay);
        TextView job_type = (TextView) vi.findViewById(R.id.esti);
        TextView jobId = (TextView) vi.findViewById(R.id.job_id);
        ImageView img=(ImageView) vi.findViewById(R.id.cir);

        System.out.println("pending adapter class");
        img.setOnClickListener(new View.OnClickListener()
        {
        @Override
         public void onClick(View v)
        {

     }
    });


        HashMap<String, String> items = new HashMap<String, String>();
        items = data.get(position);
        final String get_jobname=items.get("name");
        System.out.println("1111111"+get_jobname);
        final String get_jobdate=items.get("date");
        System.out.println("2222222"+get_jobdate);
        final String get_pay=items.get("amount");
        System.out.println("3333333"+get_pay);
        final String get_esti=items.get("type");
        System.out.println("4444444"+get_esti);
        final String get_status=items.get("status");
        System.out.println("555555"+get_status);


        //job_name.setText("PAY"+get_jobname);

        job_name.setText(""+get_jobname);
        job_date.setText("WHEN: edited"+get_jobdate);
        pay.setText("PAY:"+get_pay);
        job_type.setText("ESTIMATED DURATION:"+get_esti);
        //jobId.setText(get_id);

        return vi;
    }
}