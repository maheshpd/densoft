package com.densoftinfotech.densoftpaysmart.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.R;
import com.densoftinfotech.densoftpaysmart.TeamActivity;
import com.densoftinfotech.densoftpaysmart.app_utilities.Constants;
import com.densoftinfotech.densoftpaysmart.app_utilities.DateUtils;
import com.densoftinfotech.densoftpaysmart.model.FirebaseLiveLocation;
import com.densoftinfotech.densoftpaysmart.model.FirebaseLiveLocationDiffCallback;
import com.densoftinfotech.densoftpaysmart.model.TeamList;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.security.CryptoPrimitive;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GoogleMapAdapter extends RecyclerView.Adapter<GoogleMapAdapter.MyViewHolder> {

    Context context;
    ArrayList<FirebaseLiveLocation> firebaseLiveLocations = new ArrayList<>();

    public GoogleMapAdapter(Context context, ArrayList<FirebaseLiveLocation> firebaseLiveLocations) {
        this.context = context;
        this.firebaseLiveLocations = firebaseLiveLocations;
        LocalBroadcastManager.getInstance(context).registerReceiver(rec, new IntentFilter("estimated"));
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.googlemap_list_layout, parent, false);
        return new MyViewHolder(v);
    }

    /*@Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i, @NonNull List<Object> payloads) {
        if(payloads.isEmpty()){
            super.onBindViewHolder(holder, i, payloads);
        }else{
            if(!firebaseLiveLocations.get(i).getPhoto_url().trim().equals("")) {
                Picasso.get()
                        .load(firebaseLiveLocations.get(i).getPhoto_url())
                        .error(R.mipmap.ic_launcher)
                        .into(holder.iv_photo);
            }

            holder.tv_name.setText(firebaseLiveLocations.get(i).getStaff_name() + " (Staff id: " + firebaseLiveLocations.get(i).getStaff_id() + ")");
            holder.tv_address.setText(firebaseLiveLocations.get(i).getAddress());

            if(!firebaseLiveLocations.get(i).getEstimated_distance().trim().equals("")){
                holder.tv_estimated_distance.setVisibility(View.VISIBLE);
                holder.tv_estimated_distance.setText(firebaseLiveLocations.get(i).getEstimated_distance());
            }else{
                holder.tv_estimated_distance.setVisibility(View.GONE);
            }

            holder.tv_estimated_time.setText("");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    firebaseLiveLocations.get(i).setEstimated_distance(String.valueOf(DateUtils.distance(Constants.current_loc_latitude, Constants.current_loc_longitude, firebaseLiveLocations.get(i).getLatitude(),
                            firebaseLiveLocations.get(i).getLongitude(), 'K')));
                    notifyItemChanged(i);

                    Intent i1 = new Intent("notifymap");

                    i1.putExtra("lat", firebaseLiveLocations.get(i).getLatitude());
                    i1.putExtra("long", firebaseLiveLocations.get(i).getLongitude());
                    i1.putExtra("mode", firebaseLiveLocations.get(i).getTransport_mode());
                    i1.putExtra("staffid", firebaseLiveLocations.get(i).getStaff_id());
                    i1.putExtra("pos", i);
                    i1.putExtra("querystring", firebaseLiveLocations.get(i).getStaff_name() + " (Staff id: " + firebaseLiveLocations.get(i).getStaff_id() + ")");
                    LocalBroadcastManager.getInstance(context).sendBroadcast(i1);
                }
            });

        }

    }*/

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        if(!firebaseLiveLocations.get(i).getPhoto_url().trim().equals("")) {
            Picasso.get()
                    .load(firebaseLiveLocations.get(i).getPhoto_url())
                    .error(R.mipmap.ic_launcher)
                    .into(holder.iv_photo);
        }

        holder.tv_name.setText(firebaseLiveLocations.get(i).getStaff_name() + " (Staff id: " + firebaseLiveLocations.get(i).getStaff_id() + ")");
        holder.tv_address.setText(firebaseLiveLocations.get(i).getAddress());

        /*if(!firebaseLiveLocations.get(i).getEstimated_distance().trim().equals("")){
            holder.tv_estimated_distance.setVisibility(View.VISIBLE);
            holder.tv_estimated_distance.setText(firebaseLiveLocations.get(i).getEstimated_distance());
        }else{
            holder.tv_estimated_distance.setVisibility(View.GONE);
        }

        holder.tv_estimated_time.setText("");*/

        holder.tv_estimated_distance.setText(String.format("Estimated distance: %.2f  kms",DateUtils.distance(Constants.current_loc_latitude, Constants.current_loc_longitude, firebaseLiveLocations.get(i).getLatitude(),
                firebaseLiveLocations.get(i).getLongitude(), 'K')));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*firebaseLiveLocations.get(i).setEstimated_distance(String.valueOf(DateUtils.distance(Constants.current_loc_latitude, Constants.current_loc_longitude, firebaseLiveLocations.get(i).getLatitude(),
                        firebaseLiveLocations.get(i).getLongitude(), 'K')));
                notifyItemChanged(i);*/

                Intent i1 = new Intent("notifymap");

                i1.putExtra("lat", firebaseLiveLocations.get(i).getLatitude());
                i1.putExtra("long", firebaseLiveLocations.get(i).getLongitude());
                i1.putExtra("mode", firebaseLiveLocations.get(i).getTransport_mode());
                i1.putExtra("staffid", firebaseLiveLocations.get(i).getStaff_id());
                i1.putExtra("pos", i);
                i1.putExtra("querystring", firebaseLiveLocations.get(i).getStaff_name() + " (Staff id: " + firebaseLiveLocations.get(i).getStaff_id() + ")");
                LocalBroadcastManager.getInstance(context).sendBroadcast(i1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return firebaseLiveLocations.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_photo)
        ImageView iv_photo;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_address)
        TextView tv_address;
        @BindView(R.id.tv_estimated_distance)
        TextView tv_estimated_distance;
        @BindView(R.id.tv_estimated_time)
        TextView tv_estimated_time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    protected BroadcastReceiver rec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent!=null && intent.hasExtra("pos")){
                notifyItemChanged(intent.getIntExtra("pos", 0));
            }
        }
    };

    /*public void updateNewList(ArrayList<FirebaseLiveLocation> firebaseLiveLocations1){
        final FirebaseLiveLocationDiffCallback firebaseLiveLocationDiffCallback = new FirebaseLiveLocationDiffCallback(this.firebaseLiveLocations, firebaseLiveLocations1);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(firebaseLiveLocationDiffCallback);

        firebaseLiveLocations.clear();
        this.firebaseLiveLocations.addAll(firebaseLiveLocations1);
        diffResult.dispatchUpdatesTo(this);


    }*/
}
