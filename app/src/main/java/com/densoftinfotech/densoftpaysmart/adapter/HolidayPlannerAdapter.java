package com.densoftinfotech.densoftpaysmart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.R;
import com.densoftinfotech.densoftpaysmart.model.CalendarDetails;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HolidayPlannerAdapter extends RecyclerView.Adapter<HolidayPlannerAdapter.MyViewHolder> {

    Context context;
    ArrayList<CalendarDetails> calendarDetails = new ArrayList<>();

    public HolidayPlannerAdapter(Context context, ArrayList<CalendarDetails> calendarDetails) {
        this.context = context;
        this.calendarDetails = calendarDetails;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_details_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.linearlayout_one.setVisibility(View.GONE);

        if (calendarDetails.get(position).getStatus().equalsIgnoreCase("3") && !calendarDetails.get(position).getWeekOff().equalsIgnoreCase("Saturday")
                && !calendarDetails.get(position).getWeekOff().equalsIgnoreCase("Sunday")) {
            holder.linearlayout_two.setVisibility(View.VISIBLE);
            holder.tv_date.setText(calendarDetails.get(position).getCDate());
            holder.tv_holidayname.setText(calendarDetails.get(position).getHoliDayName());
        } else {
            holder.linearlayout_two.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return calendarDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.linearlayout_one)
        LinearLayout linearlayout_one;
        @BindView(R.id.linearlayout_two)
        LinearLayout linearlayout_two;
        @BindView(R.id.tv_date)
        TextView tv_date;
        @BindView(R.id.tv_holidayname)
        TextView tv_holidayname;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}


/*      0    Absent
        1    Present
        2    LateBy
        3    WeekOff Or Holiday
        4    Workdone on Weekoff Or Holiday
        5    Taken Leave
        6    Overtime*/
