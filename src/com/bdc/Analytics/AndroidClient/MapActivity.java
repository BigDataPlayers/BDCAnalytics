package com.bdc.Analytics.AndroidClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;



public class MapActivity extends Activity {

    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapactivity);
         imView = (ImageView)findViewById(R.id.imageView_GeoMap);

        progressBar = new ProgressDialog(imView.getContext());
        progressBar.setCancelable(true);
        progressBar.setMessage("File downloading ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);
        progressBar.setMax(1000);
        progressBarStatus = 0;
        progressBar.show();

        new Thread(new Runnable() {
            public void run() {
                while (progressBarStatus < 1000) {

                    progressBarStatus++;


                    // Update the progress bar
                    progressBarHandler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressBarStatus);
                        }
                    });

                    // your computer is too fast, sleep 1 second
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }


            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {

                downloadFile();
            }
        }).start();







       // downloadFile();
    }



  ImageView imView;

    Bitmap bmImg;
    void downloadFile(){
        URL myFileUrl =null;
        try {
            myFileUrl= new URL("http://ec2-23-22-121-65.compute-1.amazonaws.com/ChartType=geoChart");
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }
        try {
            HttpURLConnection conn= (HttpURLConnection)myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();

            bmImg = BitmapFactory.decodeStream(is);
            progressBarHandler.post(new Runnable() {
                public void run() {
                    imView.setImageBitmap(bmImg);
                    progressBar.dismiss();
                }
            });
          // imView.setImageBitmap(bmImg);

        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}