package com.bdc.analytics.androidClient;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: a09726a
 * Date: 4/8/13
 * Time: 6:10 PM
 * To change this template use File | settings | File Templates.
 */
public class SettingsActivity extends Activity {
    Button button, button1, button2 ;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        setEditText (getURL()) ;

        button = (Button) findViewById(R.id.button_session);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                SharedPreferences settings = getSharedPreferences(AndroidConstants.ANALYTICS_SERVER_PREF, 0);
                SharedPreferences.Editor editor = settings.edit();

                EditText text = (EditText) findViewById(R.id.editText_Settings);
                editor.putString(AndroidConstants.WEB_URL, text.getText().toString());

                // Commit the edits!
                editor.commit();
                finish();
            }
        });

        button1 = (Button) findViewById(R.id.button_perm);
        button1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                try {
                    FileOutputStream fos = openFileOutput(AndroidConstants.FILENAME, Context.MODE_PRIVATE);
                    EditText text = (EditText) findViewById(R.id.editText_Settings);
                    fos.write(text.getText().toString().getBytes());
                    fos.close();
                    button.performClick();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                finish();
            }
        });

        button2 = (Button) findViewById(R.id.button_restore);
        button2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                try {
                    setEditText(AndroidConstants.DEFAULT_ANALYTICS_SERVER);
                    button1.performClick();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                finish();
            }
        });

    }

    private String getURL(){
        SharedPreferences settings = getSharedPreferences(AndroidConstants.ANALYTICS_SERVER_PREF, 0);
        return settings.getString(AndroidConstants.WEB_URL, AndroidConstants.DEFAULT_ANALYTICS_SERVER);
    }

    private void setEditText (String str) {
        EditText text = (EditText) findViewById(R.id.editText_Settings);
        text.setText(str, EditText.BufferType.EDITABLE);
    }
}
