package com.densoftinfotech.densoftpaysmart.model;

import android.os.Bundle;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class FirebaseLiveLocationDiffCallback extends DiffUtil.Callback {

    private final ArrayList<FirebaseLiveLocation> oldList;
    private final ArrayList<FirebaseLiveLocation> newList;

    public FirebaseLiveLocationDiffCallback(ArrayList<FirebaseLiveLocation> oldList, ArrayList<FirebaseLiveLocation> newList) {
        this.oldList = oldList;
        this.newList = newList;

    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getStaff_id() == newList.get(newItemPosition).getStaff_id();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getStaff_id() == newList.get(newItemPosition).getStaff_id();
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {

        return newList.get(newItemPosition);

        //return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
