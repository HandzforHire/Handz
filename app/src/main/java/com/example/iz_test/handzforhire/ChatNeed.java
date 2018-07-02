package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*Hi for testing*/
public class ChatNeed extends Activity {

    LinearLayout layout,lin_layoutmsg;
    RelativeLayout layout2;
    ImageView sendButton,close_btn;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    String get_user,job_id,channel_id,user_id,user_type,sender_id,child_id,key;
    TextView Tv,txt_sendmsg;
    SessionManager session;
    String current_user_id = "OGO6K8nyqKVJ8WQoE02WT5qFc1S2";

    //To upload images
    FirebaseStorage storage;
    StorageReference storageReference;
    String storepath="gs://handz-8ac86.appspot.com";

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout) findViewById(R.id.layout1);
        lin_layoutmsg=(LinearLayout)findViewById(R.id.lin_layoutmsg);
        layout2 = (RelativeLayout) findViewById(R.id.layout2);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        close_btn = (ImageView) findViewById(R.id.img1);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        Tv = (TextView) findViewById(R.id.txt1);
        txt_sendmsg=(TextView)findViewById(R.id.txt_sendmsg);

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


        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://handz-8ac86.firebaseio.com/channels");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl(storepath);

        //reference2 = new Firebase("https://chatapp-85e06.firebaseio.com/Channels_lend/" + Chat_Need.channel_id + Chat_Need.job_id).child("Messages");

        txt_sendmsg.setOnClickListener(new View.OnClickListener() {
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

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
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

                lin_layoutmsg.removeAllViews();
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    key = messageSnapshot.getKey();
                    System.out.println("fffffffff:key::" + key.substring(0,3));
                    System.out.println("message snapshot "+messageSnapshot);
                    if (key.equals(child_id)) {
                        for (DataSnapshot recipient : messageSnapshot.child("messages").getChildren()) {

                            if(recipient.hasChild("photoURL")) {
                                String photoURL = String.valueOf(recipient.child("photoURL").getValue());
                                DownloadImage(photoURL,String.valueOf(recipient.child("senderId").getValue()));
                                System.out.println("photoURL " + photoURL);
                            }else if(recipient.hasChild("text")){
                                String text = String.valueOf(recipient.child("text").getValue());
                                System.out.println("fffffffff:text::" + text);
                                System.out.println("fffffffff:id::" + recipient.child("senderId").getValue());
                                if(!text.equals("null")) {
                                    addMessageBox(text,String.valueOf(recipient.child("senderId").getValue()));
                                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            uploadImage();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
              //  imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
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

    public void adImage(String url,String senderid)
    {

        ImageView img=new ImageView(ChatNeed.this);
        Glide.with(ChatNeed.this).load(url).into(img);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.setMargins(0, 10, 0, 0);
        lp2.weight = 2.0f;
        img.setMaxWidth(200);
        img.setMinimumHeight(200);
        // System.out.println("usr_type chatneed "+user_type);
        if(sender_id.equals(senderid))
        {

            lp2.gravity = Gravity.LEFT;
            // System.out.println("condtion matched");
        }
        else{
            lp2.gravity = Gravity.RIGHT;
        }
        lin_layoutmsg.addView(img);
        img.setLayoutParams(lp2);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            String randomno=UUID.randomUUID().toString();
            final String photourl=storepath+"/"+child_id+"/"+randomno+".jpg";
            StorageReference ref = storageReference.child(child_id+"/"+randomno+".jpg");

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("senderId", sender_id);
                            map.put("senderName", get_user);
                            map.put("photoURL", photourl);
                            reference1.child(child_id).child("messages").push().setValue(map);
                           // addMessageBox(messageText,sender_id);
                            messageArea.setText("");
                          //  Toast.makeText(ChatNeed.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ChatNeed.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    private void DownloadImage(String url, final String senderid){
        System.out.println("url"+url);
        String urls=url.replace(storepath,"");
        storageReference.child(urls).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                System.out.println("URI "+uri);
                adImage(uri.toString(),senderid);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                System.out.println("URI "+exception.getMessage());
            }
        });
    }
}
