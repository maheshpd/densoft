package com.densoftinfotech.densoftpaysmart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.MainActivity;
import com.densoftinfotech.densoftpaysmart.R;
import com.densoftinfotech.densoftpaysmart.TeamActivity;
import com.densoftinfotech.densoftpaysmart.model.QuickActions;
import com.densoftinfotech.densoftpaysmart.model.TeamList;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamListAdapter extends RecyclerView.Adapter<TeamListAdapter.MyViewHolder> {

    Context context;
    ArrayList<TeamList> teamLists = new ArrayList<>();

    public TeamListAdapter(Context context, ArrayList<TeamList> teamLists) {
        this.context = context;
        this.teamLists = teamLists;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.tv_name.setText(teamLists.get(i).getEmpName());

        if(teamLists.get(i).getFromDate().split("T").length > 0 && teamLists.get(i).getToDate().split("T").length > 0) {
            holder.tv_leavedate.setText("Leave from " + teamLists.get(i).getFromDate().split("T")[0] + " - " + teamLists.get(i).getToDate().split("T")[0]);
        }


        if(teamLists.get(i).getNoOfDays().equalsIgnoreCase("1.0") || teamLists.get(i).getNoOfDays().equalsIgnoreCase("1")) {
            holder.tv_days.setText("(" + teamLists.get(i).getLeaveName() + ", " + teamLists.get(i).getNoOfDays() + " day)");
        }else {
            holder.tv_days.setText("(" + teamLists.get(i).getLeaveName() + ", " + teamLists.get(i).getNoOfDays() + " days)");
        }

        holder.tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TeamActivity)context).gotoGetStatus(teamLists.get(i), "1", context.getResources().getString(R.string.accept));
                holder.tv_accept.setText(context.getResources().getString(R.string.accepted));
                holder.tv_accept.setEnabled(false);
                holder.tv_reject.setBackground(context.getResources().getDrawable(R.drawable.textview_rounded_status));
                holder.tv_reject.setTextColor(context.getResources().getColor(R.color.darkgray));
                holder.tv_reject.setEnabled(false);
                holder.tv_onhold.setBackground(context.getResources().getDrawable(R.drawable.textview_rounded_status));
                holder.tv_onhold.setTextColor(context.getResources().getColor(R.color.darkgray));
                holder.tv_onhold.setEnabled(false);
            }
        });

        holder.tv_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TeamActivity)context).gotoGetStatus(teamLists.get(i),"2", context.getResources().getString(R.string.reject));
                holder.tv_reject.setText(context.getResources().getString(R.string.rejected));
                holder.tv_reject.setEnabled(false);
                holder.tv_accept.setBackground(context.getResources().getDrawable(R.drawable.textview_rounded_status));
                holder.tv_accept.setTextColor(context.getResources().getColor(R.color.darkgray));
                holder.tv_accept.setEnabled(false);
                holder.tv_onhold.setBackground(context.getResources().getDrawable(R.drawable.textview_rounded_status));
                holder.tv_onhold.setTextColor(context.getResources().getColor(R.color.darkgray));
                holder.tv_onhold.setEnabled(false);
            }
        });
        holder.tv_onhold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TeamActivity)context).gotoGetStatus(teamLists.get(i),"3", context.getResources().getString(R.string.onhold));
                holder.tv_onhold.setText(context.getResources().getString(R.string.putonhold));
                holder.tv_onhold.setEnabled(false);
                holder.tv_reject.setBackground(context.getResources().getDrawable(R.drawable.textview_rounded_status));
                holder.tv_reject.setTextColor(context.getResources().getColor(R.color.darkgray));
                holder.tv_reject.setEnabled(false);
                holder.tv_accept.setBackground(context.getResources().getDrawable(R.drawable.textview_rounded_status));
                holder.tv_accept.setTextColor(context.getResources().getColor(R.color.darkgray));
                holder.tv_accept.setEnabled(false);
            }
        });

    }

    @Override
    public int getItemCount() {
        return teamLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_leavedate)
        TextView tv_leavedate;
        @BindView(R.id.tv_days)
        TextView tv_days;
        @BindView(R.id.tv_accept)
        TextView tv_accept;
        @BindView(R.id.tv_reject)
        TextView tv_reject;
        @BindView(R.id.tv_onhold)
        TextView tv_onhold;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
