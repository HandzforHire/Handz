package com.example.iz_test.handzforhire;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MakePaymentNew extends Activity
{
    ImageView click,click1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_payment_new);
        click=(ImageView)findViewById(R.id.imgg);
        click1=(ImageView)findViewById(R.id.im);

        click1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                final Dialog dialog = new Dialog(MakePaymentNew.this);
                dialog.setContentView(R.layout.custom_dialognew);
                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("You may add a tip for a job well done, or if the job took longer than expected.");
                Button dialogButton = (Button) dialog.findViewById(R.id.ok);

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
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(MakePaymentNew.this);
                dialog.setContentView(R.layout.custom_dialognew);

                TextView text = (TextView) dialog.findViewById(R.id.text);
                text.setText("This fees may minimally increase if you choose to add a tip");
                Button dialogButton = (Button) dialog.findViewById(R.id.ok);

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

}
