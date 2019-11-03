package com.densoftinfotech.densoftpaysmart.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.densoftinfotech.densoftpaysmart.R;
import com.densoftinfotech.densoftpaysmart.classes.SalarySlip;
import com.densoftinfotech.densoftpaysmart.classes.SalarySlipDistinct;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SalaryDistinctAdapter extends RecyclerView.Adapter<SalaryDistinctAdapter.MyViewHolder> {

    ArrayList<SalarySlipDistinct> salarySlipDistincts = new ArrayList<>();
    ArrayList<SalarySlipDistinct> salarySlipDistincts_response = new ArrayList<>();
    ArrayList<SalarySlip> salarySlips = new ArrayList<>();
    Context context;
    SalarySlipAdapter salarySlipAdapter;
    RecyclerView.LayoutManager layoutManager;

    public SalaryDistinctAdapter(Context context, ArrayList<SalarySlipDistinct> salarySlipDistincts_response, ArrayList<SalarySlipDistinct> salarySlipDistincts){
        this.context = context;
        this.salarySlipDistincts = salarySlipDistincts;
        this.salarySlipDistincts_response = salarySlipDistincts_response;
    }


    @NonNull
    @Override
    public SalaryDistinctAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.salary_distinct_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SalaryDistinctAdapter.MyViewHolder holder, int i) {

        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        holder.recycler_view_salaryslip.setLayoutManager(layoutManager);

        for(int j = 0; j<salarySlipDistincts.size(); j++){
            if (salarySlipDistincts_response.get(i).getApplyForMonth() == salarySlipDistincts.get(j).getApplyForMonth()) {
                Log.d("distinct elements are ", salarySlipDistincts_response.get(j).getApplyForMonth() + "");
                salarySlips.add(new SalarySlip(salarySlipDistincts_response.get(j).getName(),
                        salarySlipDistincts_response.get(j).getAmount(), salarySlipDistincts_response.get(j).getApplyForMonth(),
                        salarySlipDistincts_response.get(j).getApplyForYear()));

            }
        }

        salarySlipAdapter = new SalarySlipAdapter(context, salarySlips);
        holder.recycler_view_salaryslip.setAdapter(salarySlipAdapter);
    }

    @Override
    public int getItemCount() {
        return salarySlipDistincts.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        RecyclerView recycler_view_salaryslip;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recycler_view_salaryslip = itemView.findViewById(R.id.recycler_view_salaryslip);
        }
    }
}
