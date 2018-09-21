package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class PayEmployee extends Activity  implements SimpleGestureFilter.SimpleGestureListener{

    ImageView logo,image,info1,info2;
    Button payment_button;
    EditText tip;
    String job_id,employer_id,employee_id,job_name,profile_image,paypal_value,estimated_value;
    ProgressDialog progress_dialog;
    TextView name,date,total,payout,service_fee,processing_fee;
    String profile_name,user_name,job_payout,paypal_fee,estimated_payment,fee_details,job_payment_amount;
    Integer total_value;
    Dialog dialog;
    private SimpleGestureFilter detector;
    private String current = "";
    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
           // .environment(PayPalConfiguration.ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
           // .clientId(PayPalConfig.PAYPAL_LIVE_CLIENT_ID)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID)
            .merchantName("HandzForHire")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.homeadvisor.com/rfs/aboutus/privacyPolicy.jsp"))
            .merchantUserAgreementUri(Uri.parse("https://www.homeadvisor.com/servlet/TermsServlet"));
    public static final int PAYPAL_REQUEST_CODE = 123;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_employee);

        dialog = new Dialog(PayEmployee.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        logo = (ImageView)findViewById(R.id.logo);
        image = (ImageView)findViewById(R.id.imageView);
        info1 = (ImageView)findViewById(R.id.info1);
        info2 = (ImageView)findViewById(R.id.info2);
        payout = (TextView) findViewById(R.id.job_payout);
        service_fee = (TextView) findViewById(R.id.service_fee);
        processing_fee = (TextView) findViewById(R.id.processing_fee);
        tip = (EditText) findViewById(R.id.tip);
        payment_button = (Button) findViewById(R.id.payment_btn);
        name = (TextView) findViewById(R.id.name);
        date = (TextView) findViewById(R.id.transaction_date);
        total = (TextView) findViewById(R.id.total);

        Intent i = getIntent();
        job_id = i.getStringExtra("jobId");
        employer_id = i.getStringExtra("employerId");
        employee_id = i.getStringExtra("employeeId");
        job_name = i.getStringExtra("jobname");
        profile_image = i.getStringExtra("image");
        profile_name = i.getStringExtra("profilename");
        user_name = i.getStringExtra("username");
        job_payout = i.getStringExtra("job_payout");
        paypal_fee = i.getStringExtra("paypalfee");
        estimated_payment = i.getStringExtra("job_estimated_payment");
        fee_details = i.getStringExtra("fee_details");
        job_payment_amount=i.getStringExtra("job_payment_amount");

        detector = new SimpleGestureFilter(this,this);

        tip.addTextChangedListener(tw);

        String new_payout_value = job_payout;
        new_payout_value = new_payout_value.substring(1);
        System.out.println("DDDDDDDd:new_payout_value::"+new_payout_value);

        if(fee_details.equals("add"))
        {
            payout.setText(new_payout_value);
            String get_service_fee = service_fee.getText().toString().trim();
            String get_tip = tip.getText().toString().trim();
            System.out.println("DDDDDDDd:get_service_fee:::"+get_service_fee);
            paypal_value= paypal_fee;
            paypal_value = paypal_value.substring(1);
            processing_fee.setText(paypal_value);
            String get_total = String.valueOf(Float.valueOf(new_payout_value)+Float.valueOf(get_service_fee)+Float.valueOf(get_tip)+ Float.valueOf(paypal_value));
            System.out.println("DDDDDDDd:get_total:::"+get_total);
            String tot= String.format("%.2f", Float.valueOf(get_total));
            System.out.println("DDDDDDDd:tot:::"+tot);
            total.setText(tot);
        }
        else
        {
            estimated_value = estimated_payment;
            estimated_value = estimated_value.substring(1);
            payout.setText(estimated_value);
            System.out.println("DDDDDDDd:estimated_value:::"+estimated_value);
            String get_service_fee = service_fee.getText().toString().trim();
            String get_tip = tip.getText().toString().trim();
            System.out.println("DDDDDDDd:get_service_fee:::"+get_service_fee);
            paypal_value= paypal_fee;
            paypal_value = paypal_value.substring(1);
            processing_fee.setText(paypal_value);
            String get_total = String.valueOf(Float.valueOf(estimated_value)+Float.valueOf(get_service_fee)+Float.valueOf(get_tip)+ Float.valueOf(paypal_value));
            System.out.println("DDDDDDDd:get_total:::"+get_total);
            String tot= String.format("%.2f", Float.valueOf(get_total));
            System.out.println("DDDDDDDd:tot:::"+tot);
            total.setText(tot);
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("MMMM dd, yyyy");
        String strDate = mdformat.format(calendar.getTime());
        System.out.println("DDDDDDDd:date::"+strDate);
        date.setText(strDate);

        if(profile_image==null) {

        }
        else {
            Glide.with(this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image);
        }
        if(profile_name.equals(""))
        {
            name.setText(user_name);
        }
        else {
            name.setText(profile_name);
        }

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PayEmployee.this,ProfilePage.class);
                i.putExtra("userId",employer_id);
                startActivity(i);
            }
        });

        payment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* get_tip = tip.getText().toString().trim();
                get_payout = payment_amount.getText().toString().trim();
                get_servicefee = payment_method.getText().toString().trim();
                get_date = date.getText().toString().trim();
                total_value = Integer.parseInt(get_amount)+Integer.parseInt(get_tip);
                get_total = String.valueOf(total_value);
                total.setText(get_total);*/
               /* Intent i = new Intent(PayEmployee.this,PayEmployee1.class);
                i.putExtra("jobId",job_id);
                i.putExtra("employerId",employer_id);
                i.putExtra("employeeId",employee_id);
                i.putExtra("jobName",job_name);
                i.putExtra("tip",get_tip);
                i.putExtra("paymentMethod",get_method);
                i.putExtra("payment_amount",get_tip);
                i.putExtra("transaction_date",get_date);
                startActivity(i);*/
                Intent intent = new Intent(PayEmployee.this, PayPalProfileSharingActivity.class);
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
                intent.putExtra(PayPalProfileSharingActivity.EXTRA_REQUESTED_SCOPES, getOauthScopes());
                startActivityForResult(intent, PAYPAL_REQUEST_CODE);
            }
        });

        info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(PayEmployee.this);
                dialog.setContentView(R.layout.custom_dialog);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("You may add a tip for a job well done, or if the job took longer than expected.");
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
        });

        info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // custom dialog
                final Dialog dialog = new Dialog(PayEmployee.this);
                dialog.setContentView(R.layout.custom_dialog);

                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("This fee may minimally increase if you choose to add a tip.");
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
        });
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

                tip.removeTextChangedListener(this);
                tip.setText(cashAmountBuilder.toString());

                tip.setTextKeepState(cashAmountBuilder.toString());
                Selection.setSelection(tip.getText(), cashAmountBuilder.toString().length());

                tip.addTextChangedListener(this);
            }

            if(fee_details.equals("add"))
            {
                String new_payout = payout.getText().toString().trim();
                System.out.println("DDDDDDDd:new_payout::"+new_payout);
                String new_payout_value = new_payout;
                new_payout_value = new_payout_value.substring(1);
                System.out.println("DDDDDDDd:new_payout_value::"+new_payout_value);
                String new_tip = tip.getText().toString().trim();
                String add_job_payout = String.valueOf(Float.valueOf(new_payout)+Float.valueOf(new_tip));
                System.out.println("DDDDDDDd:add_job_payout::"+add_job_payout);

                String s1 = "100";
                String multi = String.valueOf(Float.valueOf(s1)*Float.valueOf(add_job_payout));
                System.out.println("DDDDDDDd:pay_employee:multi:"+multi);
                String s2 = "130";
                String add1 = String.valueOf(Float.valueOf(multi)+Float.valueOf(s2));
                System.out.println("DDDDDDDd:pay_employee:add1:"+add1);
                String s3 = "97.1";
                String div_total = String.valueOf(Float.valueOf(add1)/Float.valueOf(s3));
                System.out.println("DDDDDDDd:pay_employee:div_total:"+div_total);
                String roundup_value = String.format("%.2f", Float.valueOf(div_total));
                System.out.println("DDDDDDDd:pay_employee:roundup_value:"+roundup_value);
                String handz_fee = "1.00";
                String pay_fee = String.valueOf(Float.valueOf(roundup_value)-Float.valueOf(new_payout)-Float.valueOf(new_tip)-Float.valueOf(handz_fee));
                String total_value = String.format("%.2f", Float.valueOf(pay_fee));
                System.out.println("DDDDDDDd:pay_employee:pay_fee:"+pay_fee+"total2:::"+total_value);
                processing_fee.setText(total_value);
                total.setText(roundup_value);
            }
            else
            {
                String new_payout = payout.getText().toString().trim();
                System.out.println("DDDDDDDd:new_payout::"+new_payout);
                String estimated_value = estimated_payment;
                estimated_value = estimated_value.substring(1);
                System.out.println("DDDDDDDd:estimated_value::"+estimated_value);
                String new_tip = tip.getText().toString().trim();

                String s1 = "100";
                String multi = String.valueOf(Float.valueOf(s1)*Float.valueOf(new_tip));
                System.out.println("DDDDDDDd:pay_employee:multi:"+multi);
                String s3 = "97.1";
                String div_total = String.valueOf(Float.valueOf(multi)/Float.valueOf(s3));
                System.out.println("DDDDDDDd:pay_employee:div_total:"+div_total);
                String roundup_value = String.format("%.2f", Float.valueOf(div_total));
                System.out.println("DDDDDDDd:pay_employee:roundup_value:"+roundup_value);
                String total_value = String.valueOf(Float.valueOf(job_payment_amount)+Float.valueOf(roundup_value));
                System.out.println("DDDDDDDd:pay_employee:total_value:"+total_value);
                String handz_fee = "1.00";
                String pay_fee = String.valueOf(Float.valueOf(total_value)-Float.valueOf(estimated_value)-Float.valueOf(new_tip)-Float.valueOf(handz_fee));
                String pay_roundup_value = String.format("%.2f", Float.valueOf(pay_fee));
                System.out.println("DDDDDDDd:pay_employee:pay_roundup_value:"+pay_roundup_value);
                processing_fee.setText(pay_roundup_value);
                total.setText(total_value);
            }
        }
    };
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
                Intent i;
                if(Profilevalues.usertype.equals("1")) {
                    i = new Intent(getApplicationContext(), ProfilePage.class);
                }else{
                    i = new Intent(getApplicationContext(), LendProfilePage.class);
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                //PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                PayPalAuthorization auth = data
                        .getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                System.out.println("authorization "+auth.toJSONObject());
                if (auth != null) {
                    String authorization_code = auth.getAuthorizationCode();
                    getAccessToken(authorization_code);
                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    private PayPalOAuthScopes getOauthScopes() {

        HashSet<String> scopes = new HashSet<String>(
                Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS) );
        scopes.add(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL);
        scopes.add(PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS);
        scopes.add(PayPalOAuthScopes.PAYPAL_SCOPE_PHONE);
        scopes.add(PayPalOAuthScopes.PAYPAL_SCOPE_PROFILE);
        return new PayPalOAuthScopes(scopes);
    }


    private String getAccessToken(String authorizationCode)
    {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://api.paypal.com/v1/oauth2/token");
        //HttpPost httppost = new HttpPost("https://api.sandbox.paypal.com/v1/oauth2/token");

        try {
            String text=PayPalConfig.PAYPAL_CLIENT_ID+":"+PayPalConfig.PAYPAL_SECRET_KEY;
            //String text=PayPalConfig.PAYPAL_LIVE_CLIENT_ID+":"+PayPalConfig.PAYPAL_LIVE_SECRET_KEY;
            byte[] data = text.getBytes("UTF-8");
            String base64 = Base64.encodeToString(data, Base64.NO_WRAP);

            httppost.addHeader("content-type", "application/x-www-form-urlencoded");
            httppost.addHeader("Authorization", "Basic " + base64);
            StringEntity se=new StringEntity("grant_type=authorization_code&response_type=token&redirect_uri=urn:ietf:wg:oauth:2.0:oob&code="+authorizationCode);
            httppost.setEntity(se);
            HttpResponse response = httpclient.execute(httppost);
            String responseContent = EntityUtils.toString(response.getEntity());
            System.out.println("authorizatio code "+authorizationCode);
            System.out.println("authorizatio code "+authorizationCode);
            Log.d("Response", responseContent );
            try {
                JSONObject obj = new JSONObject(responseContent);
                System.out.println(obj.getString("access_token"));
                OrderAPI(obj.getString("access_token"));
            }catch (Exception e)
            {
                System.out.println("e "+e.getMessage());
            }
            System.out.println("Response "+responseContent);

        } catch (ClientProtocolException e) {
            System.out.println("Exception "+e.getMessage());

        } catch (IOException e) {
            System.out.println("Exception "+e.getMessage());

        }
        return null;
    }

    public void OrderAPI(String accesstoken) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://api.sandbox.paypal.com/v1/checkout/orders/");
        try {

            Date c = Calendar.getInstance().getTime();
            System.out.println("Current time => " + c);

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            String formattedDate = df.format(c);

            System.out.println("Date "+formattedDate);

            httppost.addHeader("Accept", "application/json");
            httppost.addHeader("Accept-Language", "en_US");
            httppost.addHeader("content-type", "application/json");
            httppost.addHeader("Authorization", "Bearer " + accesstoken);
            httppost.addHeader("PayPal-Partner-Attribution-Id", "HandzForHire_SP_PPM");
            httppost.addHeader("PayPal-Request-Id", "Bearer " + accesstoken);
            httppost.addHeader("Paypal-Client-Metadata-Id", "AZKGEIXjRP25L9gE8PZLI17F5BujtqTehLicuLknK1RUTmqErqBvIuJ84edzXOn5dOfNn67sTaUL3mgV");
            HttpResponse response = httpclient.execute(httppost);
            String responseContent = EntityUtils.toString(response.getEntity());
            System.out.println("Response "+responseContent);

        } catch (ClientProtocolException e) {
            System.out.println("Exception "+e.getMessage());

        } catch (IOException e) {
            System.out.println("Exception "+e.getMessage());

        }
    }
}
