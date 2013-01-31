package com.bdc.android.helloandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created with IntelliJ IDEA.
 * User: mahesh1
 * Date: 1/12/13
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class Login extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button button =(Button) findViewById(R.id.button);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                //Intent gotoNextActivity = new Intent(getApplicationContext(),MyActivity.class);
               Intent gotoNextActivity = new Intent(getApplicationContext(), AppointmentCalendar.class);
                startActivity(gotoNextActivity);

            }
        });


    }
}