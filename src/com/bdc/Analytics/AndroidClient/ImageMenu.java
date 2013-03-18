package com.bdc.analytics.androidClient;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.example.BDCAnalytics.R;

/**
 * Created with IntelliJ IDEA.
 * User: mahesh1
 * Date: 1/12/13
 * Time: 10:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageMenu extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagemenu);

        Button b1 = (Button)findViewById(R.id.button);
        Button b2 = (Button)findViewById(R.id.button1);
        ImageButton b3 = (ImageButton)findViewById(R.id.imageButton);

        b1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                ImageView iview = (ImageView)findViewById(R.id.imageView)  ;
                iview.setImageResource(R.drawable.unimobi);

            }
        });

        b2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                ImageView iview = (ImageView)findViewById(R.id.imageView)  ;
                iview.setImageResource(R.drawable.ic_launcher);

            }
        });





    }
}