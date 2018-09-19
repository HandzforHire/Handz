package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
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
import com.app.Config;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.glide.Glideconstants;
import com.glide.RoundedCornersTransformation;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LendReviewRating extends Activity implements SimpleGestureFilter.SimpleGestureListener{

    String image,id,date,profile_image,profilename,average_rating,comments,avg_rat;
    private static final String URL = Constant.SERVER_URL+"review_rating";
    private static final String LINKEDIN_URL = Constant.SERVER_URL+"linked_in ";
    ArrayList<HashMap<String, String>> job_list = new ArrayList<HashMap<String, String>>();
    public static String KEY_USERID = "user_id";
    public static String XAPP_KEY = "X-APP-KEY";
    public static String EMAIL = "email";
    public static String FIRST_NAME = "first_name";
    public static String LAST_NAME = "last_name";
    public static String ID = "id";
    public static String PROF_URL = "profile_url";
    public static String PIC_URL = "picture_url";

    String value = "HandzForHire@~";
    public static String TYPE = "type";
    String usertype = "employee";
    int timeout = 60000;
    TextView txt_rating;
    ImageView imageprofile;
    TextView txt_profilename;

    ListView list;
    LinearLayout lin_linkin,lin_linkininfo;
    Button close;
    Dialog dialog;
    private SimpleGestureFilter detector;
    Activity thisActivity;
    private static final String AUTHORIZATION_URL = "https://www.linkedin.com/uas/oauth2/authorization";
    private static final String ACCESS_TOKEN_URL = "https://www.linkedin.com/uas/oauth2/accessToken";
    private static final String SECRET_KEY_PARAM = "client_secret";
    private static final String RESPONSE_TYPE_PARAM = "response_type";
    private static final String GRANT_TYPE_PARAM = "grant_type";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String RESPONSE_TYPE_VALUE ="code";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String STATE_PARAM = "state";
    private static final String REDIRECT_URI_PARAM = "redirect_uri";
    /*---------------------------------------*/
    private static final String QUESTION_MARK = "?";
    private static final String AMPERSAND = "&";
    private static final String EQUALS = "=";

    private static final String API_KEY = "81qt2f2mg525a4";
    //This is the private api key of our application
    private static final String SECRET_KEY = "E3VnJD4wY2l5x9U7";
    //This is any string we want to use. This will be used for avoid CSRF attacks. You can generate one here: http://strongpasswordgenerator.com/
    private static final String STATE = "DLKDJF46ikMMZADfdfds";
    //This is the url that LinkedIn Auth process will redirect to. We can put whatever we want that starts with http:// or https:// .
    //We use a made up url that we will intercept when redirecting. Avoid Uppercases.
    private static final String REDIRECT_URI = "https://www.handzforhire.com";
    String firstnmae,lastnmae,lin_email,lin_id,pictureurl,profileurl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.review_rating);

        dialog = new Dialog(LendReviewRating.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressbar);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        list = (ListView) findViewById(R.id.listview);
        close = (Button) findViewById(R.id.cancel_btn);
        ImageView image = (ImageView)findViewById(R.id.profile_image);
        super.onCreate(savedInstanceState);
        TextView name = (TextView) findViewById(R.id.t2);
        txt_rating=(TextView)findViewById(R.id.text2);
        lin_linkin=(LinearLayout)findViewById(R.id.lin_linkin);
        lin_linkininfo=(LinearLayout)findViewById(R.id.lin_linkininfo);
        txt_profilename=(TextView)findViewById(R.id.txt_profilename);
        imageprofile=(ImageView)findViewById(R.id.imageprofile);



        Intent i = getIntent();
        id = i.getStringExtra("userId");
        profile_image = i.getStringExtra("image");
        profilename = i.getStringExtra("name");

        detector = new SimpleGestureFilter(this,this);

        completerating();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        name.setText(profilename);
        if(profile_image.equals(""))
        {

        }
        else {

            Glide.with(LendReviewRating.this).load(profile_image).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(LendReviewRating.this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(image);

        }

        lin_linkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if(appInstalledOrNot("com.linkedin.android")) {
                    linkedlogin();
               /* }else{
                    Toast.makeText(getApplicationContext(),"App not installed ",Toast.LENGTH_LONG).show();
                    getAuthorizationToken();
                }*/
            }
        });
        lin_linkin.setVisibility(View.VISIBLE);

        thisActivity = this;
    }

    public void completerating() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:job_history::" + response);
                        onResponserecieved(response, 1);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof TimeoutError ||error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(LendReviewRating.this);
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
                            Toast.makeText(getApplicationContext(),"Authentication Failure while performing the request",Toast.LENGTH_LONG).show();
                        }else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(),"Network error while performing the request",Toast.LENGTH_LONG).show();
                        }else {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                System.out.println("error" + jsonObject);
                                String status = jsonObject.getString("msg");
                             //   if (status.equals("This User Currently Does Not Have Any Ratings")) {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(LendReviewRating.this);
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
                            //    }

                                if(jsonObject.isNull("linked_in_data"))
                                {
                                   /* lin_linkin.setVisibility(View.VISIBLE);
                                    lin_linkininfo.setVisibility(View.GONE);*/
                                    System.out.println("Null value");
                                }else{
                                 /*   lin_linkininfo.setVisibility(View.VISIBLE);
                                    lin_linkin.setVisibility(View.GONE);*/
                                    String linked_in_data=jsonObject.getString("linked_in_data");
                                    JSONObject obj=new JSONObject(linked_in_data);
                                    String id=obj.getString("id");
                                    String email=obj.getString("email");
                                    String first_name=obj.getString("first_name");
                                    String last_name=obj.getString("last_name");
                                    String profile_url=obj.getString("profile_url");
                                    String picture_url=obj.getString("picture_url");

                                    txt_profilename.setText("VIEW "+first_name+" "+last_name);

                                    Glide.with(LendReviewRating.this).load(picture_url).apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(LendReviewRating.this,0, Glideconstants.sCorner,Glideconstants.sColor, Glideconstants.sBorder)).error(R.drawable.default_profile)).into(imageprofile);

                                }

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
                params.put(TYPE, usertype);
                params.put(Constant.DEVICE, Constant.ANDROID);
                System.out.println("Params "+params);
                System.out.println("URL "+URL);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
      //  stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    public void onResponserecieved(String jsonobject, int i) {
        System.out.println("review rating response"+jsonobject);

        String status = null;
        String rating_list = null;

        try {
            JSONObject jResult = new JSONObject(jsonobject);
            status = jResult.getString("status");
            if(status.equals("success"))
            {
                rating_list = jResult.getString("rating_lists");
                JSONArray array = new JSONArray(rating_list);
                for(int n = 0; n < array.length(); n++) {
                    JSONObject object = (JSONObject) array.get(n);
                    final String employer = object.getString("employer");
                    date = object.getString("job_date");
                    JSONArray emparray = new JSONArray(employer);
                    avg_rat=object.getString("average_rating");
                    for(int a = 0; a < emparray.length(); a++) {
                        JSONObject obj=emparray.getJSONObject(a);
                        image = obj.getString("profile_image");
                        average_rating = obj.getString("rating");
                    }

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("image",image);
                    map.put("average",average_rating);
                    map.put("comments",object.getString("comments"));
                    map.put("date",date);
                    job_list.add(map);

                }


                ReviewAdapter arrayAdapter = new ReviewAdapter(this, job_list) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        // Get the current item from ListView
                        View view = super.getView(position, convertView, parent);
                        if (position % 2 == 1) {
                            // Set a background color for ListView regular row/item
                            view.setBackgroundColor(Color.parseColor("#BF178487"));
                        } else {
                            // Set the background color for alternate row/item
                            view.setBackgroundColor(Color.parseColor("#BFE8C64B"));
                        }
                        return view;
                    }
                };

                // DataBind ListView with items from ArrayAdapter
                list.setAdapter(arrayAdapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        view.setSelected(true);

                    }
                });

                txt_rating.setText("Rating: "+avg_rat);

            }
            else
            {

            }
            String linked_in_data=jResult.getString("linked_in_data");
            if(linked_in_data!=null && !linked_in_data.isEmpty()){

                JSONObject obj=new JSONObject(linked_in_data);
                String id=obj.getString("id");
                String email=obj.getString("email");
                String first_name=obj.getString("first_name");
                String last_name=obj.getString("last_name");
                String profile_url=obj.getString("profile_url");
                String picture_url=obj.getString("picture_url");

            }else{
                lin_linkin.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public void linkedlogin() {

        LISessionManager.getInstance(getApplicationContext()).init(thisActivity, buildScope(), new AuthListener() {
            @Override
            public void onAuthSuccess () {
                // Authentication was successful.  You can now do
                // other calls with the SDK.
                getPersonelinfo();
            }

            @Override
            public void onAuthError (LIAuthError error){
                // Handle authentication errors
                System.out.println("Error "+error.toString());
            }
        },true);

    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.W_SHARE,Scope.R_EMAILADDRESS);
    }

    public void getPersonelinfo(){
      //  String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,email,picture-url,profile_url)";
        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,email-address,picture-url,picture-urls::(original),positions,date-of-birth,phone-numbers,location)?format=json";
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!
                JSONObject json=apiResponse.getResponseDataAsJson();
                System.out.println("json res "+json);
             try{

                 lin_email=json.getString("emailAddress");
                 firstnmae=json.getString("firstName");
                 lastnmae=json.getString("lastName");
                 lin_id=json.getString("id");
                 String pictureUrl="";
                 if(json.has("pictureUrl")){
                     pictureUrl=json.getString("pictureUrl");
                 }
                 pictureurl=pictureUrl;
                 getpublicprofileurl();
              } catch (Exception e)
              {

               }
                System.out.println("Json "+json);
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!

                System.out.println("Json "+liApiError);
            }
        });
    }

    public void getpublicprofileurl(){
        final String profilurl = "https://api.linkedin.com/v1/people/~:(public-profile-url)?format=json";
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, profilurl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // Success!
                JSONObject json=apiResponse.getResponseDataAsJson();
                try{
                    String publicProfileUrl=json.getString("publicProfileUrl");
                    System.out.println("publicProfileUrl "+publicProfileUrl);
                    profileurl=publicProfileUrl;
                    UpdatelinkedingData();
                }catch (Exception e){
                    System.out.println("Exception e"+e.getMessage());
                }
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!

                System.out.println("Json "+liApiError);
            }
        });

    }
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }
    private  String getAuthorizationToken() {

       /* HttpClient httpclient = new DefaultHttpClient();
       // String url="https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id="+ Config.LINKEDIN_CLIENTID+"&redirect_uri=https://www.handzforhire.com&state=DLKDJF46ikMMZADfdfds&scope=r_basicprofile";

        String url="https://www.linkedin.com/uas/oauth2/authorization?response_type=code&client_id=81qt2f2mg525a4&redirect_uri=https%3A%2F%2Fwww.handzforhire.com&state=DLKDJF46ikMMZADfdfds&scope=r_basicprofile,r_emailaddress";

        HttpGet httpget = new HttpGet(url);
        System.out.println("URL "+url);
        try {
            //httpget.addHeader("content-type", "application/json");
           // httpget.addHeader("Authorization", "Bearer " + accesstoken);
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httpget);
            String responseContent = EntityUtils.toString(response.getEntity());
            System.out.println("Authorization"+responseContent);
         *//*   try {
                JSONObject obj = new JSONObject(responseContent);
                JSONObject obj1=obj.getJSONObject("referral_data");
                JSONObject obj2=obj1.getJSONObject("customer_data");
                JSONArray array=obj2.getJSONArray("partner_specific_identifiers");
                for(int i=0;i<array.length();i++){
                    JSONObject obj3=array.getJSONObject(i);
                    String trackiungvalue=obj3.getString("value");
                    System.out.println("tracking Value "+trackiungvalue);
                    getMerchantIdOfSeller(trackiungvalue,accesstoken);
                }


            }catch (Exception e)
            {
                System.out.println("e "+e.getMessage());
            }*//*


        } catch (ClientProtocolException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        } catch (IOException e) {
            System.out.println("Exception "+e.getMessage());
// TODO Auto-generated catch block
        }
        return null;*/
       // String url="https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=81qt2f2mg525a4&redirect_uri=https://www.handzforhire.com&state=DLKDJF46ikMMZADfdfds&scope=r_basicprofile,r_emailaddress";
        //String url="https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=81qt2f2mg525a4&redirect_uri=https%3A%2F%2Fwww.handzforhire.com%2Fauth%2Flinkedin&state=DLKDJF46ikMMZADfdfds&scope=r_basicprofile";
        //  String url="https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id="+ Config.LINKEDIN_CLIENTID+"&redirect_uri=https://www.handzforhire.com&state=DLKDJF46ikMMZADfdfds&scope=r_basicprofile";
       /* String url = getAuthorizationUrl();
        System.out.println("Authorization url "+url);
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                System.out.println("Api response "+apiResponse);
                // Success!
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                // Error making GET request!
                System.out.println("Api response "+liApiError);
            }
        });
*/

      //  String myUrl = "http://myApi.com/get_some_data";
        //String to place our result in
        String result;
        //Instantiate new instance of our class
        HttpGetRequest getRequest = new HttpGetRequest();
        //Perform the doInBackground method, passing in our url
        try {
            result = getRequest.execute(getAuthorizationUrl()).get();
            System.out.println("Result "+result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

           /* HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://www.linkedin.com/oauth/v2/accessToken");
            //HttpPost httppost = new HttpPost("https://api.sandbox.paypal.com/v1/oauth2/token");

            try {
                //String text=PayPalConfig.PAYPAL_CLIENT_ID+":"+PayPalConfig.PAYPAL_SECRET_KEY;
                httppost.addHeader("content-type", "application/x-www-form-urlencoded");
                StringEntity se=new StringEntity("grant_type=client_credentials&client_id="+Config.LINKEDIN_CLIENTID+"&client_secret="+Config.LINKEDIN_SECRETKEY);
                httppost.setEntity(se);
                HttpResponse response = httpclient.execute(httppost);
                String responseContent = EntityUtils.toString(response.getEntity());
                Log.d("Response", responseContent );
                try {
                    JSONObject obj = new JSONObject(responseContent);
                    System.out.println(obj.getString("access_token"));

                }catch (Exception e)
                {
                    System.out.println("e "+e.getMessage());
                }
                System.out.println("Response "+responseContent);

            } catch (ClientProtocolException e) {
                System.out.println("Exception "+e.getMessage());

            } catch (IOException e) {
                System.out.println("Exception " + e.getMessage());
            }*/

        return null;
    }

    private static String getAuthorizationUrl(){
        return AUTHORIZATION_URL
                +QUESTION_MARK+RESPONSE_TYPE_PARAM+EQUALS+RESPONSE_TYPE_VALUE
                +AMPERSAND+CLIENT_ID_PARAM+EQUALS+API_KEY
                +AMPERSAND+STATE_PARAM+EQUALS+STATE
                +AMPERSAND+REDIRECT_URI_PARAM+EQUALS+REDIRECT_URI;
    }

    public class HttpGetRequest extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;
        @Override
        protected String doInBackground(String... params){
            String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            }catch(IOException e){
                e.printStackTrace();
                result = null;
            }
            return result;
        }
    }


    public void UpdatelinkedingData() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LINKEDIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        onResponserecieved(response, 1);
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            final Dialog dialog = new Dialog(LendReviewRating.this);
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
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(), "Authentication Failure while performing the request", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "Network error while performing the request", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject jsonObject = new JSONObject(responseBody);
                                System.out.println("error" + jsonObject);
                                String status = jsonObject.getString("msg");
                                if (status.equals("This User Currently Does Not Have Any Ratings")) {
                                    // custom dialog
                                    final Dialog dialog = new Dialog(LendReviewRating.this);
                                    dialog.setContentView(R.layout.custom_dialog);

                                    // set the custom dialog components - text, image and button
                                    TextView text = (TextView) dialog.findViewById(R.id.text);
                                    text.setText("This User Currently Does Not Have Any Ratings");
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
                                }
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
                params.put(KEY_USERID, lin_id);
                params.put(FIRST_NAME, firstnmae);
                params.put(LAST_NAME, lastnmae);
                params.put(ID, id);
                params.put(PROF_URL, profileurl);
                params.put(PIC_URL, pictureurl);
                params.put(EMAIL, lin_email);
                params.put(Constant.DEVICE, Constant.ANDROID);

                System.out.println("Params " + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(timeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}
