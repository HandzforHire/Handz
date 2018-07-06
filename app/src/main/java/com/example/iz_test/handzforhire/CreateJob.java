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
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;

import android.text.Selection;

import android.text.TextUtils;
import android.text.TextWatcher;

import android.view.LayoutInflater;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bigkoo.pickerview.MyOptionsPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CreateJob extends Activity implements View.OnClickListener {

    Spinner list;
    LinearLayout layout;
    String id,address,zipcode,state,city,name,description,date,start_time,end_time,amount,type,st_time,en_time;
    private static final String URL = Constant.SERVER_URL+"job_category_lists";
    Button next;
    static String category="0",categoryId="0";
    EditText job_name,job_description;
    static TextView date_text;
    TextView start_time_text;
    TextView end_time_text;
    TextView job_amount;
    TextView pay_text;
    TextView amount_text,hour;
    public static TextView textview;
    public static ImageView img_paint;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ImageView logo,arrow,img_arrow;
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    static ArrayList<HashMap<String, String>> job_title = new ArrayList<HashMap<String, String>>();
    String job_category_name,job_id,payment_type,pay_amount,flexible_status,job_estimated,paytext;
    RelativeLayout pay_lay,payment_layout,date_layout,time_layout,estimate_layout;
    Integer cat;
    CheckBox checkBox;
    private String current = "";
    EditText payamount;
    Activity activity;
    private static final String BEGIN_WITH_DOLLAR = "$###,###.##";
    CustomJobListAdapter adapter;
    public static PopupWindow popupWindowDogs;
    public static Button buttonShowDropDown;
    MyOptionsPickerView threePicker;
    Dialog dialog;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_job);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

<<<<<<< HEAD
        dialog = new Dialog(CreateJob.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
=======
>>>>>>> baskaran-dev

        layout = (LinearLayout)findViewById(R.id.relay);
        next = (Button) findViewById(R.id.next);
        job_name = (EditText) findViewById(R.id.descrip);
        job_description = (EditText) findViewById(R.id.detail);
        date_text = (TextView) findViewById(R.id.date_text);
        textview = (TextView) findViewById(R.id.textview);
        img_paint=(ImageView)findViewById(R.id.img_paint);
        start_time_text = (TextView) findViewById(R.id.start_time_text);
        end_time_text = (TextView) findViewById(R.id.end_time_text);
        job_amount = (TextView) findViewById(R.id.amount);
        amount_text = (TextView) findViewById(R.id.pay_type);
        pay_text = (TextView) findViewById(R.id.payment_details);
        logo = (ImageView) findViewById(R.id.logo);
        arrow = (ImageView) findViewById(R.id.arrow);
        img_arrow=(ImageView)findViewById(R.id.img_arrow);
        list = (Spinner)findViewById(R.id.listview);
        pay_lay = (RelativeLayout) findViewById(R.id.linear4);
        payment_layout = (RelativeLayout) findViewById(R.id.relay1);
        date_layout = (RelativeLayout) findViewById(R.id.linear1);
        time_layout = (RelativeLayout) findViewById(R.id.linear2);
        estimate_layout = (RelativeLayout) findViewById(R.id.linear3);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        hour = (TextView) findViewById(R.id.hour);

        Intent i = getIntent();
        id = i.getStringExtra("userId");
        address = i.getStringExtra("address");
        city = i.getStringExtra("city");
        state = i.getStringExtra("state");
        zipcode = i.getStringExtra("zipcode");
        System.out.println("iiiiiiiiiiiiiiiiiiiii:" + id);

        //String currentDate = DateFormat.getDateInstance().format(new Date());
        //date_text.setText(dateInString);

        String pattern1 = "hh:mm a";
        String timeFormat = new SimpleDateFormat(pattern1).format(new Date());

        //String currentTime = DateFormat.getTimeInstance().format(new Date());
        //start_time_text.setText(timeFormat);
        //end_time_text.setText(timeFormat);
        String pattern2 = "hh:mm:ss";
        st_time = new SimpleDateFormat(pattern2).format(new Date());
        en_time = new SimpleDateFormat(pattern2).format(new Date());
        System.out.println("777777777:time::::" + st_time+",,,,"+ en_time);
        activity=this;
        /*job_amount.setPaintFlags(date_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        start_time_text.setPaintFlags(date_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        end_time_text.setPaintFlags(date_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        date_text.setPaintFlags(date_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
*/
        listCategory();

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

        date_layout.setOnClickListener(this);
        time_layout.setOnClickListener(this);
        estimate_layout.setOnClickListener(this);

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

        img_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindowDogs.showAsDropDown(v, -5, 0);

            }
        });

        end_time_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                threePicker.show();
            }
        });

        //Three Options PickerView
        threePicker = new MyOptionsPickerView(CreateJob.this);
        final ArrayList<Integer> numbers = new ArrayList<Integer>(100);

        for (int j = 0; j <= 100; j++)
        {
            numbers.add(j);
            System.out.println(numbers.get(j));
        }
        /* ArrayList<String> threeItemsOptions1 = new ArrayList<String>();
        threeItemsOptions1.add("AA");
        threeItemsOptions1.add("BB");
        threeItemsOptions1.add("CC");
        threeItemsOptions1.add("DD");
        threeItemsOptions1.add("EE");*/

        final ArrayList<String> threeItemsOptions2 = new ArrayList<String>();
        threeItemsOptions2.add("0.00");
        threeItemsOptions2.add("0.25");
        threeItemsOptions2.add("0.50");
        threeItemsOptions2.add("0.75");

        final ArrayList<String> threeItemsOptions3 = new ArrayList<String>();
        threeItemsOptions3.add("Hours");
        threeItemsOptions3.add("Minutes");

        threePicker.setPicker(numbers, threeItemsOptions2, threeItemsOptions3, false);
        //threePicker.setTitle("Picker");
        threePicker.setCyclic(false, false, false);
        threePicker.setSelectOptions(0, 0, 0);
        threePicker.setOnoptionsSelectListener(new MyOptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                String a = String.valueOf(numbers.get(options1));
                String b = String.valueOf(threeItemsOptions2.get(option2));
                float numa = Float.parseFloat(a);
                float numb = Float.parseFloat(b);
                System.out.println("aaaaaaaaaaa:::"+numa+"..."+numb+"..."+a+"...."+b);
                float c = numa + numb;
                System.out.println("aaaaaaaaaaa::cccc:"+c);
                end_time_text.setText(String.valueOf(c));
                String option = threeItemsOptions3.get(options3);
                hour.setText(option);
                // Toast.makeText(CreateJob.this, "" + numbers.get(options1) + " " + threeItemsOptions2.get(option2) + " " + threeItemsOptions3.get(options3), Toast.LENGTH_SHORT).show();
            }
        });

        pay_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(CreateJob.this);
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
                        System.out.println("sssssssssssss::pay_amount:"+pay_amount);
                        paytext = text.getText().toString().trim();

                        payment_layout.setVisibility(View.VISIBLE);
                        job_amount.setText(pay_amount);
                        amount_text.setText(paytext);
                        pay_text.setVisibility(View.GONE);
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

        if (v == date_layout) {
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
                            int mm = monthOfYear + 1;
                            String month = (mm < 10) ? "0" + mm : "" + mm;
                            date_text.setText(year + "-" + month + "-" + dayOfMonth);
                            String pattern = "MMM d,yyyy";
                            String dateInString = new SimpleDateFormat(pattern).format(new Date());
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();*/
        }
        if (v == time_layout) {

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
                            st_time = hour_day + ":" + min + ":" + sec;
                            System.out.println("77777777:start_time::::::"+st_time);

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
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
       /* if (v == estimate_layout) {

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
                            //en_time = hour_day + ":" + min + ":" + sec;
                            en_time = hour_day;
                            System.out.println("77777777:end_time::::::"+en_time);

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
                            }
                            else {
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
                            if(min1.equals("00"))
                            {
                                end_time_text.setText(hour_day1 + " Hours");
                            }
                            else
                            {
                                end_time_text.setText(hour_day1 + " Hours" + " " + min1 + " Minutes");
                            }
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }*/
    }

    public void validate()
    {
        name = job_name.getText().toString().trim();
        description = job_description.getText().toString().trim();
        date = date_text.getText().toString().trim();
        start_time = start_time_text.getText().toString();
        end_time = end_time_text.getText().toString();
        amount = job_amount.getText().toString().trim();
        payment_type = amount_text.getText().toString().trim();

        amount=amount.substring(1);
        System.out.println("amount "+amount);

        if (TextUtils.isEmpty(name)) {
            // custom dialog
            final Dialog dialog = new Dialog(CreateJob.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must Fill In \"Job Title\" Box");
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
        }
        if (TextUtils.isEmpty(description)) {
            // custom dialog
            final Dialog dialog = new Dialog(CreateJob.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must Fill In \"Detailed Description of Job\" Box");
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
        }
        if (TextUtils.isEmpty(date)) {
            // custom dialog
            final Dialog dialog = new Dialog(CreateJob.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must Fill In \"Job Date\" Box");
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
        }
        if (TextUtils.isEmpty(start_time)) {
            // custom dialog
            final Dialog dialog = new Dialog(CreateJob.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must Fill In \"Start Time\" Box");
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
        }
        if (TextUtils.isEmpty(end_time)) {
            // custom dialog
            final Dialog dialog = new Dialog(CreateJob.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must Fill In \"End Time\" Box");
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
        }
        if (TextUtils.isEmpty(amount)) {
            // custom dialog
            final Dialog dialog = new Dialog(CreateJob.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must Fill In \"Payment Details\"");
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
        }
        if(category.equals("0"))
        {
            final Dialog dialog = new Dialog(CreateJob.this);
            dialog.setContentView(R.layout.custom_dialog);

            // set the custom dialog components - text, image and button
            TextView text = (TextView) dialog.findViewById(R.id.text);
            text.setText("Must choose any one category");
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
        }
        if (checkBox.isChecked())
        {
            flexible_status = "yes";
        }
        else
        {
            flexible_status = "no";
        }
        if(!name.equals("")&&!description.equals("")&&!date.equals("")&&!start_time.equals("")&&!end_time.equals("")&&!amount.equals(""))
        {
            String estim = end_time_text.getText().toString();
            System.out.println("eeeeeeeee:time:::"+estim+"...."+amount);
            job_estimated = String.valueOf(Float.valueOf(estim)*Float.valueOf(amount));
            System.out.println("eeeeeeeee:estimated:::"+job_estimated);
            Intent i = new Intent(CreateJob.this, CreateJob2.class);
            i.putExtra("userId", id);
            i.putExtra("address", address);
            i.putExtra("city", city);
            i.putExtra("state", state);
            i.putExtra("zipcode", zipcode);
            i.putExtra("name",name);
            i.putExtra("category",categoryId);
            i.putExtra("description",description);
            i.putExtra("date", date);
            i.putExtra("start_time",st_time);
            i.putExtra("end_time",estim);
            i.putExtra("payment_amount",amount);
            i.putExtra("payment_type", payment_type);
            i.putExtra("flexible_status", flexible_status);
            i.putExtra("estimated_amount", job_estimated);
            i.putExtra("jobPayout", "0.0");
            i.putExtra("paypalFee", "0.0");
            startActivity(i);
        }
        else
        {

        }
    }


    public void listCategory()
    {

        dialog = new Dialog(CreateJob.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
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
        System.out.println("response from interface"+jsonobject);

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

                // list.setAdapter(adapter);
/*
                PopupWindow popupWindow = new PopupWindow(this);

                // the drop down list is a list view
                ListView listcate = new ListView(this);
                listcate.setAdapter(adapter);*/

              /*  list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        category = parent.getItemAtPosition(position).toString();
                        if(category.equals("Select Job Category"))
                        {

                        }
                     else {
                            System.out.println("ssssssssssselected:item:" + category);
                            String value = "1";
                            cat = Integer.parseInt(category) + Integer.parseInt(value);
                            categoryId = String.valueOf(cat);
                            System.out.println("ssssssssssselected:job_cat_name:response:" + categoryId);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        category = "0";
                        System.out.println("on nothing selected");
                        list.setVisibility(View.GONE);
                        img_arrow.setVisibility(View.VISIBLE);
                        textview.setVisibility(View.VISIBLE);

                    }
                });*/



            }
            else
            {
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

    public PopupWindow popupWindowDogs() {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(this);

        // the drop down list is a list view


        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popupbackground,null);

        ListView listViewDogs =(ListView) layout.findViewById(R.id.list_category);;
        // set our adapter and pass our pop up window contents
        adapter = new CustomJobListAdapter(CreateJob.this, job_title,imageId);
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


    public static void SetCategory(int pos){

        HashMap<String,String> map = job_title.get(pos);
        String title =map.get("job_category");
        category =title;
        textview.setText(title);
        categoryId=String.valueOf(pos);
        switch (pos){
            case 0:
                img_paint.setImageResource(R.drawable.box_17);
                break;
            case 1:
                img_paint.setImageResource(R.drawable.box_10);
                break;
            case 2:
                img_paint.setImageResource(R.drawable.box_8);
                break;
            case 3:
                img_paint.setImageResource(R.drawable.box_15);
                break;
            case 4:
                img_paint.setImageResource(R.drawable.box_18);
                break;
            case 5:
                img_paint.setImageResource(R.drawable.box_9);
                break;
            case 6:
                img_paint.setImageResource(R.drawable.box_11);
                break;
            case 7:
                img_paint.setImageResource(R.drawable.box_20);
                break;
            case 8:
                img_paint.setImageResource(R.drawable.box_3);
                break;
            case 9:
                img_paint.setImageResource(R.drawable.box_5);
                break;
            case 10:
                img_paint.setImageResource(R.drawable.box_6);
                break;
            case 11:
                img_paint.setImageResource(R.drawable.box_2);
                break;
            case 12:
                img_paint.setImageResource(R.drawable.box_19);
                break;
            case 13:
                img_paint.setImageResource(R.drawable.box_21);
                break;
            case 14:
                img_paint.setImageResource(R.drawable.box_1);
                break;
            case 15:
                img_paint.setImageResource(R.drawable.box_12);
                break;
            case 16:
                img_paint.setImageResource(R.drawable.box_7);
                break;
            case 17:
                img_paint.setImageResource(R.drawable.box_14);
                break;
            case 18:
                img_paint.setImageResource(R.drawable.box_16);
                break;
            case 19:
                img_paint.setImageResource(R.drawable.box_4);
                break;
            case 20:
                img_paint.setImageResource(R.drawable.box_6);
                break;
            default:
                break;
        }

        img_paint.setVisibility(View.VISIBLE);
    }

    public static class DatePickerDialogTheme4 extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datepickerdialog = new DatePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT,this,year,month,day);

            return datepickerdialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day){

           /* TextView textview = (TextView)getActivity().findViewById(R.id.textView1);

            textview.setText(day + ":" + (month+1) + ":" + year);*/

        }
    }

}
