package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class ChatNeed extends Activity {

    LinearLayout layout,lin_layoutmsg;
    RelativeLayout layout2;
    ImageView close_btn;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    String get_user,job_id,channel_id,user_id,user_type,sender_id,child_id,key;
    TextView Tv,sendButton;
    SessionManager session;
    String current_user_id = "OGO6K8nyqKVJ8WQoE02WT5qFc1S2";
    private final int PICK_IMAGE_REQUEST = 71;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout) findViewById(R.id.layout1);
        lin_layoutmsg=(LinearLayout)findViewById(R.id.lin_layoutmsg);
        layout2 = (RelativeLayout) findViewById(R.id.layout2);
        sendButton = (TextView) findViewById(R.id.sendButton);
        close_btn = (ImageView) findViewById(R.id.img1);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        Tv = (TextView) findViewById(R.id.txt1);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        user_type = user.get(SessionManager.USER_TYPE);
        System.out.println("uuuuuuuuuuu:chatneed:::user_type::::" + user_type);

        Intent i = getIntent();
        job_id = i.getStringExtra("jobId");
        channel_id = i.getStringExtra("channel");
        get_user = i.getStringExtra("username");
        user_id = i.getStringExtra("userId");
        Tv.setText(get_user);
        sender_id = current_user_id + user_id;
        child_id = channel_id + job_id;

        /*get_job_id=i.getStringExtra("jobId");
        get_user_id=i.getStringExtra("channel");
        get_channel_id=i.getStringExtra("username");*/

        System.out.println("jobbbbbbbbbbbb" + job_id);
        System.out.println("jobbbbbbbbbbbb" + channel_id);
        System.out.println("jobbbbbbbbbbbb:" + get_user);
        System.out.println("jobbbbbbbbbbbb:" + user_id);

        System.out.println("User type  "+user_type);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://handz-8ac86.firebaseio.com/channels");
        //reference2 = new Firebase("https://chatapp-85e06.firebaseio.com/Channels_lend/" + Chat_Need.channel_id + Chat_Need.job_id).child("Messages");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();
                System.out.println("uuuuuuuuuuu:chatneed:::messageText::::" + messageText);

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("senderId", sender_id);
                    map.put("senderName", get_user);
                    map.put("text", messageText);
                    reference1.child(child_id).child("messages").push().setValue(map);
                    addMessageBox(messageText,sender_id);
                    messageArea.setText("");
                   /* if(user_type.equals("employer"))
                    {
                        addMessageBox("\n" + messageText, 1);
                    }
                    else{
                        addMessageBox("\n"+ "" + messageText, 2);
                    }*/

                    System.out.println("senderid "+sender_id);
                    System.out.println("senderName "+get_user);
                    System.out.println("text "+messageText);
                    System.out.println("child_id "+child_id);
                }
            }
        });

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("data snaopshot"+dataSnapshot);
                lin_layoutmsg.removeAllViews();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    key = messageSnapshot.getKey();
                    System.out.println("fffffffff:key::" + key.substring(0,3));
                    if (key.equals(child_id)) {
                        for (DataSnapshot recipient : messageSnapshot.child("messages").getChildren()) {
                            //String id = String.valueOf(recipient.child("senderId").getValue());
                       // String name = String.valueOf(recipient.child("senderName").getValue());
                        String text = String.valueOf(recipient.child("text").getValue());
                        //System.out.println("fffffffff:id::" + id);
                        //System.out.println("fffffffff:name::" + name);
                            System.out.println("fffffffff:text::" + text);
                            System.out.println("fffffffff:id::" + recipient.child("senderId").getValue());
                            if(!text.equals("null")) {
                                addMessageBox(text,String.valueOf(recipient.child("senderId").getValue()));
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


       /* reference1.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
               *//* Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("Text").toString();
                String channel_id = map.get("SenderId").toString();
                String get_user=map.get("SenderName").toString();
                System.out.println("uuuuuuuuuuu:chatneed:::message::::"+message);
                System.out.println("uuuuuuuuuuu:chatneed:::channel_id::::"+channel_id);
                System.out.println("uuuuuuuuuuu:chatneed:::get_user::::"+get_user);
                System.out.println("uuuuuuuuuuu:chatneed:::user_type::::"+user_type);
*//*

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {

            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });*/

    public void addMessageBox(String message,String senderid)
    {
        TextView textView = new TextView(ChatNeed.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 2.0f;
       // System.out.println("usr_type chatneed "+user_type);
        if(sender_id.equals(senderid))
        {
            textView.setBackgroundResource(R.drawable.yellow_bg_c);
            lp2.gravity = Gravity.LEFT;
            textView.setGravity(View.FOCUS_LEFT);
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setFitsSystemWindows(true);
           // System.out.println("condtion matched");
        }
        else{

            textView.setBackgroundResource(R.drawable.green_bg_c);
            lp2.gravity = Gravity.RIGHT;
            textView.setGravity(View.FOCUS_LEFT);
            textView.setTextColor(Color.parseColor("#FFFFFF"));
            textView.setFitsSystemWindows(true);
        }
        textView.setLayoutParams(lp2);
        lin_layoutmsg.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    public static void uploadfile(){

    }
}
