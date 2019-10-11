package com.densoftinfotech.densoftpayroll.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.densoftinfotech.densoftpayroll.PlannerActivity;
import com.densoftinfotech.densoftpayroll.R;
import com.densoftinfotech.densoftpayroll.classes.LeaveDetails;
import com.densoftinfotech.densoftpayroll.classes.QuickActions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.MyViewHolder> {

    Context context;
    ArrayList<LeaveDetails> leaveDetails = new ArrayList<>();

    public LeaveAdapter(Context context, ArrayList<LeaveDetails> leaveDetails) {
        this.context = context;
        this.leaveDetails = leaveDetails;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.tv_days.setText("" + leaveDetails.get(i).getDays());
        holder.tv_name_of_leave.setText(leaveDetails.get(i).getName_of_leave());


        holder.tv_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return leaveDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_days)
        TextView tv_days;
        @BindView(R.id.tv_name_of_leave)
        TextView tv_name_of_leave;
        @BindView(R.id.tv_apply)
        TextView tv_apply;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
