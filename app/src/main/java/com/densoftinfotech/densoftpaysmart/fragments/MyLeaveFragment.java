package com.densoftinfotech.densoftpaysmart.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.densoftinfotech.densoftpaysmart.R;
import com.densoftinfotech.densoftpaysmart.adapter.LeaveAdapter;
import com.densoftinfotech.densoftpaysmart.classes.LeaveDetails;

import java.util.ArrayList;

public class MyLeaveFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    View v;

    ArrayList<LeaveDetails> leaveDetails = new ArrayList<>();
    @BindView(R.id. recyclerview_leave) RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    LeaveAdapter leaveAdapter;

    public MyLeaveFragment() {
        //Required empty public constructor
    }

    public static MyLeaveFragment newInstance(String param1, String param2) {
        MyLeaveFragment fragment = new MyLeaveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_my_leave, container, false);
        ButterKnife.bind(this, v);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        /*for(int i = 0; i< LeaveDetailsDemo.name_of_leave.length; i++){
            leaveDetails.add(new LeaveDetails(LeaveDetailsDemo.days[i], LeaveDetailsDemo.name_of_leave[i]));
        }*/

        leaveAdapter = new LeaveAdapter(getActivity(), leaveDetails);
        recyclerView.setAdapter(leaveAdapter);

        return v;
    }

}
