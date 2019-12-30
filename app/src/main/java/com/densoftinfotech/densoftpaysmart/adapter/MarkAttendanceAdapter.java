package com.densoftinfotech.densoftpaysmart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.R;
import com.densoftinfotech.densoftpaysmart.model.MarkAttendanceDetails;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MarkAttendanceAdapter extends RecyclerView.Adapter<MarkAttendanceAdapter.MyViewHolder> {

    Context context;
    ArrayList<MarkAttendanceDetails> markAttendanceDetails = new ArrayList<>();

    public MarkAttendanceAdapter(Context context, ArrayList<MarkAttendanceDetails> markAttendanceDetails) {
        this.context = context;
        this.markAttendanceDetails = markAttendanceDetails;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mark_attendance_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.tv_thumbdate.setText(markAttendanceDetails.get(i).getThumbDate().split("T")[0]);
        holder.tv_thumbtime.setText(markAttendanceDetails.get(i).getThumbTime());
    }

    @Override
    public int getItemCount() {
        return markAttendanceDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_thumbdate)
        TextView tv_thumbdate;
        @BindView(R.id.tv_thumbtime)
        TextView tv_thumbtime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
