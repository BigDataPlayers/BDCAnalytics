package com.bdc.android.helloandroid;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Calendar;


public class AppointmentCalendar extends Activity implements CalendarView.OnDateChangeListener {
    private Calendar cal = Calendar.getInstance();
    private int displayedMonth = cal.get(Calendar.MONTH); //current month;
    private int displayedYear = cal.get(Calendar.YEAR); //current year;

    CalendarView calView;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        calView = (CalendarView) findViewById(R.id.calendarView);
        calView.setOnDateChangeListener(this);
        calView.setShowWeekNumber(false);
//        new UpdateMonth(-1).onClick(calView);
        new UpdateMonth(1).onClick(calView);
        calView.setDate(Calendar.getInstance().getTimeInMillis());

        try {
            Class<?> cvClass = calView.getClass();
            Field field = cvClass.getDeclaredField("mMonthName");
            field.setAccessible(true);
            TextView tv = (TextView) field.get(calView);
//            tv.setTextColor(Color.rgb(1,182,92));
            tv.setTextColor(Color.RED);
            tv.setBackgroundColor(Color.YELLOW);
//            Field list = cvClass.getDeclaredField("mDayLabels");
//            list.setAccessible(true);
//            String[] lables = (String[]) list.get(calView);
//            System.out.println("Date Labels [" + lables.length + "]:" );
//            System.out.println("Date Labels [" + Arrays.asList(lables) + "]:" );

//            field = cvClass.getDeclaredField("mListView");
//            field.setAccessible(true);
            ListView lv = (ListView) field.get(calView);
            lv.setBackgroundColor(Color.RED);
            int count = lv.getChildCount();
            System.out.println("Date Labels count [" + count + "]:" );
            for (int i=0; i < count; i++){
//            if (count >=5) lv.getChildAt(5).setBackgroundColor(Color.GREEN) ;
                System.out.println(" view at count [" + count + "]:" + lv.getChildAt(count) );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Button nextButton = (Button) findViewById(R.id.buttonNext);
        Button prevButton = (Button) findViewById(R.id.buttonPrev);
        Button todayButton = (Button) findViewById(R.id.buttonToday);

        nextButton.setOnClickListener(new UpdateMonth(1));
        prevButton.setOnClickListener(new UpdateMonth(-1));
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calView.setDate(Calendar.getInstance().getTimeInMillis());
            }
        });


        //set the first day of month in focus and call OnSelectedDayChange()


    }

    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayofMonth) {
        System.out.println("Selected Year:" + year + " Month:" + month + " Day:" + dayofMonth);
        Intent gotoNextActivity = new Intent(getApplicationContext(), AppointmentSlotMap.class);
        startActivity(gotoNextActivity);

        //call server to get the opening slots

        //update the display with available slots

    }

    private class UpdateMonth implements View.OnClickListener {
        int monthIncr;

        private UpdateMonth(int incr) {
            monthIncr = incr;
        }

        @Override
        public void onClick(View view) {
            displayedMonth += monthIncr;

            if (displayedMonth < 0) {
                displayedMonth += 12;
                displayedYear -= 1;
            }

            if (displayedMonth > 11) {
                displayedMonth -= 12;
                displayedYear += 1;
            }

            cal.set(displayedYear, displayedMonth, 1);
            calView.setDate(cal.getTimeInMillis());
        }
    }

}