package com.densoftinfotech.densoftpaysmart.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.R;
import com.densoftinfotech.densoftpaysmart.SalarySlipDetailsActivity;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;

import java.util.ArrayList;
import java.util.TreeSet;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MyViewHolder> {

    Context context;
    ArrayList<Integer> monthDisplays = new ArrayList<>();
    int selected = 0;
    int monthpos;

    public MonthAdapter(Context context, TreeSet<Integer> monthDisplays, int monthpos) {
        this.context = context;
        this.monthDisplays = new ArrayList<Integer>(monthDisplays);
        this.monthpos = monthpos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

       if(monthpos == -1){
           holder.tv_month.setSelected(i == selected);
       }else {
           if (monthDisplays.get(i) == monthpos) {
               holder.tv_month.setSelected(true);
                monthpos = -1;
           }
       }


        if (holder.tv_month.isSelected()) {
            holder.tv_month.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv_month.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            ((SalarySlipDetailsActivity) context).gotoselection(String.valueOf(monthDisplays.get(i)));
        } else {
            holder.tv_month.setTextColor(context.getResources().getColor(R.color.gray));
            holder.tv_month.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }


        holder.tv_month.setText(((CommonActivity) context).get_monthName(monthDisplays.get(i)));
    }


    @Override
    public int getItemCount() {
        return monthDisplays.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_month)
        TextView tv_month;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            tv_month.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selected = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
