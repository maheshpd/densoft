package com.densoftinfotech.densoftpayroll.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.densoftinfotech.densoftpayroll.R;
import com.densoftinfotech.densoftpayroll.classes.CalendarDetails;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.tv_status.setText(calendarDetails.get(position).getStatus());
        holder.tv_description.setText(calendarDetails.get(position).getDescription());

        if (calendarDetails.get(position).getStatus().equalsIgnoreCase("Present")) {
            holder.iv_status.setBackgroundColor(context.getResources().getColor(R.color.present));
        } else if (calendarDetails.get(position).getStatus().equalsIgnoreCase("Absent")) {
            holder.iv_status.setBackgroundColor(context.getResources().getColor(R.color.absent));
        } else if (calendarDetails.get(position).getStatus().equalsIgnoreCase("Bunk")) {
            holder.iv_status.setBackgroundColor(context.getResources().getColor(R.color.bunk));
        } else if (calendarDetails.get(position).getStatus().equalsIgnoreCase("Leave")) {
            holder.iv_status.setBackgroundColor(context.getResources().getColor(R.color.leave));
        } else if (calendarDetails.get(position).getStatus().equalsIgnoreCase("Late")) {
            holder.iv_status.setBackgroundColor(context.getResources().getColor(R.color.late));
        } else if (calendarDetails.get(position).getStatus().equalsIgnoreCase("OnTask")) {
            holder.iv_status.setBackgroundColor(context.getResources().getColor(R.color.ontask));
        }
    }

    @Override
    public int getItemCount() {
        return calendarDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_status, tv_description;
        ImageView iv_status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_status = itemView.findViewById(R.id.tv_status);
            iv_status = itemView.findViewById(R.id.iv_status);
            tv_description = itemView.findViewById(R.id.tv_description);
        }
    }
}
