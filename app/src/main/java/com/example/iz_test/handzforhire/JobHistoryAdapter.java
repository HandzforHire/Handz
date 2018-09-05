package com.example.iz_test.handzforhire;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class JobHistoryAdapter extends BaseAdapter {
        private static final String GET_COUNT_URL = Constant.SERVER_URL+"view_count";
        public static String KEY_USERID = "user_id";
        public static String XAPP_KEY = "X-APP-KEY";
        public static String TYPE = "type";
        String value = "HandzForHire@~";
        private Activity activity;
        private ArrayList<HashMap<String, String>> data;
        private static LayoutInflater inflater = null;
        String jobId;
        Dialog dialog;
    public JobHistoryAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
            activity = a;
            data = d;
            dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progressbar);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        inflater = LayoutInflater.from(a);
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
                vi = inflater.inflate(R.layout.job_history_list, null);

            TextView job_name = (TextView) vi.findViewById(R.id.text1);
            final TextView job_id = (TextView) vi.findViewById(R.id.job_id);
            final TextView employer_id = (TextView) vi.findViewById(R.id.employer_id);
            final TextView employee_id = (TextView) vi.findViewById(R.id.employee_id);
            final TextView image_text = (TextView) vi.findViewById(R.id.image1);
            TextView user_name = (TextView) vi.findViewById(R.id.text3);
            ImageView image1 = (ImageView) vi.findViewById(R.id.img1);
            TextView leave_rating_btn = (TextView) vi.findViewById(R.id.leave_rating);
            Button job_details = (Button) vi.findViewById(R.id.btn);
            LinearLayout chat = (LinearLayout) vi.findViewById(R.id.lay1);

            String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
            Typeface font = Typeface.createFromAsset(activity.getAssets(), fontPath);
            String fontPath1 = "fonts/calibri.ttf";
            Typeface font1 = Typeface.createFromAsset(activity.getAssets(), fontPath1);

            HashMap<String, String> items = new HashMap<String, String>();
            items = data.get(position);
            final String get_name = items.get("name");
            final String get_image = items.get("image");
            final String get_profile = items.get("profile");
            final String get_user = items.get("user");
            final String get_id = items.get("user_id");
            final String get_jobid = items.get("jobId");
            final String get_employer = items.get("employer");
            final String get_employee = items.get("employee");
            final String channel_id=items.get("channel");


            job_name.setText(get_name);
            job_name.setTypeface(font);
            job_id.setText(get_jobid);
            employer_id.setText(get_employer);
            employee_id.setText(get_employee);
            image_text.setText(get_image);

            if(get_profile.equals("null"))
            {
                user_name.setText(get_user);
                user_name.setTypeface(font1);
            }
            else
            {
                user_name.setText(get_profile);
                user_name.setTypeface(font1);
            }
            if(get_image.equals(""))
            {
                image1.setVisibility(View.VISIBLE);
            }
            else {
                Glide.with(activity).load(get_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(activity,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image1);

            }

            leave_rating_btn.setTag(position);
            chat.setTag(position);
            job_details.setTag(position);

            leave_rating_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    HashMap<String, String> items = new HashMap<String, String>();
                    items = data.get((Integer) v.getTag());
                    getratingcount(items.get("user_id"));
                    Intent intent = new Intent(activity, NeedRating.class);
                    intent.putExtra("jobId",items.get("jobId"));
                    intent.putExtra("employer_id", items.get("employer"));
                    intent.putExtra("employee_id",items.get("employee"));
                    intent.putExtra("user_id", items.get("user_id"));
                    intent.putExtra("image",items.get("image"));
                    intent.putExtra("profilename", items.get("profile"));
                    v.getContext().startActivity(intent);
                }
            });

            chat.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {

                    int pos= (int) view.getTag();
                    HashMap<String, String> items =data.get(pos);
                    getmsgcountjobhis(items.get("user_id"));
                    String username="";
                    if(items.get("profile").isEmpty())
                        username=items.get("user");
                    else
                        username= items.get("profile");

                    Intent i = new Intent(activity,ChatNeed.class);
                    i.putExtra("jobId",items.get("jobId"));
                    i.putExtra("channel",items.get("channel"));
                    i.putExtra("username",username);
                    i.putExtra("userId", items.get("user_id"));
                    i.putExtra("message_type","job_history");
                    i.putExtra("user_type","employer");
                    view.getContext().startActivity(i);

                }
            });

            job_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos= (int) v.getTag();
                    HashMap<String, String> items =data.get(pos);

                    Intent i = new Intent(activity,JobDetails.class);
                    i.putExtra("jobId",items.get("jobId"));
                    i.putExtra("userId",items.get("user_id"));
                    v.getContext().startActivity(i);
                }
            });

            return vi;
        }


    public void getmsgcountjobhis(final String id) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resposne "+response);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(activity);
                            dialog.setContentView(R.layout.custom_dialog);
                            // set the custom dialog components - text, image and button
                            TextView text = (TextView) dialog.findViewById(R.id.text);
                            text.setText("Error Connecting To Network");
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
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        }else if (error instanceof AuthFailureError) {
                            Toast.makeText(activity,"Authentication Failure while performing the request",Toast.LENGTH_LONG).show();
                        }else if (error instanceof NetworkError) {
                            Toast.makeText(activity,"Network error while performing the request",Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                System.out.println("volley error::: " + jsonObject);
                                String status = jsonObject.getString("msg");

                                // custom dialog
                                final Dialog dialog = new Dialog(activity);
                                dialog.setContentView(R.layout.custom_dialog);

                                // set the custom dialog components - text, image and button
                                TextView text = (TextView) dialog.findViewById(R.id.text);
                                text.setText(status);
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
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                            } catch (JSONException e) {
                                //Handle a malformed json response
                                System.out.println("volley error ::" + e.getMessage());
                            } catch (UnsupportedEncodingException errors) {
                                System.out.println("volley error ::" + errors.getMessage());
                            }
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(KEY_USERID, id);
                params.put(TYPE,"notificationCountMsgJobhistory");
                params.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println("Params "+params);
                return params;
            }
        };



        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    public void getratingcount(final String id) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_COUNT_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resposne "+response);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(activity);
                            dialog.setContentView(R.layout.custom_dialog);
                            // set the custom dialog components - text, image and button
                            TextView text = (TextView) dialog.findViewById(R.id.text);
                            text.setText("Error Connecting To Network");
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
                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        }else if (error instanceof AuthFailureError) {
                            Toast.makeText(activity,"Authentication Failure while performing the request",Toast.LENGTH_LONG).show();
                        }else if (error instanceof NetworkError) {
                            Toast.makeText(activity,"Network error while performing the request",Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                System.out.println("volley error::: " + jsonObject);
                                String status = jsonObject.getString("msg");

                                // custom dialog
                                final Dialog dialog = new Dialog(activity);
                                dialog.setContentView(R.layout.custom_dialog);

                                // set the custom dialog components - text, image and button
                                TextView text = (TextView) dialog.findViewById(R.id.text);
                                text.setText(status);
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
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


                            } catch (JSONException e) {
                                //Handle a malformed json response
                                System.out.println("volley error ::" + e.getMessage());
                            } catch (UnsupportedEncodingException errors) {
                                System.out.println("volley error ::" + errors.getMessage());
                            }
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(XAPP_KEY, value);
                params.put(KEY_USERID, id);
                params.put(TYPE,"notificationCountStarRating");
                params.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println("Params "+params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    }
