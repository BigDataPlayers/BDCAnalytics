package com.bdc.analytics.androidClient;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.FloatMath;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;

import android.webkit.WebSettings;
import android.webkit.WebView;

import android.widget.*;


public class MyActivity extends Activity implements View.OnTouchListener
{
    private static final String TAG = "MyActivity";

    ImageButton ibBubble, ibColumn, ibPie, ibBar, ibArea, ibMap, ibDashBoard, ibCandle;
    WebView web = null;

    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    int mode = NONE;

    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;

    //    RelativeLayout detailListView ;
    /*   Spinner spinner ;
    String currentSpinnerSelection = "";

    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
*/
    public void onCreate(Bundle savedInstanceState)

    {
        // activity = this;
     super.onCreate(savedInstanceState);
     setContentView(R.layout.main);

//        detailListView = (RelativeLayout) findViewById(R.id.layout);
        /*spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.charts_array, analytics.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(analytics.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
               spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                 currentSpinnerSelection = adapterView.getItemAtPosition(i).toString();
//                System.out.println("Spinner Selection:" + currentSpinnerSelection);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
*/
        web = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        web.setVisibility(View.GONE);
        web.loadUrl(AndroidConstants.ANALYTICS_SERVER + "/ChartType=bubbleChart");


        ibBubble = (ImageButton) findViewById(R.id.imageButton_Bubble);
        ibColumn = (ImageButton) findViewById(R.id.imageButton_Column);
        ibPie = (ImageButton) findViewById(R.id.imageButton_Pie);
        ibBar = (ImageButton) findViewById(R.id.imageButton_bar);
        ibArea = (ImageButton) findViewById(R.id.imageButton_Area);
        ibMap = (ImageButton) findViewById(R.id.imageButton_Map);
        ibDashBoard = (ImageButton) findViewById(R.id.imageButton_DashBoard);
        ibCandle = (ImageButton) findViewById(R.id.imageButton_Candle);

        ibBubble.setOnClickListener(new ButtonListener("bubbleChart"));
        ibColumn.setOnClickListener(new ButtonListener("barChart"));
        ibPie.setOnClickListener(new ButtonListener("pieChart"));
        ibBar.setOnClickListener(new ButtonListener("columnChart"));
        ibArea.setOnClickListener(new ButtonListener("areaChart"));
        ibMap.setOnClickListener(new ButtonListener("treeMap"));
        ibDashBoard.setOnClickListener(new ButtonListener("dashBoard"));
        ibCandle.setOnClickListener(new ButtonListener("candleChart"));

    }


    void downloadFile(String charType) {

        System.out.println("Request URL:: "+ AndroidConstants.ANALYTICS_SERVER +"/ChartType=" + charType);
        if (charType.equals("geoChart")) {
            Intent gotoNextActivity = new Intent(getApplicationContext(), MapActivity.class);
            // Intent goToNextActivity = new Intent(getApplicationContext(), ImageMenu.class);
            startActivity(gotoNextActivity);

        } else {
            web.setVisibility(View.VISIBLE);
            web.setVerticalScrollBarEnabled(true);
            web.setHorizontalScrollBarEnabled(true);
            web.loadUrl(AndroidConstants.ANALYTICS_SERVER + "/ChartType=" + charType);

        }


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Handle touch events here...
        dumpEvent(event);
        ImageView view = (ImageView) v;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG" );
                mode = DRAG;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                Log.d(TAG, "mode=NONE" );
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM" );
                }
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                }
                else if (mode == ZOOM) {
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        // Perform the transformation
        view.setImageMatrix(matrix);

        return true; // indicate event was handled
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private class ButtonListener implements Button.OnClickListener {
        String chartType = "";

        public ButtonListener(String chart) {
            chartType = chart;
        }

        public void onClick(View view) {

            // EditText tView = (EditText)findViewById(R.id.editText);

//               final String file  = tView.getText().toString();
            //   downloadFile("http://192.168.6.23:8080/" + file);

            // web.loadUrl("http://timesofindia.indiatimes.com/city/mumbai/This-Bangladesh-houses-rural-Maharashtrians/articleshow/18026714.cms");


            downloadFile(chartType);
//
//                String s = downloadFile("http://192.168.6.23:8080/clicks.html");

           /*     RelativeLayout detailListView = (RelativeLayout) findViewById(R.id.layout); // Find the layout where you want to add button

                  web = new WebView(detailListView.getContext());


                // web.setLayoutParams();

                //  web.loadUrl("http://chart.googleapis.com/chart?cht=p3&chs=250x100&chd=t:30,30,40&chl=Hello|World|Again");

               web.loadUrl("http://192.168.6.23:8080/clicks.html");
                detailListView.addView(web);

                //   web.loadData(s,mimeType,encoding);

                //tView.setText(s);

            /*   Uri uriUrl = Uri.parse("http://192.168.6.23:8080/clicks.html");

                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);

                startActivity(launchBrowser);    */

             /*
                progressBar = new ProgressDialog(view.getContext());
                progressBar.setCancelable(true);
                progressBar.setMessage("File downloading ...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                progressBarStatus = 0;
                progressBar.show();

                new Thread(new Runnable() {
                    public void run() {
                        while (progressBarStatus < 100) {

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
                }).start();     */


        }
    }
 /*   ImageView imView;

    Bitmap bmImg;
    void downloadFile(String fileUrl){
        URL myFileUrl =null;
        try {
            myFileUrl= new URL(fileUrl);
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

        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }  */

    /** Show an event in the LogCat view, for debugging */
    private void dumpEvent(MotionEvent event) {
        String names[] = { "DOWN" , "UP" , "MOVE" , "CANCEL" , "OUTSIDE","POINTER_DOWN" , "POINTER_UP" , "7?" , "8?" , "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_" ).append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid " ).append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")" );
        }
        sb.append("[" );
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#" ).append(i);
            sb.append("(pid " ).append(event.getPointerId(i));
            sb.append(")=" ).append((int) event.getX(i));
            sb.append("," ).append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";" );
        }
        sb.append("]" );
        Log.d(TAG, sb.toString());
    }

}