package com.densoftinfotech.densoftpayroll;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.densoftinfotech.densoftpayroll.utilities.CommonActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        toolbar_common();
        back();

        ButterKnife.bind(this);

        final Animation animLeftToRight = AnimationUtils.loadAnimation(MarkAttendanceActivity.this, R.anim.swiperight);
        animLeftToRight.setRepeatMode(Animation.INFINITE);
        //tv_checkin.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        tv_checkin.startAnimation(animLeftToRight);

        final Animation animRightToLeft = AnimationUtils.loadAnimation(MarkAttendanceActivity.this, R.anim.swipeleft);
        animRightToLeft.setRepeatMode(Animation.INFINITE);
        //tv_checkout.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        tv_checkout.startAnimation(animRightToLeft);

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

        tv_checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss z");
                String currentDateandTime = sdf.format(new Date());
                tv_checkintime.setText(getResources().getString(R.string.checkintime) + " " + currentDateandTime);
                tv_checkintime.setVisibility(View.VISIBLE);
            }
        });

        tv_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss z");
                String currentDateandTime = sdf.format(new Date());
                tv_checkouttime.setText(getResources().getString(R.string.checkouttime) + " " + currentDateandTime);
                tv_checkouttime.setVisibility(View.VISIBLE);
            }
        });

    }
}
