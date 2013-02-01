package com.bdc.analytics.androidClient;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;


public class MyOldActivity extends Activity

{

      Button nav;

    WebView web;

    final MyOldActivity activity= this;

    @Override



    public void onCreate(Bundle savedInstanceState)

    {

        // activity = this;

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);




        nav = (Button)findViewById(R.id.button);

        //web = (WebView)findViewById(R.id.webView);




        nav.setOnClickListener(new Button.OnClickListener() {




            public void onClick(View view) {


              /* Uri uriUrl = Uri.parse("http://192.168.6.23:8080/clicks.png");

                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);

                startActivity(launchBrowser);   */


                LinearLayout detailListView = (LinearLayout) findViewById(R.id.layout); // Find the layout where you want to add button

                web = new WebView(detailListView.getContext());

                // web.setLayoutParams();

              //  web.loadUrl("http://chart.googleapis.com/chart?cht=p3&chs=250x100&chd=t:30,30,40&chl=Hello|World|Again");

                web.loadUrl("http://192.168.6.23:8080/clicks.png");







               final Button button = new Button(activity);

                button.setText("Done");

                detailListView.addView(button);//add view to add

                detailListView.addView(web);

                button.setOnClickListener(new Button.OnClickListener() {

                    public void onClick(View view) {

                        LinearLayout detailListView = (LinearLayout) findViewById(R.id.layout); // Find the layout where you want to add button

                        detailListView.removeView(web);

                        detailListView.removeView(button);

                    }

                });

            }

        });

    }






    public void getFileData()

    {

        // Uri uriUrl = Uri.parse("http://androidbook.blogspot.com/");

        // Uri uriUrl = Uri.parse("http://192.168.6.23:8080/snap");

        Uri uriUrl = Uri.parse("http://chart.googleapis.com/chart?cht=p3&chs=250x100&chd=t:60,40&chl=Hello|World");

        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);

        startActivity(launchBrowser);

    }



    /*
    public void onCreate(Bundle savedInstanceState)

    {

        // activity = this;

        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);


    }       */


}