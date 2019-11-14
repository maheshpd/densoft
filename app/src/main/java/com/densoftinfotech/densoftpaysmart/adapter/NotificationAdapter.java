package com.densoftinfotech.densoftpaysmart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.R;
import com.densoftinfotech.densoftpaysmart.classes.NotificationReceived;
import com.densoftinfotech.densoftpaysmart.sqlitedatabase.DatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    Context context;
    List<NotificationReceived> notificationList = new ArrayList<>();

    public NotificationAdapter(Context context, List<NotificationReceived> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int i) {

        if(!notificationList.get(i).getTitle().trim().equals("")) {
            holder.tv_title.setText(notificationList.get(i).getTitle());
        }
        if(!notificationList.get(i).getDescription().trim().equals("")) {
            holder.tv_description.setText(notificationList.get(i).getDescription());
        }

        if(!notificationList.get(i).getBig_picture().trim().equals("")) {
            Picasso.with(context)
                    .load(notificationList.get(i).getBig_picture())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.iv_bigimage);
        }

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper.getInstance(context).deletebyid(notificationList.get(i).getId());
                notificationList.remove(notificationList.get(i));
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_bigimage)
        ImageView iv_bigimage;
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.tv_description)
        TextView tv_description;

        @BindView(R.id.iv_delete)
        ImageView iv_delete;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
