package com.densoftinfotech.densoftpayroll.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.densoftinfotech.densoftpayroll.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CalendarGridAdapter extends ArrayAdapter {
    private static final String TAG = "GridAdapter";
    private LayoutInflater mInflater;
    private List<Date> monthlyDates;
    private Calendar currentDate;
    Context context;
    ArrayList<String> status = new ArrayList<>();
    int i = 0;

    public CalendarGridAdapter(Context context, List<Date> monthlyDates, Calendar currentDate, ArrayList<String> status) {
        super(context, R.layout.single_cell_layout);
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.context = context;
        this.status = status;

        mInflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Date mDate = monthlyDates.get(position);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);
        int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCal.get(Calendar.MONTH) + 1;
        int displayYear = dateCal.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);
        View view = convertView;

        if(view == null){
            view = mInflater.inflate(R.layout.single_cell_layout, parent, false);

        }

        TextView cellNumber = view.findViewById(R.id.calendar_date_id);
        if(displayMonth == currentMonth && displayYear == currentYear){
            //view.setBackgroundColor(context.getResources().getColor(R.color.colorWhite))

            Log.d("here i is ", i + " position is " + position);
            if(i<status.size()) {
                cellNumber.setTextColor(context.getResources().getColor(R.color.white));
                setstatus_color(cellNumber, status.get(i));
                i++;
            }



        }else{
            //view.setBackgroundResource(R.drawable.textview_rounded_holiday);
            cellNumber.setTextColor(context.getResources().getColor(R.color.black));
        }
        //Add day to calendar

        cellNumber.setText(String.valueOf(dayValue));
        /*Add events to the calendar
        TextView eventIndicator = (TextView)view.findViewById(R.id.event_id);
        Calendar eventCalendar = Calendar.getInstance();*/



        return view;
    }

    private void setstatus_color(TextView cellNumber, String s) {
        if(s.equalsIgnoreCase("Absent")){
            cellNumber.setBackgroundResource(R.drawable.textview_rounded_absent);
        }else if(s.equalsIgnoreCase("Present")){
            cellNumber.setBackgroundResource(R.drawable.textview_rounded_present);
        }else if(s.equalsIgnoreCase("Bunk")){
            cellNumber.setBackgroundResource(R.drawable.textview_rounded_bunk);
        }else if(s.equalsIgnoreCase("Leave")){
            cellNumber.setBackgroundResource(R.drawable.textview_rounded_leave);
        }else if(s.equalsIgnoreCase("Late")){
            cellNumber.setBackgroundResource(R.drawable.textview_rounded_late);
        }else if(s.equalsIgnoreCase("OnTask")){
            cellNumber.setBackgroundResource(R.drawable.textview_rounded_ontask);
        }else{
            cellNumber.setBackgroundResource(R.drawable.textview_rounded_holiday);
            cellNumber.setTextColor(context.getResources().getColor(R.color.black));
        }
    }

    @Override
    public int getCount() {
        return monthlyDates.size();
    }
    @Nullable
    @Override
    public Object getItem(int position) {
        return monthlyDates.get(position);
    }
    @Override
    public int getPosition(Object item) {
        return monthlyDates.indexOf(item);
    }
}