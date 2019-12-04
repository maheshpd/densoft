package com.densoftinfotech.densoftpaysmart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.R;
import com.densoftinfotech.densoftpaysmart.classes.LeaveAppliedDetails;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LeaveAppliedDetailsAdapter extends RecyclerView.Adapter<LeaveAppliedDetailsAdapter.MyViewHolder> {

    Context context;
    ArrayList<LeaveAppliedDetails> leaveAppliedDetailsList = new ArrayList<>();

    public LeaveAppliedDetailsAdapter(Context context, ArrayList<LeaveAppliedDetails> leaveAppliedDetailsList) {
        this.context = context;
        this.leaveAppliedDetailsList = leaveAppliedDetailsList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaveapplied_details_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {

        holder.tv_name_of_leave.setText(leaveAppliedDetailsList.get(i).getName());

        holder.tv_balance_leave.setText("" + leaveAppliedDetailsList.get(i).getFromDate().split("T")[0]);
        holder.tv_takenleave.setText("" + leaveAppliedDetailsList.get(i).getToDate().split("T")[0]);
        holder.tv_totalassigned.setText("" + leaveAppliedDetailsList.get(i).getNoOfDays());

        if(leaveAppliedDetailsList.get(i).getStatus().equals("1")){
            holder.tv_status.setText("Accepted");
            //holder.iv_status.setBackgroundResource(R.drawable.accept);
        }else if(leaveAppliedDetailsList.get(i).getStatus().equals("2")){
            holder.tv_status.setText("Rejected");
            //holder.iv_status.setBackgroundResource(R.drawable.reject);
        }else if(leaveAppliedDetailsList.get(i).getStatus().equals("3")){
            holder.tv_status.setText("OnHold");
        }else{
            holder.tv_status.setText("Waiting");
        }
    }

    @Override
    public int getItemCount() {
        return leaveAppliedDetailsList.size();
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
        @BindView(R.id.iv_status)
        ImageView iv_status;
        @BindView(R.id.tv_status)
        TextView tv_status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
