package com.bdc.Analytics.AndroidClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created with IntelliJ IDEA.
 * User: mahesh1
 * Date: 1/12/13
 * Time: 2:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class SplashScreen extends Activity {

   // Activity activity = this;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        try {
        Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        Intent goToNextActivity = new Intent(getApplicationContext(), Login.class);
        Intent goToNextActivity = new Intent(getApplicationContext(), MyActivity.class);
//        Intent goToNextActivity = new Intent(getApplicationContext(), AppointmentCalendar.class);
        startActivity(goToNextActivity);

    }
}