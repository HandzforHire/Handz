package com.example.iz_test.handzforhire;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomList extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;

    public CustomList(Activity a, ArrayList<HashMap<String, String>> d) {
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
            vi = inflater.inflate(R.layout.click_list, null);

        TextView job_name = (TextView) vi.findViewById(R.id.job_name_text);
        TextView date = (TextView) vi.findViewById(R.id.text3);
        TextView amount = (TextView) vi.findViewById(R.id.text7);
        TextView type = (TextView) vi.findViewById(R.id.duration);
        TextView jobId = (TextView) vi.findViewById(R.id.job_id);
        TextView rating = (TextView) vi.findViewById(R.id.rating);
        ImageView profile = (ImageView) vi.findViewById(R.id.img2);

        HashMap<String, String> items = new HashMap<String, String>();
        items = data.get(position);
        final String get_name = items.get("name");
        final String get_date = items.get("date");
        String get_amount = items.get("amount");
        String get_type = items.get("type");
        String get_id = items.get("jobId");
        String get_image = items.get("image");
        String average_rating = items.get("average_rating");

        job_name.setText(get_name);
        date.setText(get_date);
        amount.setText(get_amount);
        type.setText(get_type);
        jobId.setText(get_id);
        rating.setText(average_rating);


        DateFormat dateInstance = SimpleDateFormat.getDateInstance();
        DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat destDf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        try {
            java.util.Date dates = srcDf.parse(get_date);
            date.setText("" + destDf.format(dates));

        } catch (Exception e)
        {
            System.out.println("error " + e.getMessage());
        }

        Glide.with(activity).load(get_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(activity,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(profile);


        return vi;
    }

}