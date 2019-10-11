package com.densoftinfotech.densoftpayroll.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ScrollView;
import com.densoftinfotech.densoftpayroll.R;
import com.densoftinfotech.densoftpayroll.adapter.CalendarDetailsAdapter;
import com.densoftinfotech.densoftpayroll.classes.CalendarDetails;
import com.densoftinfotech.densoftpayroll.demo_class.CalendarDetailsDemo;

import java.util.ArrayList;
import java.util.Calendar;

public class MyPlannerFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @BindView(R.id.calendar)
    CalendarView calendarview;
    @BindView(R.id.recyclerview_planner)
    RecyclerView recyclerview;
    @BindView(R.id.scrollview)
    ScrollView scrollview;

    View v;
    Calendar calendar = Calendar.getInstance();

    ArrayList<CalendarDetails> calendarDetails = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    CalendarDetailsAdapter calendarDetailsAdapter;

    public MyPlannerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyPlannerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyPlannerFragment newInstance(String param1, String param2) {
        MyPlannerFragment fragment = new MyPlannerFragment();
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
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_planner, container, false);

        ButterKnife.bind(this, v);
        scrollview.fullScroll(ScrollView.FOCUS_UP);
        recyclerview.setFocusable(false);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(layoutManager);

        for (int i = 0; i < CalendarDetailsDemo.status.length; i++) {
            calendarDetails.add(new CalendarDetails(CalendarDetailsDemo.status[i], CalendarDetailsDemo.description[i]));
        }

        calendarDetailsAdapter = new CalendarDetailsAdapter(getActivity(), calendarDetails);
        recyclerview.setAdapter(calendarDetailsAdapter);

        return v;
    }
}
