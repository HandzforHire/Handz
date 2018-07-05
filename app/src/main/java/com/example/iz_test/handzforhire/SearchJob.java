package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchJob extends Activity {

    private static final String URL = Constant.SERVER_URL+"job_category_lists";

    LinearLayout layout;
    ProgressDialog progress_dialog;
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    String id,address,zipcode,state,city,name,job_category_name,job_id,categoryId,item,job_cat_name;
    ArrayList<HashMap<String, String>> job_title = new ArrayList<HashMap<String, String>>();
    Spinner list;
    ImageView logo;
    Button search;
    EditText zip,radius;
    CheckBox checkBox;
    LocationManager locationManager;
    String get_zipcode,get_radius,all_jobs;
    Integer cat;
    TextView textview;
    ImageView img_dropdown;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static PopupWindow popupWindowDogs;
    CustomJobListAdapter adapter;

    Integer[] imageId = {
            R.drawable.box_1,
            R.drawable.box_2,
            R.drawable.box_3,
            R.drawable.box_4,
            R.drawable.box_5,
            R.drawable.box_6,
            R.drawable.box_7,
            R.drawable.box_8,
            R.drawable.box_9,
            R.drawable.box_10,
            R.drawable.box_11,
            R.drawable.box_12,
            R.drawable.box_13,
            R.drawable.box_14,
            R.drawable.box_15,
            R.drawable.box_16,
            R.drawable.box_17,
            R.drawable.box_18,
            R.drawable.box_19,
            R.drawable.box_20,
            R.drawable.box_21,

    };
Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_job);

      /*  progress_dialog = new ProgressDialog(this);
        progress_dialog.setMessage("Loading.Please wait....");
        progress_dialog.show();*/

        dialog = new Dialog(SearchJob.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        layout = (LinearLayout)findViewById(R.id.relay);
        list = (Spinner)findViewById(R.id.listview);
        logo = (ImageView) findViewById(R.id.logo);
        search = (Button)findViewById(R.id.search);
        zip = (EditText) findViewById(R.id.zip);
        radius = (EditText) findViewById(R.id.radius);
        checkBox = (CheckBox) findViewById(R.id.checkBox1);
        img_dropdown=(ImageView)findViewById(R.id.img_dropdown);
        textview=(TextView)findViewById(R.id.textview);
        Intent i = getIntent();
        id = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");
        System.out.println("iiiiiiiiiiiiiiiiiiiii:" + id);

        listPostedJobs();

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        list.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryId = "";
                System.out.println("ssssssssssselected:job_cat_name:response:" + categoryId);
            }
        });


        /*list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                job_cat_name = parent.getItemAtPosition(position).toString();
                System.out.println("ssssssssssselected:job_cat_name:" + job_cat_name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SearchJob.this, "nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });*/

        popupWindowDogs = popupWindowDogs();
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*list.setVisibility(View.VISIBLE);
                list.performClick();

                if(list.getSelectedItem() == null) { // user selected nothing...
                    list.performClick();
                }
                img_arrow.setVisibility(View.GONE);
                textview.setVisibility(View.GONE);*/
                popupWindowDogs.showAsDropDown(v, -5, 0);

            }
        });

        img_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindowDogs.showAsDropDown(v, -5, 0);

            }
        });
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                get_zipcode = zip.getText().toString().trim();
                get_radius = radius.getText().toString().trim();

                if(job_cat_name.equals("0")&&TextUtils.isEmpty(get_zipcode)&&TextUtils.isEmpty(get_radius)&&!checkBox.isChecked()) {
                    final Dialog dialog = new Dialog(SearchJob.this);
                    dialog.setContentView(R.layout.custom_dialog);

                    // set the custom dialog components - text, image and button
                    TextView text = (TextView) dialog.findViewById(R.id.text);
                    text.setText("Select atleast one value");
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
                }
                else
                {
                    if(checkBox.isChecked())
                    {
                        all_jobs = "all_jobs";
                        categoryId = "";
                        get_zipcode = "";
                        get_radius = "";
                    }
                    else
                    {
                        all_jobs = "";
                    }
                    Intent i = new Intent(SearchJob.this,ViewSearchJob.class);
                    i.putExtra("userId",id);
                    i.putExtra("address",address);
                    i.putExtra("city",city);
                    i.putExtra("state",state);
                    i.putExtra("zipcode",zipcode);
                    i.putExtra("categoryId",categoryId);
                    i.putExtra("zip",get_zipcode);
                    i.putExtra("radius",get_radius);
                    i.putExtra("alljobs",all_jobs);
                    startActivity(i);
                }
            }
        });
    }

    public void listPostedJobs() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:" + response);
                        onResponserecieved1(response, 2);
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
                        } catch ( JSONException e ) {
                            //Handle a malformed json response
                        } catch (UnsupportedEncodingException error1){

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(KEY_USERID, id);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved1(String jsonobject, int i) {
        System.out.println("response from interface" + jsonobject);

        String status = null;
        String categories = null;

        try {
            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            categories = jResult.getString("categories");
            System.out.println("jjjjjjjjjjjjjjjob:::categories:::"+categories);
            if(status.equals("success"))
            {
                JSONArray array = new JSONArray(categories);
                for(int n = 0; n < array.length(); n++)
                {
                    JSONObject object = (JSONObject) array.get(n);
                    job_category_name = object.getString("name");
                    System.out.println(":job_category_name::" + job_category_name);
                    job_id = object.getString("id");
                    System.out.println(":job_id::" + job_id);

                    HashMap<String,String> map = new HashMap<String,String>();
                    map.put("job_category", job_category_name);
                    map.put("job_id", job_id);
                    job_title.add(map);
                    System.out.println("menuitems:::" + job_title);
                }

                CustomJobListAdapter adapter = new CustomJobListAdapter(SearchJob.this, job_title,imageId);
                list.setAdapter(adapter);
                list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        view.setSelected(true);
                        job_cat_name = parent.getItemAtPosition(position).toString();
                        String value = "1";
                        cat = Integer.parseInt(job_cat_name)+ Integer.parseInt(value);
                        categoryId = String.valueOf(cat);
                        System.out.println("ssssssssssselected:categoryId:response11:" + categoryId);
                        checkBox.setChecked(false);
                        all_jobs = "";
                        job_cat_name = categoryId;
                        System.out.println("ssssssssssselected:job_cat_name:response22:" + job_cat_name);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                       /* categoryId = "";
                        job_cat_name = "";*/
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

    /*@Override
    protected void onResume() {
        super.onResume();
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    *//* Remove the locationlistener updates when Activity is paused *//*
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        double lat = (double) (location.getLatitude());
        double lng = (double) (location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    public void permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasSMSPermission = checkSelfPermission(ACCESS_COARSE_LOCATION);
            if (hasSMSPermission != PackageManager.PERMISSION_GRANTED) {
                if (!shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)) {
                    showMessageOKCancel("You need to allow access to Access Fine Location",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{ACCESS_COARSE_LOCATION},
                                                MY_PERMISSIONS_REQUEST_LOCATION);
                                    }
                                }
                            });
                    return;
                }
                requestPermissions(new String[]{ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
                return;
            }
        }
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access", Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)) {
                            showMessageOKCancel("You need to allow access to both the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{ACCESS_COARSE_LOCATION},
                                                        MY_PERMISSIONS_REQUEST_LOCATION);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new android.support.v7.app.AlertDialog.Builder(SearchJob.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }*/

    public PopupWindow popupWindowDogs() {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(this);

        // the drop down list is a list view


        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popupbackground,null);

        ListView listViewDogs =(ListView) layout.findViewById(R.id.list_category);;
        // set our adapter and pass our pop up window contents
        adapter = new CustomJobListAdapter(SearchJob.this, job_title,imageId);
        listViewDogs.setAdapter(adapter);

        // set the item click listener
        listViewDogs.setOnItemClickListener(new DogsDropdownOnItemClickListener());

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(600);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // popupWindow.setBackgroundDrawable(R.layout.popupbackground);

        // set the list view as pop up window content
        popupWindow.setContentView(layout);

        return popupWindow;
    }

}
