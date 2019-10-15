package com.densoftinfotech.densoftpayroll.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.densoftinfotech.densoftpayroll.MainActivity;
import com.densoftinfotech.densoftpayroll.PlannerActivity;
import com.densoftinfotech.densoftpayroll.R;
import com.densoftinfotech.densoftpayroll.classes.QuickActions;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class QuickActionsAdapter extends RecyclerView.Adapter<QuickActionsAdapter.MyViewHolder> {

    Context context;
    ArrayList<QuickActions> quickActions = new ArrayList<>();

    public QuickActionsAdapter(Context context, ArrayList<QuickActions> quickActions) {
        this.context = context;
        this.quickActions = quickActions;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.quickactions_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.tv_name.setText(quickActions.get(i).getName());
        holder.iv_icon.setImageResource(quickActions.get(i).getImage());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((MainActivity)context).gotoactivity(quickActions.get(i).getName());

            }
        });
    }

    @Override
    public int getItemCount() {
        return quickActions.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.iv_icon)
        ImageView iv_icon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
