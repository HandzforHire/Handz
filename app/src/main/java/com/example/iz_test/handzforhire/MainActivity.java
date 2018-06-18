package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;

public class MainActivity extends Activity {

    Button need_hand,lend_hand;
    LinearLayout promo_video;
    public static final int RequestPermissionCode = 1;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());

        if(checkPermission()){

            //Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();

        }
        else {

            requestPermission();
        }

        need_hand = (Button) findViewById(R.id.get_handz);
        lend_hand = (Button) findViewById(R.id.give_handz);
        promo_video = (LinearLayout) findViewById(R.id.promo_video);
        TextView text = (TextView) findViewById(R.id.text);
        TextView text1 = (TextView) findViewById(R.id.text1);

        String fontPath = "fonts/LibreFranklin-SemiBold.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        text.setTypeface(tf);
        text1.setTypeface(tf);
        need_hand.setTypeface(tf);
        lend_hand.setTypeface(tf);

        text.setText(Html.fromHtml("Handz is currently offered in the Jacksonville,FL \n area. Want Handz in your area? <u>Click here.</u>"));

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,PromoVideo.class);
                startActivity(i);
            }
        });

        need_hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Boolean loginstatus = session.getLoginStatus();
                System.out.println("ppppppppppp:dispatch:" +loginstatus);
                if(loginstatus == true)
                {
                    Intent i = new Intent(MainActivity.this, ProfilePage.class);
                    startActivity(i);
                    finish();
                    String a = "if_condition";
                    System.out.println("pppppppp:::::::"+a);
                }
                else
                {
                    Intent in = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(in);
                    finish();
                    String b = "else_condition";
                    System.out.println("pppppppp:::::::"+b);
                }
            }
        });

        lend_hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Boolean status = session.isLoggedIn();
                System.out.println("ppppppppp:florist:" +status);
                if(status == true)
                {
                    Intent intent = new Intent(MainActivity.this,FindJobMap.class);
                    startActivity(intent);
                    finish();
                    String a = "if_condition";
                    System.out.println("pppppppp:::::::"+a);
                }
                else
                {
                    Intent i = new Intent(MainActivity.this, LendLoginPage.class);
                    startActivity(i);
                    finish();
                    String b = "else_condition";
                    System.out.println("pppppppp:::::::"+b);
                }
            }
        });

        promo_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,PromoVideo.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {
                        CAMERA,
                        ACCESS_FINE_LOCATION,
                        READ_PHONE_STATE,
                        READ_EXTERNAL_STORAGE
                }, RequestPermissionCode);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadContactsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPhoneStatePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadExternalStoragePermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && ReadContactsPermission && ReadPhoneStatePermission && ReadExternalStoragePermission) {

                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this,"Permission Denied",Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int ForthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ForthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

}
