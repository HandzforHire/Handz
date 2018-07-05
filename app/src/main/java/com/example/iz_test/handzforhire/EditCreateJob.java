package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditCreateJob extends Activity implements View.OnClickListener {

    Spinner list;
    LinearLayout layout;
    String id, address, zipcode, state, city, name, category,description,date,start_time,end_time,amount,st_time,en_time, type;
    private static final String URL = Constant.SERVER_URL+"job_category_lists";
    private static final String GET_JOB = Constant.SERVER_URL+"job_detail_view";
    Button next;
    EditText job_name, job_description,payamount;
    static TextView date_text;
    TextView start_time_text;
    TextView end_time_text;
    TextView job_amount,symbol;
    TextView pay_text;
    TextView amount_text,textview;
    ImageView img_arrow;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ImageView logo,arrow;
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String JOB_ID = "job_id";
    String value = "HandzForHire@~";
    ArrayList<HashMap<String, String>> job_title = new ArrayList<HashMap<String, String>>();
    String job_category_name, job_id,jobId,paytext,pay_amount,flexible_status,job_estimated,hourr,mintt,secc;
    ProgressDialog progress_dialog;
    RelativeLayout pay_lay,payment_layout,date_layout,time_layout,estimate_layout;
    CheckBox checkBox;
    Activity activity;

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
        setContentView(R.layout.edit_create_job);

        /*progress_dialog = new ProgressDialog(this);
        progress_dialog.setMessage("Loading.Please wait....");
        progress_dialog.show();*/

        layout = (LinearLayout)findViewById(R.id.relay);
        next = (Button) findViewById(R.id.next);
        job_name = (EditText) findViewById(R.id.descrip);
        job_description = (EditText) findViewById(R.id.detail);
        date_text = (TextView) findViewById(R.id.date_text);
        start_time_text = (TextView) findViewById(R.id.start_time_text);
        end_time_text = (TextView) findViewById(R.id.end_time_text);
        job_amount = (TextView) findViewById(R.id.amount);
        amount_text = (TextView) findViewById(R.id.pay_type);
        pay_text = (TextView) findViewById(R.id.payment_details);
        symbol = (TextView) findViewById(R.id.symbol);
        logo = (ImageView) findViewById(R.id.logo);
        arrow = (ImageView) findViewById(R.id.arrow);
        list = (Spinner)findViewById(R.id.listview);
        pay_lay = (RelativeLayout) findViewById(R.id.linear4);
        payment_layout = (RelativeLayout) findViewById(R.id.relay1);
        date_layout = (RelativeLayout) findViewById(R.id.linear1);
        time_layout = (RelativeLayout) findViewById(R.id.linear2);
        estimate_layout = (RelativeLayout) findViewById(R.id.linear3);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        textview = (TextView) findViewById(R.id.textview);
        img_arrow=(ImageView)findViewById(R.id.img_arrow);

        Intent i = getIntent();
        id = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");
        jobId = i.getStringExtra("jobId");
        System.out.println("iiiiiiiiiiiiiiiiiiiii:jobId::::" + jobId);

        String pattern2 = "hh:mm:ss";
        st_time = new SimpleDateFormat(pattern2).format(new Date());
        en_time = new SimpleDateFormat(pattern2).format(new Date());
        System.out.println("777777777:time::::" + st_time+",,,,"+ en_time);
        activity=this;

        listCategory();
        getJobDetails();

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        date_text.setOnClickListener(this);
        start_time_text.setOnClickListener(this);
        end_time_text.setOnClickListener(this);

        list.setVisibility(View.VISIBLE);
        //list.performClick();
        img_arrow.setVisibility(View.GONE);
        textview.setVisibility(View.GONE);
        pay_lay.setVisibility(View.GONE);

        /*list.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/

        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.setVisibility(View.VISIBLE);
                list.performClick();
                img_arrow.setVisibility(View.GONE);
                textview.setVisibility(View.GONE);

            }
        });

        img_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.setVisibility(View.VISIBLE);
                list.performClick();
                img_arrow.setVisibility(View.GONE);
                textview.setVisibility(View.GONE);

            }
        });

        job_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("hhhhhhhhhhai:");
                final Dialog dialog = new Dialog(EditCreateJob.this);
                dialog.setContentView(R.layout.payment_details);

                ImageView close = (ImageView) dialog.findViewById(R.id.close_btn);
                ImageView logo = (ImageView) dialog.findViewById(R.id.logo);
                payamount = (EditText) dialog.findViewById(R.id.amount);
                Button update = (Button) dialog.findViewById(R.id.update_btn);
                final TextView text = (TextView) dialog.findViewById(R.id.text);

                payamount.addTextChangedListener(tw);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                logo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pay_amount = payamount.getText().toString().trim();
                        paytext = text.getText().toString().trim();

                        payment_layout.setVisibility(View.VISIBLE);
                        job_amount.setText(pay_amount);
                        amount_text.setText(paytext);
                        pay_text.setVisibility(View.GONE);
                        symbol.setVisibility(View.GONE);
                        arrow.setVisibility(View.GONE);
                        dialog.dismiss();
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                dialog.getWindow().

                        setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                return;

            }
        });

    }

    @Override
    public void onClick(View v) {

        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

        if (v == date_text) {

            DialogFragment dialogfragment = new datepickerClass();

            dialogfragment.show(getFragmentManager(), "DatePickerDialog");

           /* // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            date_text.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();*/
        }
        if (v == start_time_text) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            int second = 00;
                            int hour = hourOfDay;
                            int minutes = minute;
                            String sec = (second < 10) ? "0" + second : "" + second;
                            String min = (minutes < 10) ? "0" + minutes : "" + minutes;
                            String hour_day = (hour < 10) ? "0" + hour : "" + hour;
                            start_time = hour_day + ":" + min + ":" + sec;
                            System.out.println("77777777:start_time::::::"+start_time);

                            hourr = hour_day;
                            System.out.println("77777777:hourr::::::"+hourr);

                            String timeSet = "";
                            if (hour > 12) {
                                hour -= 12;
                                timeSet = "PM";
                            } else if (hour == 0) {
                                hour += 12;
                                timeSet = "AM";
                            }
                            else if (hour == 12){
                                timeSet = "PM";
                            }else{
                                timeSet = "AM";
                            }

                            String min1 = "";
                            if (minutes < 10)
                            {
                                min1 = "0" + minutes ;
                            } else {
                                min1 = String.valueOf(minutes);
                            }
                            String hour_day1 = "";
                            if(hour < 10)
                            {
                                hour_day1 = "0" + hour ;
                            }
                            else
                            {
                                hour_day1 = String.valueOf(hour);
                            }
                            start_time_text.setText(hour_day1 + ":" + min1 + " " + timeSet);
                            //start_time_text.setText(start_time);

                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (v == end_time_text) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            int second = 00;
                            int hour = hourOfDay;
                            int minutes = minute;
                            String sec = (second < 10) ? "0" + second : "" + second;
                            String min = (minutes < 10) ? "0" + minutes : "" + minutes;
                            String hour_day = (hour < 10) ? "0" + hour : "" + hour;
                            end_time = hour_day + ":" + min + ":" + sec;
                            System.out.println("77777777:end_time::::::"+end_time);

                            String timeSet = "";
                            if (hour > 12) {
                                hour -= 12;
                                timeSet = "PM";
                            } else if (hour == 0) {
                                hour += 12;
                                timeSet = "AM";
                            }
                            else if (hour == 12){
                                timeSet = "PM";
                            }else{
                                timeSet = "AM";
                            }

                            String min1 = "";
                            if (minutes < 10)
                            {
                                min1 = "0" + minutes ;
                            } else {
                                min1 = String.valueOf(minutes);
                            }
                            String hour_day1 = "";
                            if(hour < 10)
                            {
                                hour_day1 = "0" + hour ;
                            }
                            else
                            {
                                hour_day1 = String.valueOf(hour);
                            }
                            end_time_text.setText(hour_day1 + ":" + min1 + " " + timeSet);
                            //end_time_text.setText(end_time);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }

    public void validate() {
        name = job_name.getText().toString().trim();
        description = job_description.getText().toString().trim();
        date = date_text.getText().toString().trim();
        start_time = start_time_text.getText().toString();
        end_time = end_time_text.getText().toString();
        amount = job_amount.getText().toString().trim();
        type = amount_text.getText().toString().trim();

        amount=amount.substring(1);
        System.out.println("amount "+amount);

        if (checkBox.isChecked())
        {
            flexible_status = "yes";
        }
        else
        {
            flexible_status = "no";
        }

        System.out.println("eeeeeeeee:time:::"+hourr+"...."+amount);
        job_estimated = String.valueOf(Float.valueOf(hourr)*Float.valueOf(amount));
        System.out.println("eeeeeeeee:estimated:::"+job_estimated);
        Intent i = new Intent(EditCreateJob.this, EditCreateJob2.class);
        i.putExtra("userId", id);
        i.putExtra("address", address);
        i.putExtra("city", city);
        i.putExtra("state", state);
        i.putExtra("zipcode", zipcode);
        i.putExtra("name",name);
        i.putExtra("category",category);
        i.putExtra("description",description);
        i.putExtra("job_id",jobId);
        i.putExtra("date", date);
        i.putExtra("start_time",st_time);
        i.putExtra("end_time",en_time);
        i.putExtra("payment_amount",amount);
        i.putExtra("payment_type", type);
        i.putExtra("flexible_status", flexible_status);
        i.putExtra("estimated_amount", job_estimated);
        startActivity(i);
    }

    public void getJobDetails()
    {

        dialog = new Dialog(EditCreateJob.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_JOB,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:new:get:job:" + response);
                        onResponserecieved(response, 2);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            System.out.println("error" + jsonObject);
                        } catch (JSONException e) {
                            //Handle a malformed json response
                        } catch (UnsupportedEncodingException error1) {

                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(JOB_ID, jobId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved(String jsonobject, int i) {
        System.out.println("response from interface" + jsonobject);

        String status = null;
        String job_data = null;

        try {
            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            if (status.equals("success")) {
                job_data = jResult.getString("job_data");
                System.out.println("jjjjjjjjjjjjjjjob:::job_data:::" + job_data);
                JSONObject object = new JSONObject(job_data);
                String get_name = object.getString("job_name");
                System.out.println("nnnnnnnnnnn:name::"+get_name);
                category = object.getString("job_category");
                System.out.println("nnnnnnnnnnn:category::" + category);
                String get_description = object.getString("description");
                System.out.println("nnnnnnnnnnn:description::" + get_description);
                String get_date = object.getString("job_date");
                System.out.println("nnnnnnnnnnn:date::" + get_date);
                String get_start_time = object.getString("start_time");
                System.out.println("nnnnnnnnnnn:start_time::" + get_start_time);
                String get_end_time = object.getString("end_time");
                System.out.println("nnnnnnnnnnn:end_time::" + get_end_time);
                String get_amount = object.getString("job_payment_amount");
                System.out.println("nnnnnnnnnnn:amount::" + get_amount);
                String get_type = object.getString("job_payment_type");
                System.out.println("nnnnnnnnnnn:type::" + get_type);
                String flexible = object.getString("job_date_time_flexible");
                System.out.println("nnnnnnnnnnn:flexible::" + flexible);

                String[] arrayString = get_end_time.split(":");

                 hourr = arrayString[0];
                 mintt = arrayString[1];
                 secc = arrayString[2];
                System.out.println("eeeeeeeee:eeeeee:::"+hourr+"...."+mintt+"...."+secc);

                job_name.setText(get_name);
                job_description.setText(get_description);
                date_text.setText(get_date);
                start_time_text.setText(get_start_time);
                end_time_text.setText(get_end_time);
                pay_text.setVisibility(View.GONE);
                arrow.setVisibility(View.GONE);
                payment_layout.setVisibility(View.VISIBLE);
                job_amount.setText(get_amount);
                amount_text.setText(get_type);
                if(flexible.equals("yes"))
                {
                    checkBox.setChecked(true);
                }

                if(category.equals("1")) {
                    int position = 0;
                    list.setSelection(position);
                }
                if(category.equals("2")) {
                    int position = 1;
                    list.setSelection(position);
                }
                if(category.equals("3")) {
                    int position = 2;
                    list.setSelection(position);
                }
                if(category.equals("4")) {
                    int position = 3;
                    list.setSelection(position);
                }
                if(category.equals("5")) {
                    int position = 4;
                    list.setSelection(position);
                }
                if(category.equals("6")) {
                    int position = 5;
                    list.setSelection(position);
                }
                if(category.equals("7")) {
                    int position = 6;
                    list.setSelection(position);
                }
                if(category.equals("8")) {
                    int position = 7;
                    list.setSelection(position);
                }
                if(category.equals("9")) {
                    int position = 8;
                    list.setSelection(position);
                }
                if(category.equals("10")) {
                    int position = 9;
                    list.setSelection(position);
                }
                if(category.equals("11")) {
                    int position = 10;
                    list.setSelection(position);
                }
                if(category.equals("12")) {
                    int position = 11;
                    list.setSelection(position);
                }
                if(category.equals("13")) {
                    int position = 12;
                    list.setSelection(position);
                }
                if(category.equals("14")) {
                    int position = 13;
                    list.setSelection(position);
                }
                if(category.equals("15")) {
                    int position = 14;
                    list.setSelection(position);
                }
                if(category.equals("16")) {
                    int position = 15;
                    list.setSelection(position);
                }
                if(category.equals("17")) {
                    int position = 16;
                    list.setSelection(position);
                }
                if(category.equals("18")) {
                    int position = 17;
                    list.setSelection(position);
                }
                if(category.equals("19")) {
                    int position = 18;
                    list.setSelection(position);
                }
                if(category.equals("20")) {
                    int position = 19;
                    list.setSelection(position);
                }
                if(category.equals("21")) {
                    int position = 20;
                    list.setSelection(position);
                }

            } else {
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void listCategory() {
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
                       /* try {
                            String responseBody = new String(error.networkResponse.data, "utf-8");
                            JSONObject jsonObject = new JSONObject(responseBody);
                            System.out.println("error" + jsonObject);
                        } catch (JSONException e) {
                            //Handle a malformed json response
                        } catch (UnsupportedEncodingException error1) {

                        }*/
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
            System.out.println("jjjjjjjjjjjjjjjob:::categories:::" + categories);
            if (status.equals("success")) {
                JSONArray array = new JSONArray(categories);
                for (int n = 0; n < array.length(); n++) {
                    JSONObject object = (JSONObject) array.get(n);
                    job_category_name = object.getString("name");
                    System.out.println(":job_category_name::" + job_category_name);
                    job_id = object.getString("id");
                    System.out.println(":job_id::" + job_id);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("job_category", job_category_name);
                    map.put("job_id", job_id);
                    job_title.add(map);
                    System.out.println("menuitems:::" + job_title);
                }

                CustomJobListAdapter adapter = new CustomJobListAdapter(EditCreateJob.this, job_title, imageId);
                list.setAdapter(adapter);
                list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        category = ((TextView) view.findViewById(R.id.id)).getText().toString();
                        System.out.println("ssssssssssselected:item:" + category);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            } else {
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static class datepickerClass extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();

            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_LIGHT,this,year,month,day);
            datepickerdialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            return datepickerdialog;

        }


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub


            //TextView textview = (TextView) getActivity().findViewById(R.id.textView1);

            Calendar calander2 = Calendar.getInstance();

            calander2.setTimeInMillis(0);

            calander2.set(year, monthOfYear, dayOfMonth, 0, 0, 0);

            Date SelectedDate = calander2.getTime();

            DateFormat dateformat_US = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
            String StringDateformat_US = dateformat_US.format(SelectedDate);
            date_text.setText(StringDateformat_US);

            /*DateFormat dateformat_UK = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
            String StringDateformat_UK = dateformat_UK.format(SelectedDate);
            date_text.setText(date_text.getText() + StringDateformat_UK + "\n");*/

        }
    }

    TextWatcher tw = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (!s.toString().matches("^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {
                String userInput = "" + s.toString().replaceAll("[^\\d]", "");
                StringBuilder cashAmountBuilder = new StringBuilder(userInput);
                while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                    cashAmountBuilder.deleteCharAt(0);
                }
                while (cashAmountBuilder.length() < 3) {
                    cashAmountBuilder.insert(0, '0');
                }
                cashAmountBuilder.insert(cashAmountBuilder.length() - 2, '.');

                payamount.removeTextChangedListener(this);
                payamount.setText(cashAmountBuilder.toString());

                payamount.setTextKeepState("$" + cashAmountBuilder.toString());
                Selection.setSelection(payamount.getText(), cashAmountBuilder.toString().length() + 1);

                payamount.addTextChangedListener(this);
            }
        }
    };
}