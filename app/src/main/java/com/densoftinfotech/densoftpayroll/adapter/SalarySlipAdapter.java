package com.densoftinfotech.densoftpayroll.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.densoftinfotech.densoftpayroll.R;
import com.densoftinfotech.densoftpayroll.SalarySlipDetailsActivity;
import com.densoftinfotech.densoftpayroll.classes.SalarySlip;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SalarySlipAdapter extends RecyclerView.Adapter<SalarySlipAdapter.MyViewHolder> {

    Context context;
    ArrayList<SalarySlip> salarySlipDemos = new ArrayList<>();
    public static int[] background_gradient = {R.drawable.gradient_2, R.drawable.gradient_12, R.drawable.gradient_4,
            R.drawable.gradient_11, R.drawable.gradient_5, R.drawable.gradient_6 , R.drawable.gradient_3, R.drawable.gradient_7,
            R.drawable.gradient_8, R.drawable.gradient_9, R.drawable.gradient_10, R.drawable.gradient_13};

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


        setdata(holder, i);

        holder.linearlayout_salaryslip.setBackgroundResource(background_gradient[i]);

        holder.tv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SalarySlipDetailsActivity.class);
                intent.putExtra("pos", i);
                context.startActivity(intent);
            }
        });
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
        @BindView(R.id.linearlayout_salaryslip)
        LinearLayout linearlayout_salaryslip;
        @BindView(R.id.piechart)
        PieChart piechart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }

    public void setdata(MyViewHolder holder, int i){
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry((float)salarySlipDemos.get(i).getTake_home(), "Take Home"));
        entries.add(new PieEntry((float)salarySlipDemos.get(i).getDeduction(), "Deductions"));

        PieDataSet set = new PieDataSet(entries, "Salary Slip");
        PieData data = new PieData(set);
        holder.piechart.setData(data);
        data.setValueTextColor(ContextCompat.getColor(context,R.color.black));
        data.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        //set.setColors(ColorTemplate.COLORFUL_COLORS);
        set.setColors(new int[] { R.color.bluepie, R.color.purplepie}, context);
        holder.piechart.animateXY(5000, 5000);
    }
}
