package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IZ-Parimala on 09-07-2018.
 */

public class ChatAdapter extends BaseAdapter {

    private final List<ChatItems> chatMessages;
    private Activity context;
    String photourl;
    public static StorageReference storageReference;
    FirebaseStorage storage;
    public ChatAdapter(Activity context, List<ChatItems> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl(ChatNeed.storepath);
    }

    @Override
    public int getCount() {
        if (chatMessages != null) {
            return chatMessages.size();
        } else {
            return 0;
        }
    }

    @Override
    public ChatItems getItem(int position) {
        if (chatMessages != null) {
            return chatMessages.get(position);
        } else {
            return null;
        }
    }
    @Override
    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ChatItems chatMessage = getItem(position);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = vi.inflate(R.layout.list_item_chat_message, null);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        }
            holder = (ViewHolder) convertView.getTag();


        boolean myMsg = chatMessage.isMe() ;//Just a dummy check
        setAlignment(holder, myMsg);
        if(chatMessage.getMessage().equals("FROM HANDZ: Just a reminder that payment has not been completed on this job! Have a great day!"))
            holder.txtMessage.setBackgroundResource(R.drawable.gray_bg_c_9);
        holder.txtMessage.setText(chatMessage.getMessage());
        if(chatMessage.has_Attachemnt) {
            DownloadImage(chatMessage.getPhotoURL(),holder.img_view);
            holder.txtMessage.setVisibility(View.GONE);
        }else{
            holder.img_view.setVisibility(View.GONE);
        }

        return convertView;
    }

    public void add(ChatItems message) {
        chatMessages.add(message);
    }

    public void add(List<ChatItems> messages) {

        chatMessages.addAll(messages);
    }

    private void setAlignment(ViewHolder holder, boolean isMe) {
        if (!isMe) {
            //holder.contentWithBG.setBackgroundResource(R.drawable.green_bg_c);
            holder.txtMessage.setBackgroundResource(R.drawable.green_bg_c);
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.img_view.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.img_view.setLayoutParams(layoutParams);
        } else {
           // holder.contentWithBG.setBackgroundResource(R.drawable.yellow_bg_c);
            holder.txtMessage.setBackgroundResource(R.drawable.yellow_bg_c);
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMessage.setPadding(30,0,10,0);
            holder.txtMessage.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.img_view.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.img_view.setLayoutParams(layoutParams);
        }
    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        holder.content = (LinearLayout) v.findViewById(R.id.content);
        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
        holder.img_view = (ImageView) v.findViewById(R.id.img_view);
        return holder;
    }

    private static class ViewHolder {
        public TextView txtMessage;
        public ImageView img_view;
        public LinearLayout content;
        public LinearLayout contentWithBG;
    }
    private String DownloadImage(String url,final ImageView image_view){
        photourl="";
        String urls=url.replace( ChatNeed.storepath,"");
        storageReference.child(urls).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
               // Glide.with(image_view.getContext()).load(uri.toString()).into(image_view);

                RequestManager requestManager = Glide.with(context);
                requestManager.load(uri.toString()).into(image_view);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                System.out.println("URI "+exception.getMessage());
            }
        });
        System.out.println("on download place "+photourl);
        return photourl;
    }

}
