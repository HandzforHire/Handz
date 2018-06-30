package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Window;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.jibble.simpleftp.SimpleFTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class FileUpload{

    public static String user_id;
    String type;
    public static String firstRemoteFile;
    private static final String URL = Constant.SERVER_URL+"update_profile_image";
    public static String KEY_USERID = "user_id";
    public static String KEY_PROFILE_IMAGE = "profile_image";
    public static String KEY_PROFILE_NAME = "profile_name";
    public static String APP_KEY = "X-APP-KEY";
    String value = "HandzForHire@~";
    String id;
   // Activity activity;

    /*********  work only for Dedicated IP ***********/
    static final String FTP_HOST= "162.144.41.156";

    /*********  FTP USERNAME ***********/
    static final String FTP_USER = "server@izaapinnovations.com";

    /*********  FTP PASSWORD ***********/
    static final String FTP_PASS  ="Y9+CW:K_o[";
    Dialog dialog;
    Activity cntxt;
    Activity activity;
    public FileUpload(String file,String ids, Activity cntxt){

        System.out.println("save profile");
        this.cntxt=cntxt;
        activity=cntxt;
        id=ids;
        new UploadFile().execute(file);

    }

    public class UploadFile extends AsyncTask<String, Void, Boolean> {

        SimpleFTP ftp;
        //String filename;
        Boolean status=false;

        String ss;

        @Override
        protected Boolean doInBackground(String... params) {
            //id = EditUserProfile.id;
            System.out.println("iiiiiiiiiiiid:fileupload::"+id);
           // ss=params[0];

            FTPClient ftpClient = new FTPClient();
            try {

                ftpClient.connect(FTP_HOST, 21);
                ftpClient.login(FTP_USER, FTP_PASS);
                ftpClient.enterLocalPassiveMode();

                ftpClient.changeWorkingDirectory("handzforhire/assets/images/uploads/profile");

                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                // APPROACH #1: uploads first file using an InputStream
                File firstLocalFile = new File(params[0]);
                System.out.println("firstLocalFile "+firstLocalFile);

                DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
                Date date = new Date();

                firstRemoteFile = dateFormat.format(date)+"_"+String.valueOf(System.currentTimeMillis())+".jpg";

                //firstRemoteFile ="large_"+mSharedData.getUserId()+".jpg";


                System.out.println("filename "+firstRemoteFile);
                InputStream inputStream = new FileInputStream(firstLocalFile);
                System.out.println("Start uploading first file");
                boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
                System.out.println("Done " + done);
                inputStream.close();
                if (done) {
                    status=true;
                }


                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();

                }


            } catch (IOException ex) {
                System.out.println("IO Error: " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                // status=false;
                try {
                    if (ftpClient.isConnected()) {
                        ftpClient.logout();
                        ftpClient.disconnect();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                    System.out.println("Exception\t "+ex.getMessage());
                }
            }
            return status;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(s = true)
            {
                imageUpload();
            }
            else
            {
            }
            // hideProgressDialog();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new Dialog(cntxt);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progressbar);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.show();
            //	txt_loading.setVisibility(View.VISIBLE);
            //  showProgressDialog();
        }


        private void showProgressDialog() {
           /* if (!pDialog.isShowing())
                pDialog.show();*/
        }

        private void hideProgressDialog() {
         /*   if (pDialog.isShowing())
                pDialog.dismiss();*/
        }

    }

    public void imageUpload() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("resssssssssssssssss:" + response);
                        onResponserecieved(response, 2);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        /*try {
                            String responseBody = new String( error.networkResponse.data, "utf-8" );
                            JSONObject jsonObject = new JSONObject( responseBody );
                            System.out.println("error"+jsonObject);
                        } catch ( JSONException e ) {
                            //Handle a malformed json response
                        } catch (UnsupportedEncodingException error1){

                        }*/
                        if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
                            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                            volleyError = error;
                            System.out.println("error" + volleyError);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(APP_KEY, value);
                map.put(KEY_PROFILE_IMAGE, firstRemoteFile);
                map.put(KEY_USERID, id);
                System.out.println("id "+id);
                System.out.println("firstRemoteFile "+firstRemoteFile);
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
                //Toast.makeText(activity,"Image Uploaded Successfully",Toast.LENGTH_LONG).show();
            }
            else {

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
