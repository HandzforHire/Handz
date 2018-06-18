package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaypalAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    private static final String URL = Constant.SERVER_URL+"delete_paypal_user";
    public static String PAYPAL_ID = "paypal_user_info_id";
    public static String APP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    String account_id,employer_id,address,city,state,zipcode;
    HashMap<String, String> finalItems = new HashMap<String, String>();
    TextView id;

    public PaypalAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.paypal_details, null);

        id = (TextView)vi.findViewById(R.id.acc_id);
        TextView first_name = (TextView)vi.findViewById(R.id.text2);
        TextView email = (TextView)vi.findViewById(R.id.text4);
        TextView last_name = (TextView)vi.findViewById(R.id.text5);
        Button delete_btn = (Button)vi.findViewById(R.id.delete);

        HashMap<String, String> items = new HashMap<String, String>();
        items = data.get(position);
        employer_id = items.get("userId");
        System.out.println("iiiiiiiiiiiiiiiiiiid:employer_id::"+employer_id);
        final String get_id = items.get("id");
        System.out.println("iiiiiiiiiiiiiiiiiiid:get_id::"+get_id);
        String get_email = items.get("email");
        System.out.println("iiiiiiiiiiiiiiiiiiid:get_email::"+get_email);
        String get_first = items.get("firstname");
        System.out.println("iiiiiiiiiiiiiiiiiiid:get_first::" + get_first);
        String get_last = items.get("lastname");
        System.out.println("iiiiiiiiiiiiiiiiiiid:get_last::" + get_last);
        address = items.get("address");
        city = items.get("city");
        state = items.get("state");
        zipcode = items.get("zipcode");

        // Setting all values in listview
        id.setText(get_id);
        email.setText(get_email);
        first_name.setText(get_first);
        last_name.setText(get_last);

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account_id = id.getText().toString().trim();
                System.out.println("aaaaaaaaaaa:accountid:::"+account_id);
                webService();
            }
        });


        return vi;
    }

    private void webService() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("paypal_delete:::" + response);
                        onResponserecieved(response, 2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
                Map<String, String> map = new HashMap<String, String>();
                map.put(APP_KEY, value);
                map.put(PAYPAL_ID, account_id);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        requestQueue.add(stringRequest);
    }

    public void onResponserecieved(String jsonobject, int requesttype) {
        String status = null;
        try {

            JSONObject jResult = new JSONObject(jsonobject);

            status = jResult.getString("status");

            if(status.equals("success"))
            {
                final Dialog dialog = new Dialog(activity);
                dialog.setContentView(R.layout.gray_custom);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("Deleted Successfully");
                Button dialogButton = (Button) dialog.findViewById(R.id.ok);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent i = new Intent(activity,ManagePaymentOptions.class);
                        i.putExtra("userId",employer_id);
                        i.putExtra("address", address);
                        i.putExtra("city", city);
                        i.putExtra("state", state);
                        i.putExtra("zipcode", zipcode);
                        v.getContext().startActivity(i);
                    }
                });

                dialog.show();
                Window window = dialog.getWindow();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
