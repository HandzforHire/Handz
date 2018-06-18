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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
        TextView type = (TextView) vi.findViewById(R.id.text8);
        TextView jobId = (TextView) vi.findViewById(R.id.job_id);
        ImageView def = (ImageView) vi.findViewById(R.id.img1);
        ImageView profile = (ImageView) vi.findViewById(R.id.img2);

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
        String get_id = items.get("jobId");
        System.out.println("iiiiiiiiiiiiiiiiiiid:get_id::" + get_id);
        String get_image = items.get("image");
        System.out.println("iiiiiiiiiiiiiiiiiiid:get_image::" + get_image);

        job_name.setText(get_name);
        date.setText(get_date);
        amount.setText(get_amount);
        type.setText(get_type);
        jobId.setText(get_id);

        if(get_image.equals(""))
        {
            def.setVisibility(View.VISIBLE);
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
            def.setVisibility(View.INVISIBLE);
            profile.setImageBitmap(bmp);
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