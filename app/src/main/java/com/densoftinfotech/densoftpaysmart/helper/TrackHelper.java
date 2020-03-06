package com.densoftinfotech.densoftpaysmart.helper;

import com.densoftinfotech.densoftpaysmart.model.LocalTrack;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class TrackHelper {
    Realm realm;
    RealmResults<LocalTrack> results;

    public TrackHelper(Realm realm) {
        this.realm = realm;
    }

    public void selectDB(){
//        results = realm.where(LocalTrack.class).findAll();
    }

    public ArrayList<LocalTrack> list(){
        ArrayList<LocalTrack> list = new ArrayList<>();
        for (LocalTrack l : results) {
            list.add(l);
        }
        return list;
    }


}
