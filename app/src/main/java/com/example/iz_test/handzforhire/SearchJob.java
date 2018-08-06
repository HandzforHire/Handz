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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;
import java.util.Map;

public class SearchJob extends Activity implements SimpleGestureFilter.SimpleGestureListener{

    private static final String URL = Constant.SERVER_URL+"job_category_lists";

    LinearLayout layout;
    ProgressDialog progress_dialog;
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    String id,address,zipcode,state,city,name,job_category_name,job_id,categoryId,item,job_cat_name;
    static ArrayList<HashMap<String, String>> job_title = new ArrayList<HashMap<String, String>>();
    Spinner list;
    ImageView logo,img_arrow;
    TextView category_name;
    Button search;
    EditText zip,radius;
    CheckBox checkBox;
    LocationManager locationManager;
    String get_zipcode,get_radius,all_jobs,header,sub_category;
    Integer cat;
    ImageView img_dropdown;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static PopupWindow popupWindowDogs;
    CustomJobListAdapter adapter;
    public static TextView textview;
    public static ImageView img_paint;
    static String category="0",category_id="0";
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private SimpleGestureFilter detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_job);

        layout = (LinearLayout)findViewById(R.id.relay);
        category_name = (TextView)findViewById(R.id.cat_name);
        logo = (ImageView) findViewById(R.id.logo);
        search = (Button)findViewById(R.id.search);
        zip = (EditText) findViewById(R.id.zip);
        radius = (EditText) findViewById(R.id.radius);
        checkBox = (CheckBox) findViewById(R.id.checkBox1);
        img_arrow=(ImageView)findViewById(R.id.img_arrow);
        textview = (TextView) findViewById(R.id.textview);
        img_paint=(ImageView)findViewById(R.id.img_paint);
        Intent i = getIntent();
        id = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");
        System.out.println("iiiiiiiiiiiiiiiiiiiii:" + id);
        categoryId = "";
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryId = "";
                System.out.println("ssssssssssselected:job_cat_name:response:" + categoryId);
            }
        });

        detector = new SimpleGestureFilter(this,this);

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
                System.out.println("ccccccccccccc:categoryId:::"+categoryId+",,,"+get_zipcode+",,,,"+get_radius);

                if(categoryId==null&&TextUtils.isEmpty(get_zipcode)&&TextUtils.isEmpty(get_radius)&&!checkBox.isChecked()) {
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
                    i.putExtra("type","search");
                    i.putExtra("userId",Profilevalues.user_id);
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

        category_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(SearchJob.this);
                dialog.setContentView(R.layout.category_popup);

                expListView = (ExpandableListView) dialog.findViewById(R.id.lvExp);

                // preparing list data
                prepareListData();

                listAdapter = new ExpandableListAdapter(SearchJob.this, listDataHeader, listDataChild);

                // setting list adapter
                expListView.setAdapter(listAdapter);

                // Listview Group click listener
                expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v,
                                                int groupPosition, long id) {
                        // Toast.makeText(getApplicationContext(),
                        // "Group Clicked " + listDataHeader.get(groupPosition),
                        // Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

                // Listview Group expanded listener
                expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

                    @Override
                    public void onGroupExpand(int groupPosition) {
                        Toast.makeText(getApplicationContext(),
                                listDataHeader.get(groupPosition) + " Expanded",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                // Listview Group collasped listener
                expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

                    @Override
                    public void onGroupCollapse(int groupPosition) {
                        Toast.makeText(getApplicationContext(),
                                listDataHeader.get(groupPosition) + " Collapsed",
                                Toast.LENGTH_SHORT).show();

                    }
                });

                // Listview on child click listener
                expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,
                                                int groupPosition, int childPosition, long id) {
                        // TODO Auto-generated method stub
                        Toast.makeText(
                                getApplicationContext(),
                                listDataHeader.get(groupPosition)
                                        + " : "
                                        + listDataChild.get(
                                        listDataHeader.get(groupPosition)).get(
                                        childPosition), Toast.LENGTH_SHORT)
                                .show();
                        int pos = groupPosition+1;
                        categoryId = String.valueOf(pos);
                        header =  listDataHeader.get(groupPosition);
                        sub_category =  listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                        System.out.println("ccccccccc:"+header+",,"+sub_category+",,"+pos+"id:"+categoryId);
                        category_name.setText(header+" - "+sub_category);
                        dialog.dismiss();
                        return false;
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                return;

            }
        });

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

    public void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("CARE GIVING");
        listDataHeader.add("COACHING");
        listDataHeader.add("HOLIDAYS");
        listDataHeader.add("INSIDE THE HOME");
        listDataHeader.add("OUTSIDE THE HOME");
        listDataHeader.add("PERSONAL SERVICES");
        listDataHeader.add("PETCARE");
        listDataHeader.add("TUTORING");

        // Adding child data
        List<String> care = new ArrayList<String>();
        care.add("Babysitting");
        care.add("Elder Care");
        care.add("Other");
        care.add("Respite Care");

        List<String> coach = new ArrayList<String>();
        coach.add("Baseball");
        coach.add("Basketball");
        coach.add("Dance");
        coach.add("Field Hockey");
        coach.add("Football");
        coach.add("Gymnastics");
        coach.add("Ice Hockey");
        coach.add("Lacrosse");
        coach.add("Life Coach/Mentor");
        coach.add("Other");
        coach.add("Running");
        coach.add("Soccer");
        coach.add("Surfing");
        coach.add("Swimming");
        coach.add("Tennis");
        coach.add("Track & Field");
        coach.add("Volleyball");
        coach.add("Wrestling");


        List<String> holiday = new ArrayList<String>();
        holiday.add("Decorations (Setup or Cleanup)");
        holiday.add("Gift Wrapping");
        holiday.add("Holiday Baking");
        holiday.add("Holiday Shopping");
        holiday.add("Light Hanging");
        holiday.add("Other");
        holiday.add("Party Help (Setup, Cleanup, Serving)");
        holiday.add("Tree Delivery");
        holiday.add("Tree (Setup or Cleanup)");

        List<String> inside = new ArrayList<String>();
        inside.add("Box Packing / Unpacking");
        inside.add("Cleaning");
        inside.add("Electronics Setup");
        inside.add("Furniture Assembling");
        inside.add("Heavy Lifting / Moving");
        inside.add("House Sitting");
        inside.add("Organizing");
        inside.add("Other");
        inside.add("Painting");
        inside.add("Plant Care");
        inside.add("Smart Home Setup");
        inside.add("Wall Hanging");
        inside.add("Window Washing");

        List<String> outside = new ArrayList<String>();
        outside.add("Car Washing / Detailing");
        outside.add("Cleaning");
        outside.add("Gutters");
        outside.add("Digging / Shoveling");
        outside.add("Garage Organizing");
        outside.add("Heavy Lifting / Moving");
        outside.add("Landscaping");
        outside.add("Lawn Mowing");
        outside.add("Mulching");
        outside.add("Other");
        outside.add("Painting");
        outside.add("Planting");
        outside.add("Pool Cleaning");
        outside.add("Power Washing");
        outside.add("Snow Removal");
        outside.add("Window Washing");
        outside.add("Yard Cleanup");

        List<String> personal = new ArrayList<String>();
        personal.add("Cooking / Baking");
        personal.add("Data Entry");
        personal.add("Drop off / Pick up Driver");
        personal.add("Errands");
        personal.add("Event Server / Bartender");
        personal.add("Exercise Buddy");
        personal.add("Grocery Shopper");
        personal.add("Heavy Lifting / Moving");
        personal.add("Meal Preparation");
        personal.add("Other");
        personal.add("Party Help (Setup, Cleanup, Serving)");
        personal.add("Tax Preparation");
        personal.add("Wait in Line");

        List<String> pet = new ArrayList<String>();
        pet.add("Other");
        pet.add("Pet Bathing");
        pet.add("Pet Sitting");
        pet.add("Pet Walking");

        List<String> tutor = new ArrayList<String>();
        tutor.add("Computer Coding");
        tutor.add("Computer / Software");
        tutor.add("English");
        tutor.add("Foreign Language");
        tutor.add("Google Apps Training");
        tutor.add("Graphic Design (Photoshop/Illustrator)");
        tutor.add("History");
        tutor.add("Language Arts");
        tutor.add("Math");
        tutor.add("Microsoft Office");
        tutor.add("Musical Instrument");
        tutor.add("Other");
        tutor.add("Reading");
        tutor.add("Science");
        tutor.add("Singing / Voice");

        listDataChild.put(listDataHeader.get(0), care); // Header, Child data
        listDataChild.put(listDataHeader.get(1), coach);
        listDataChild.put(listDataHeader.get(2), holiday);
        listDataChild.put(listDataHeader.get(3), inside);
        listDataChild.put(listDataHeader.get(4), outside);
        listDataChild.put(listDataHeader.get(5), personal);
        listDataChild.put(listDataHeader.get(6), pet);
        listDataChild.put(listDataHeader.get(7), tutor);
    }


    @Override
    public void onSwipe(int direction) {
        String str = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
                Intent j = new Intent(getApplicationContext(), SwitchingSide.class);
                startActivity(j);
                finish();
                break;
            case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";

                Intent  i = new Intent(getApplicationContext(), LendProfilePage.class);
                i.putExtra("userId", Profilevalues.user_id);
                i.putExtra("address", Profilevalues.address);
                i.putExtra("city", Profilevalues.city);
                i.putExtra("state", Profilevalues.state);
                i.putExtra("zipcode", Profilevalues.zipcode);
                startActivity(i);
                finish();

                break;
            case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
                break;

        }
        //  Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDoubleTap() {

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event){

        this.detector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }
}
