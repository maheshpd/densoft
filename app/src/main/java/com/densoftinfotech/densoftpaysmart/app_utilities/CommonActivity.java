package com.densoftinfotech.densoftpaysmart.app_utilities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.WindowManager;

import com.densoftinfotech.densoftpaysmart.R;
import com.densoftinfotech.densoftpaysmart.model.StaffDetails;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import butterknife.ButterKnife;

public class CommonActivity extends AppCompatActivity {

    Toolbar toolbar;
    Context context;
    SharedPreferences preferences;
    public ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }


    public void toolbar_common() {
        ButterKnife.bind(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title));
        toolbar.setTitleMargin(20, 10, 10, 10);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        context = this;

        //DatabaseHelper.getInstance(context).deletebyid();

    }

    public void setTitle(String title/*, int color*/){
        ButterKnife.bind(this);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        toolbar.setTitleMargin(20, 10, 10, 10);
        //toolbar.setBackgroundColor(color);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
        context = this;
    }

    public void fullscreen(){
        /*Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);*/

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

    public StaffDetails getStaffDetails(Context context){
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        if(preferences.contains("StaffDetails")){
            String json = preferences.getString("StaffDetails", "");
            StaffDetails staffDetails = new StaffDetails();
            Type type = new TypeToken<StaffDetails>(){}.getType();
            staffDetails = new Gson().fromJson(json, type);

            if(staffDetails!=null){
                Constants.staffid = staffDetails.getStaffId();
            }

            return staffDetails;
        }else {
            return null;
        }
    }

    public void add_loader(Context context) {

        if (!((Activity) context).isFinishing()) {
            progressDialog = ProgressDialog.show(context, "Loading", "Please wait...");
            progressDialog.setCancelable(false);

        }
    }

    public void dismiss_loader(){
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
    }

    public String get_monthName(int i) {

        String month = "";
        switch (i) {
            case 1:
                month = "Jan";
                break;
            case 2:
                month = "Feb";
                break;
            case 3:
                month = "Mar";
                break;
            case 4:
                month = "Apr";
                break;
            case 5:
                month = "May";
                break;
            case 6:
                month = "Jun";
                break;
            case 7:
                month = "Jul";
                break;
            case 8:
                month = "Aug";
                break;
            case 9:
                month = "Sep";
                break;
            case 10:
                month = "Oct";
                break;
            case 11:
                month = "Nov";
                break;
            case 12:
            case 0:
                month = "Dec";
                break;
            default:
                return "";
        }

        return month;
    }

    public String get_monthNumber(String monthname) {
        String month = "";
        switch (monthname) {
            case "Jan":
                month = "1";
                break;
            case "Feb":
                month = "2";
                break;
            case "Mar":
                month = "3";
                break;
            case "Apr":
                month = "4";
                break;
            case "May":
                month = "5";
                break;
            case "Jun":
                month = "6";
                break;
            case "Jul":
                month = "7";
                break;
            case "Aug":
                month = "8";
                break;
            case "Sep":
                month = "9";
                break;
            case "Oct":
                month = "10";
                break;
            case "Nov":
                month = "11";
                break;
            case "Dec":
                month = "12";
                break;
            default:
                return "";
        }

        return month;
    }

}
