package com.densoftinfotech.densoftpayroll.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;

import com.densoftinfotech.densoftpayroll.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CommonActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Context context;
    SharedPreferences preferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public void getsharedpref(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void toolbar_common() {
        ButterKnife.bind(this);
        toolbar.setTitle(getResources().getString(R.string.title));
        toolbar.setTitleMargin(20, 10, 10, 10);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        context = this;

    }

    public void back() {
        ButterKnife.bind(this);
        Drawable backicon = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        backicon.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
        getSupportActionBar().setHomeAsUpIndicator(backicon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
