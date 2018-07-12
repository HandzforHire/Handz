package com.example.iz_test.handzforhire;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.pwittchen.swipe.library.rx2.Swipe;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class FindJobMap extends Fragment implements GoogleMap.OnMarkerClickListener,GoogleMap.OnMyLocationChangeListener,GoogleMap.OnCameraChangeListener,ResponseListener,OnMapReadyCallback{

   Context context ;
    private static final String URL = Constant.SERVER_URL+"job_lists";
    public static String XAPP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    String user_id;
    ArrayList<HashMap<String,String>> disclosed = new ArrayList<HashMap<String,String>>();
    private GoogleMap googleMap;
    GPSTracker gps;
    public static int  MY_PERMISSIONS_REQUEST_READ_CONTACTS=1;
    public static Double lat,lon;
    TextView txt_undisclosedjob;
    ImageView logo,menu;
    private static Hashtable<String, String> markers =new Hashtable<String, String>();
    private static ArrayList<String> undisclosedjobs=new ArrayList<String>();
    SessionManager session;
    String id,address,city,state,zipcode,profile_image,profilename,email,user_name;

    public static Fragment fragments;
    View rootView;
    int undisclosedjob=0;
    Swipe swipe;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.find_job_map, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
        }
        SupportMapFragment fragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        fragment.getMapAsync(this);


        session = new SessionManager(getActivity());
        HashMap<String, String> user = session.getUserDetails();
        // get name
        user_name = user.get(SessionManager.USERNAME);
        id = user.get(SessionManager.ID);
        email = user.get(SessionManager.EMAIL);
        address = user.get(SessionManager.ADDRESS);
        city = user.get(SessionManager.CITY);
        state = user.get(SessionManager.STATE);
        zipcode = user.get(SessionManager.ZIPCODE);

       /* Intent intent = getIntent();
        user_id = intent.getStringExtra("userId");*/
        user_id=user.get(SessionManager.ID);
        System.out.println("iiiiiiiiiiii:userid:findjob:"+user_id);

        txt_undisclosedjob=(TextView)rootView.findViewById(R.id.txt_undisclosedjob);
        logo = (ImageView) rootView.findViewById(R.id.logo);
        menu = (ImageView) rootView.findViewById(R.id.menu);

        //initilizeMap();
        /// Changing map type

        // create class object
        gps = new GPSTracker(getActivity());



        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LendProfilePage.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
                getActivity().finish();
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), LendProfilePage.class);
                i.putExtra("userId", id);
                i.putExtra("address", address);
                i.putExtra("city", city);
                i.putExtra("state", state);
                i.putExtra("zipcode", zipcode);
                startActivity(i);
                getActivity().finish();
            }
        });


        txt_undisclosedjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(undisclosedjob>0){
                   Intent in_job_list=new Intent(getActivity(),SearchJob.class);
                    startActivity(in_job_list);
                }
            }
        });

        return rootView;
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_job_map);

        *//*MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);*//*
    }

    @Override
    protected void onResume() {
        super.onResume();
        //initilizeMap();
    }*/

   /* private void initilizeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }*/

    private double[] createRandLocation(double latitude, double longitude) {

        return new double[]{latitude + ((Math.random() - 0.5) / 500),
                longitude + ((Math.random() - 0.5) / 500),
                150 + ((Math.random() - 0.5) * 10)};
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (markers.get(marker.getId()) != null) {
            try {
                JSONObject obj = new JSONObject(markers.get(marker.getId()));
                showDetails(obj.toString(),getActivity());
            }catch (Exception e){
                System.out.println("Exception e test "+e.getMessage());
            }
        }

        return false;
    }

    public void showDetails(String obj,Activity activity){

        try{
            System.out.println("obj "+obj);
            AlertDialog.Builder builder=new AlertDialog.Builder(activity);
            JSONObject object=new JSONObject(obj);
            System.out.println("job cat "+object);
            String job_name=object.getString("job_name");
            String recurring=object.getString("recurring");
            String job_payment_amount=object.getString("job_payment_amount");
            String job_date=object.getString("job_date");
            String profile_image=object.getString("profile_image");
            String duration=object.getString("job_payment_type");
            LayoutInflater inflater = LayoutInflater.from(activity);
            View view = inflater.inflate(R.layout.findjob_popup, null);
            builder.setView(view);
            final AlertDialog alert = builder.create();

            TextView txt_jobcat=(TextView)view.findViewById(R.id.txt_jobcat);
            TextView txt_amount=(TextView)view.findViewById(R.id.txt_amount);
            TextView txt_when=(TextView)view.findViewById(R.id.txt_when);
            TextView txt_dur=(TextView)view.findViewById(R.id.txt_duration);
            ImageView img_profile=(ImageView)view.findViewById(R.id.img_profile);
            ImageView img_close=(ImageView)view.findViewById(R.id.img_close);

            txt_jobcat.setText(job_name);
            txt_amount.setText("PAY $"+job_payment_amount);
            txt_dur.setText("EXPECTED DURATION: "+duration);


            DateFormat srcDf = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat destDf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
            try{
                Date dates = srcDf.parse(job_date);
                System.out.println("converted "+destDf.format(dates));
                txt_when.setText("WHEN: "+destDf.format(dates));

            }catch (Exception e)
            {
                System.out.println("error "+e.getMessage());
            }
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

            Picasso.with(activity).load(profile_image).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.drawable.default_profile)
                    .into(img_profile);
            alert.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alert.show();


        }catch (Exception e){
            System.out.println("Exception "+e.getMessage());
        }
    }

    @Override
    public void onMyLocationChange(Location location) {

    }


    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        System.out.println("cameraPosition"+cameraPosition);
        try {
            LatLng latLng=cameraPosition.target;
            lat=latLng.latitude;
            lon=latLng.longitude;
            RestClientPost rest = new RestClientPost(getActivity(), 1);
            rest.execute(RestClientPost.RequestMethod.POST, getActivity(),FindJobMap.this);
        }catch (Exception e){
            System.out.println("exception "+e.getMessage());
        }
    }

    @Override
    public void onResponseReceived(JSONObject responseObj, int requestType) {
        try{
            String status=responseObj.getString("status");
            System.out.println("response "+responseObj);
            if(status.equals("error"))
            {
                undisclosedjob=0;
                txt_undisclosedjob.setText("0 disclosed Locations");
            }else {
                googleMap.clear();
                JSONArray joblist = responseObj.getJSONArray("job_lists");
                for (int i = 0; i < joblist.length(); i++) {
                    JSONObject object = joblist.getJSONObject(i);
                    String postaddress = object.getString("post_address");
                    if (postaddress.equals("yes")) {
                        String lat = object.getString("lat");
                        String lon = object.getString("lon");
                        String job_category=object.getString("job_category");
                        Bitmap icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_1);
                        if(job_category.equals("Painting (Interior / Exterior)")){
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_1);
                        }else if(job_category.equals("Moving Items"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_2);
                        }
                        else if(job_category.equals("Heavy Lifting"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_3);
                        }
                        else if(job_category.equals("Unpacking Boxes"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_4);
                        }
                        else if(job_category.equals("Landscaping"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_5);
                        }
                        else if(job_category.equals("Lawnmowing"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_6);
                        }
                        else if(job_category.equals("Raking Leaves"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_7);

                        }
                        else if(job_category.equals("Babysitting"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_8);

                        }
                        else if(job_category.equals("Digging (trench/hole)"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_9);

                        }
                        else if(job_category.equals("Assembling Furniture/Object"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_10);

                        }
                        else if(job_category.equals("Dog Walking"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_11);

                        }
                        else if(job_category.equals("Pet Care"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_12);

                        }
                        else if(job_category.equals("Workout Partner/Coach"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_13);

                        }
                        else if(job_category.equals("Server(s) for Dinner Party"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_14);

                        }
                        else if(job_category.equals("Bartender for House Party"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_15);

                        }
                        else if(job_category.equals("Shoveling Snow"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_16);

                        }
                        else if(job_category.equals("Apprentice for Skilled Laborer"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_17);

                        }
                        else if(job_category.equals("Cleaning"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_18);

                        }
                        else if(job_category.equals("Organizing"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_19);

                        }
                        else if(job_category.equals("Food Shopping"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_20);

                        }
                        else if(job_category.equals("Other"))
                        {
                            icon=getBitmapFromVectorDrawable(getActivity().getApplicationContext(), R.drawable.ic_21);

                        }

                        MarkerOptions marker = new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lon))).icon(BitmapDescriptorFactory.fromBitmap(icon));
                        googleMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) FindJobMap.this);
                        final Marker groupMarker = googleMap.addMarker(marker);
                        markers.put(groupMarker.getId(), object.toString());
                    }else{
                        undisclosedjobs.add(object.toString());
                    }
                }
                undisclosedjob=undisclosedjobs.size();
                if(undisclosedjobs.size()>0)
                    txt_undisclosedjob.setText(undisclosedjobs.size()+" Additional Undisclosed Locations\n Within the Parameters of this map\n(Click Here for ListView the Job Details)");
                else
                    txt_undisclosedjob.setText("0 Undisclosed Locations " );

            }

        }catch (Exception e){
            System.out.println("Error "+e.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap=map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.setOnCameraChangeListener(this);
        googleMap.setOnMyLocationChangeListener(this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latitude,
                            longitude)).zoom(15).build();
            lat=latitude;
            lon=longitude;
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            try {
                RestClientPost rest = new RestClientPost(getActivity(), 1);
                rest.execute(RestClientPost.RequestMethod.POST, getActivity(),FindJobMap.this);
            }catch (Exception e){
                System.out.println("exception "+e.getMessage());
            }
            // \n is for new line
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

}
