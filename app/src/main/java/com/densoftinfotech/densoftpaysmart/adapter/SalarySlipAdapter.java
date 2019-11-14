package com.densoftinfotech.densoftpaysmart.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.R;
import com.densoftinfotech.densoftpaysmart.SalarySlipDetailsActivity;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.classes.SalarySlip;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class SalarySlipAdapter extends RecyclerView.Adapter<SalarySlipAdapter.MyViewHolder> {

    Context context;
    ArrayList<Integer> salarySlips = new ArrayList<>();
    ArrayList<SalarySlip> response_salarySlips = new ArrayList<>();

    public static int[] background_gradient = {R.drawable.gradient_2, R.drawable.gradient_12, R.drawable.gradient_4,
            R.drawable.gradient_11, R.drawable.gradient_5, R.drawable.gradient_6 , R.drawable.gradient_3, R.drawable.gradient_7,
            R.drawable.gradient_8, R.drawable.gradient_9, R.drawable.gradient_10, R.drawable.gradient_13,
            R.drawable.gradient_2, R.drawable.gradient_12, R.drawable.gradient_4,
            R.drawable.gradient_11, R.drawable.gradient_5, R.drawable.gradient_6 , R.drawable.gradient_3, R.drawable.gradient_7,
            R.drawable.gradient_8, R.drawable.gradient_9, R.drawable.gradient_10, (R.drawable.gradient_13)};

    public SalarySlipAdapter(Context context, TreeSet<Integer> salarySlips, ArrayList<SalarySlip> response_salarySlips) {
        this.context = context;
        this.salarySlips = new ArrayList<Integer>(salarySlips);
        this.response_salarySlips = response_salarySlips;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

           v  = LayoutInflater.from(parent.getContext()).inflate(R.layout.salaryslip_mainlayout, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            setmainlayout(holder, position);



    }

    private void setmainlayout(MyViewHolder holder, int i) {

        holder.linearlayout_salaryslip.setBackgroundResource(background_gradient[i]);

        setdata(holder, response_salarySlips, salarySlips, i);

        holder.tv_payfor.setText(context.getResources().getString(R.string.payfor) + " " + ((CommonActivity)context).get_monthName(salarySlips.get(i)) + " " + Calendar.getInstance().get(Calendar.YEAR));
        for(int j = 0; j<response_salarySlips.size(); j++){

            if(response_salarySlips.get(j).getApplyForMonth() == salarySlips.get(i)) {

                if (response_salarySlips.get(j).getName().equals("Gross Salary"))
                    holder.tv_grosspay.setText(response_salarySlips.get(j).getName() + ": " + context.getResources().getString(R.string.rs) + " " + response_salarySlips.get(j).getAmount());

                if (response_salarySlips.get(j).getName().equals("Net Salary")) {
                    holder.tv_name1.setText(response_salarySlips.get(j).getName());
                    holder.tv_name1_value.setText(context.getResources().getString(R.string.rs) + " " + response_salarySlips.get(j).getAmount());
                }

                if (response_salarySlips.get(j).getName().equals("Total Deduction")) {
                    holder.tv_name2.setText(response_salarySlips.get(j).getName());
                    holder.tv_name2_value.setText(context.getResources().getString(R.string.rs) + " " + response_salarySlips.get(j).getAmount());
                }

                if(response_salarySlips.get(j).getName().equals("Payable Days")){
                    holder.tv_paiddays.setText(context.getResources().getString(R.string.paiddays) + " " + response_salarySlips.get(j).getAmount());
                }


            }
        }

        holder.tv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SalarySlipDetailsActivity.class);
                //intent.putExtra("month", salarySlips.get(i));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return salarySlips.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name1, tv_name1_value, tv_name2, tv_name2_value, tv_grosspay, tv_payfor, tv_paiddays,tv_view;
        LinearLayout linearlayout_salaryslip;
        PieChart piechart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


                tv_name1 = itemView.findViewById(R.id.tv_name1);
                tv_name1_value = itemView.findViewById(R.id.tv_name1_value);
                tv_name2 = itemView.findViewById(R.id.tv_name2);
                tv_name2_value = itemView.findViewById(R.id.tv_name2_value);
                tv_grosspay = itemView.findViewById(R.id.tv_grosspay);
                tv_payfor = itemView.findViewById(R.id.tv_payfor);
                tv_paiddays = itemView.findViewById(R.id.tv_paiddays);
                tv_view = itemView.findViewById(R.id.tv_view);
                linearlayout_salaryslip = itemView.findViewById(R.id.linearlayout_salaryslip);
                piechart = itemView.findViewById(R.id.piechart);


        }
    }

    private void setdata(MyViewHolder holder, ArrayList<SalarySlip> response_salarySlips, ArrayList<Integer> salarySlips, int i){
        List<PieEntry> entries = new ArrayList<>();
        for(int j = 0; j<response_salarySlips.size(); j++){

            if(response_salarySlips.get(j).getApplyForMonth() == salarySlips.get(i)) {

                if (response_salarySlips.get(j).getName().equals("Total Deduction")) {
                    entries.add(new PieEntry((float) response_salarySlips.get(j).getAmount(), response_salarySlips.get(j).getName()));
                }
                if (response_salarySlips.get(j).getName().equals("Net Salary")) {
                    entries.add(new PieEntry((float) response_salarySlips.get(j).getAmount(), response_salarySlips.get(j).getName()));
                }
            }
        }

        holder.piechart.getDescription().setEnabled(false);
        PieDataSet set = new PieDataSet(entries, "");
        PieData data = new PieData(set);
        holder.piechart.setData(data);
        data.setValueTextColor(ContextCompat.getColor(context,R.color.black));
        data.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        //set.setColors(ColorTemplate.COLORFUL_COLORS);
        set.setColors(new int[] { R.color.bluepie, R.color.purplepie}, context);
        holder.piechart.animateXY(5000, 5000);
    }

}
