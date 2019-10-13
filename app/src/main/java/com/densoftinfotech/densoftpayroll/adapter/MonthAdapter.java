package com.densoftinfotech.densoftpayroll.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.densoftinfotech.densoftpayroll.R;
import com.densoftinfotech.densoftpayroll.SalarySlipDetailsActivity;
import com.densoftinfotech.densoftpayroll.classes.MonthDisplay;

import java.time.format.TextStyle;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MyViewHolder> {

    Context context;
    ArrayList<MonthDisplay> monthDisplays = new ArrayList<>();
    int selected = 0;
    int pos = 0;

    public MonthAdapter(Context context, ArrayList<MonthDisplay> monthDisplays, int pos) {
        this.context = context;
        this.monthDisplays = monthDisplays;
        this.pos = pos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.month_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        holder.tv_month.setSelected(i == selected);

        if (holder.tv_month.isSelected()) {
            holder.tv_month.setTextColor(context.getResources().getColor(R.color.white));
            holder.tv_month.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            ((SalarySlipDetailsActivity)context).gotoselection(i);
        } else {
            holder.tv_month.setTextColor(context.getResources().getColor(R.color.gray));
        }



        /*holder.tv_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((SalarySlipDetailsActivity)context).gotoselection(i);
            }
        });*/

        holder.tv_month.setText(monthDisplays.get(i).getName_of_month());
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
