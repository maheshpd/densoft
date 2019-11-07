package com.densoftinfotech.densoftpaysmart.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.LeaveApplicationActivity;
import com.densoftinfotech.densoftpaysmart.R;
import com.densoftinfotech.densoftpaysmart.classes.LeaveDetails;

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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {

        holder.tv_name_of_leave.setText(leaveDetails.get(i).getName() + " - " + leaveDetails.get(i).getDescription());

        holder.tv_balance_leave.setText("" + leaveDetails.get(i).getBalanceLeave());
        holder.tv_takenleave.setText("" + leaveDetails.get(i).getLeaveTaken());
        holder.tv_totalassigned.setText("" + leaveDetails.get(i).getLeaveAssign());



        holder.tv_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1 = new Intent(context, LeaveApplicationActivity.class);
                i1.putExtra("leaves", leaveDetails.get(i));
                context.startActivity(i1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return leaveDetails.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_balance_leave)
        TextView tv_balance_leave;
        @BindView(R.id.tv_takenleave)
        TextView tv_takenleave;
        @BindView(R.id.tv_totalassigned)
        TextView tv_totalassigned;
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
