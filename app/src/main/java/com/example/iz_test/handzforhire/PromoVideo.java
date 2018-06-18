package com.example.iz_test.handzforhire;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

public class PromoVideo extends Activity{

    VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.promo_video);

        video = (VideoView)findViewById(R.id.promo);
        ImageView logo = (ImageView) findViewById(R.id.logo);

       /* MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(video);*/
        String path = "android.resource://" + getPackageName() + "/" + R.raw.video;
        //video.setMediaController(mediaController);
        video.setVideoURI(Uri.parse(path));
        video.start();

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PromoVideo.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
