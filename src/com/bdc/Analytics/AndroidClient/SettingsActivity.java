package com.bdc.analytics.androidClient;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created with IntelliJ IDEA.
 * User: a09726a
 * Date: 4/8/13
 * Time: 6:10 PM
 * To change this template use File | settings | File Templates.
 */
public class SettingsActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);



        Button button =(Button) findViewById(R.id.button_OK);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences settings = getSharedPreferences(AndroidConstants.ANALYTICS_SERVER_PREF, 0);
                SharedPreferences.Editor editor = settings.edit();

                EditText text = (EditText) findViewById(R.id.editText_Settings);
                editor.putString(AndroidConstants.WEB_URL, text.getText().toString());

                // Commit the edits!
                editor.commit();

//                settings.getString(AndroidConstants.WEB_URL, AndroidConstants.DEFAULT_ANALYTICS_SERVER);

               finish();
            }
        });

        Button button1 =(Button) findViewById(R.id.button_cancel);
        button1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                finish();
            }
        });

    }

}
