package com.densoftinfotech.densoftpaysmart;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.densoftinfotech.densoftpaysmart.location_utilities.UserLocation;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MarkAttendanceActivity extends CommonActivity {

    @BindView(R.id.tv_checkin)
    TextView tv_checkin;
    @BindView(R.id.tv_checkout)
    TextView tv_checkout;
    @BindView(R.id.tv_checkintime)
    TextView tv_checkintime;
    @BindView(R.id.tv_checkouttime)
    TextView tv_checkouttime;
    UserLocation userLocation;
    String longitude = "";
    String latitude = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        toolbar_common();
        back();

        ButterKnife.bind(this);

        tv_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_check_time(0);
            }
        });

        tv_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_check_time(1);
            }
        });

    }

    private void set_check_time(int flag) {
        userLocation = new UserLocation(MarkAttendanceActivity.this);
        if (userLocation.isGpsEnabled()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy ', ' HH:mm:ss z");
            String currentDateandTime = sdf.format(new Date());
            if(flag == 0){
                tv_checkintime.setText(getResources().getString(R.string.checkintime) + " " + currentDateandTime + " at " + userLocation.getAddress());
                tv_checkintime.setVisibility(View.VISIBLE);
            }else{
                tv_checkouttime.setText(getResources().getString(R.string.checkouttime) + " " + currentDateandTime + " at " + userLocation.getAddress());
                tv_checkouttime.setVisibility(View.VISIBLE);
            }
            longitude = Double.toString(userLocation.getLongitude());
            latitude = Double.toString(userLocation.getLatitude());
            Log.d("lat and long checkin ", longitude + "    " + latitude +  " address " +userLocation.getAddress());
        } else {
            userLocation.gpsNotEnabled_Alert();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.attendance_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_checkin:
                set_check_time(0);
                break;
            case R.id.menu_checkout:
                set_check_time(1);
                break;

        }


        return super.onOptionsItemSelected(item);
    }
}



/*final Animation animLeftToRight = AnimationUtils.loadAnimation(MarkAttendanceActivity.this, R.anim.swiperight);
        animLeftToRight.setRepeatMode(Animation.INFINITE);
        tv_checkin.startAnimation(animLeftToRight);

        final Animation animRightToLeft = AnimationUtils.loadAnimation(MarkAttendanceActivity.this, R.anim.swipeleft);
        animRightToLeft.setRepeatMode(Animation.INFINITE);
        tv_checkout.startAnimation(animRightToLeft);*/

        /*final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(3000L);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final float progress = (float) animation.getAnimatedValue();
                final float width = tv_checkin.getWidth();
                final float translationX = width * progress;
                tv_checkin.setTranslationX(translationX);

                final float translationX1 = - (width * progress);
                tv_checkout.setTranslationX(translationX1);
            }
        });
        animator.start();*/
