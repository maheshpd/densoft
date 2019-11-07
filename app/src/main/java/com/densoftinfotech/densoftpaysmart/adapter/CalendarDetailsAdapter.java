package com.densoftinfotech.densoftpaysmart.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.R;
import com.densoftinfotech.densoftpaysmart.classes.CalendarDetails;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CalendarDetailsAdapter extends RecyclerView.Adapter<CalendarDetailsAdapter.MyViewHolder> {

    Context context;
    ArrayList<CalendarDetails> calendarDetails = new ArrayList<>();

    public CalendarDetailsAdapter(Context context, ArrayList<CalendarDetails> calendarDetails) {
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

        setdata(holder, position);

        if (calendarDetails.get(position).getStatus().equalsIgnoreCase("0")) {
            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.absent));
            holder.linear_absent.setVisibility(View.VISIBLE);
            holder.linear_intime.setVisibility(View.GONE);
            holder.linear_outtime.setVisibility(View.GONE);
            holder.linear_workinghours.setVisibility(View.GONE);
            holder.linear_lateby.setVisibility(View.GONE);
            holder.linear_holidayname.setVisibility(View.GONE);
            holder.linear_overtime.setVisibility(View.GONE);

        } else if (calendarDetails.get(position).getStatus().equalsIgnoreCase("1")) {
            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.present));
            holder.linear_absent.setVisibility(View.GONE);
            holder.linear_intime.setVisibility(View.VISIBLE);
            holder.linear_outtime.setVisibility(View.VISIBLE);
            holder.linear_workinghours.setVisibility(View.VISIBLE);
            holder.linear_lateby.setVisibility(View.GONE);
            holder.linear_holidayname.setVisibility(View.GONE);
            holder.linear_overtime.setVisibility(View.GONE);
        } else if (calendarDetails.get(position).getStatus().equalsIgnoreCase("2")) {
            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.lateby));
            holder.linear_absent.setVisibility(View.GONE);
            holder.linear_intime.setVisibility(View.VISIBLE);
            holder.linear_outtime.setVisibility(View.VISIBLE);
            holder.linear_workinghours.setVisibility(View.VISIBLE);
            holder.linear_lateby.setVisibility(View.VISIBLE);
            holder.linear_holidayname.setVisibility(View.GONE);
            holder.linear_overtime.setVisibility(View.GONE);
        } else if (calendarDetails.get(position).getStatus().equalsIgnoreCase("3")) {
            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.weekoff_holiday));
            holder.linear_absent.setVisibility(View.GONE);
            holder.linear_intime.setVisibility(View.GONE);
            holder.linear_outtime.setVisibility(View.GONE);
            holder.linear_workinghours.setVisibility(View.GONE);
            holder.linear_lateby.setVisibility(View.GONE);
            holder.linear_holidayname.setVisibility(View.VISIBLE);
            holder.linear_overtime.setVisibility(View.GONE);
        } else if (calendarDetails.get(position).getStatus().equalsIgnoreCase("4")) {
            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.workdone_onholiday));
            holder.linear_absent.setVisibility(View.GONE);
            holder.linear_intime.setVisibility(View.VISIBLE);
            holder.linear_outtime.setVisibility(View.VISIBLE);
            holder.linear_workinghours.setVisibility(View.GONE);
            holder.linear_lateby.setVisibility(View.GONE);
            holder.linear_holidayname.setVisibility(View.VISIBLE);
            holder.linear_overtime.setVisibility(View.GONE);
        } else if (calendarDetails.get(position).getStatus().equalsIgnoreCase("5")) {
            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.takenleave));
            holder.linear_absent.setVisibility(View.GONE);
            holder.linear_intime.setVisibility(View.GONE);
            holder.linear_outtime.setVisibility(View.GONE);
            holder.linear_workinghours.setVisibility(View.GONE);
            holder.linear_lateby.setVisibility(View.GONE);
            holder.linear_holidayname.setVisibility(View.VISIBLE);
            holder.linear_overtime.setVisibility(View.GONE);
        } else if (calendarDetails.get(position).getStatus().equalsIgnoreCase("6")) {
            holder.tv_status.setBackgroundColor(context.getResources().getColor(R.color.overtime));
            holder.linear_absent.setVisibility(View.GONE);
            holder.linear_intime.setVisibility(View.VISIBLE);
            holder.linear_outtime.setVisibility(View.VISIBLE);
            holder.linear_workinghours.setVisibility(View.VISIBLE);
            holder.linear_lateby.setVisibility(View.GONE);
            holder.linear_holidayname.setVisibility(View.GONE);
            holder.linear_overtime.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return calendarDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.linear_absent)
        LinearLayout linear_absent;
        @BindView(R.id.linear_intime)
        LinearLayout linear_intime;
        @BindView(R.id.linear_outtime)
        LinearLayout linear_outtime;
        @BindView(R.id.linear_workinghours)
        LinearLayout linear_workinghours;
        @BindView(R.id.linear_holidayname)
        LinearLayout linear_holidayname;
        @BindView(R.id.linear_overtime)
        LinearLayout linear_overtime;
        @BindView(R.id.linear_lateby)
        LinearLayout linear_lateby;

        TextView tv_absent, tv_intime, tv_outtime, tv_lateby, tv_working_hours, tv_holiday_name, tv_overtime, tv_status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            tv_absent = itemView.findViewById(R.id.tv_absent);
            tv_intime = itemView.findViewById(R.id.tv_intime);
            tv_outtime = itemView.findViewById(R.id.tv_outtime);
            tv_lateby = itemView.findViewById(R.id.tv_lateby);
            tv_working_hours = itemView.findViewById(R.id.tv_working_hours);
            tv_holiday_name = itemView.findViewById(R.id.tv_holiday_name);
            tv_overtime = itemView.findViewById(R.id.tv_overtime);
            tv_status = itemView.findViewById(R.id.tv_status);
        }
    }

    private void setdata(MyViewHolder holder, int position) {

        holder.tv_status.setText("" + (position + 1));
        holder.tv_intime.setText(": " + calendarDetails.get(position).getInTime());
        holder.tv_outtime.setText(": " + calendarDetails.get(position).getOutTime());
        holder.tv_lateby.setText(": " + calendarDetails.get(position).getLateBy());
        holder.tv_working_hours.setText(": " + calendarDetails.get(position).getWorkingHour());
        holder.tv_holiday_name.setText(": " + calendarDetails.get(position).getHoliDayName());
        holder.tv_overtime.setText(": " + calendarDetails.get(position).getOutTime());

    }
}


/*      0    Absent
        1    Present
        2    LateBy
        3    WeekOff Or Holiday
        4    Workdone on Weekoff Or Holiday
        5    Taken Leave
        6    Overtime*/
