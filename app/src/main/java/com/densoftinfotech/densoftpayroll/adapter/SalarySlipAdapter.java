package com.densoftinfotech.densoftpayroll.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.densoftinfotech.densoftpayroll.R;
import com.densoftinfotech.densoftpayroll.classes.SalarySlip;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SalarySlipAdapter extends RecyclerView.Adapter<SalarySlipAdapter.MyViewHolder> {

    Context context;
    ArrayList<SalarySlip> salarySlipDemos = new ArrayList<>();

    public SalarySlipAdapter(Context context, ArrayList<SalarySlip> salarySlipDemos) {
        this.context = context;
        this.salarySlipDemos = salarySlipDemos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.salaryslip_mainlayout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.tv_takehome.setText("₹ " + salarySlipDemos.get(i).getTake_home());
        holder.tv_deductions.setText("₹ " + salarySlipDemos.get(i).getDeduction());
        holder.tv_grosspay.setText(context.getResources().getString(R.string.totalgrosspay) + ": ₹ " + ((salarySlipDemos.get(i).getTake_home() + salarySlipDemos.get(i).getDeduction())));

        holder.tv_payfor.setText(context.getResources().getString(R.string.payfor) + " " + salarySlipDemos.get(i).getMonth());
        holder.tv_paiddays.setText(context.getResources().getString(R.string.paiddays) + " " + salarySlipDemos.get(i).getDays_of_month());
    }

    @Override
    public int getItemCount() {
        return salarySlipDemos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_takehome)
        TextView tv_takehome;
        @BindView(R.id.tv_deductions)
        TextView tv_deductions;
        @BindView(R.id.tv_grosspay)
        TextView tv_grosspay;
        @BindView(R.id.tv_payfor)
        TextView tv_payfor;
        @BindView(R.id.tv_paiddays)
        TextView tv_paiddays;
        @BindView(R.id.tv_view)
        TextView tv_view;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
