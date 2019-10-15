package com.densoftinfotech.densoftpayroll;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.densoftinfotech.densoftpayroll.classes.LeaveDetails;
import com.densoftinfotech.densoftpayroll.utilities.CommonActivity;

import java.util.ArrayList;

public class LeaveApplicationActivity extends CommonActivity {


    @BindView(R.id.tv_leavetype)
    TextView tv_leavetype;
    @BindView(R.id.tv_cancel)
    TextView tv_cancel;
    @BindView(R.id.tv_submit)
    TextView tv_submit;

    LeaveDetails leaveDetails;
    ArrayList<String> templates = new ArrayList<>();
    ArrayList<String> leavecodes = new ArrayList<>();

    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fullscreen();
        setContentView(R.layout.activity_leave);

        ButterKnife.bind(this);

        b = getIntent().getExtras();
        if (b != null) {
            if(b.containsKey("leave"));
            leaveDetails = (LeaveDetails)  b.getSerializable("leave");
            Log.d("leave details ", leaveDetails + "");
            if(leaveDetails!=null) {
                tv_leavetype.setText(leaveDetails.getName_of_leave());
            }
        }

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LeaveApplicationActivity.this.finish();
            }
        });

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LeaveApplicationActivity.this.finish();
            }
        });

    }
}
