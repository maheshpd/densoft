package com.densoftinfotech.densoftpaysmart;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.app_utilities.InternetUtils;
import com.densoftinfotech.densoftpaysmart.classes.StaffDetails;
import com.densoftinfotech.densoftpaysmart.retrofit.GetServiceInterface;
import com.densoftinfotech.densoftpaysmart.retrofit.RetrofitClient;
import com.densoftinfotech.densoftpaysmart.room_database.Paysmart_roomdatabase;
import com.densoftinfotech.densoftpaysmart.room_database.Staff.StaffDetailsRoom;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends CommonActivity {

    @BindView(R.id.et_staffid)
    EditText et_staffid;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.et_customerid)
    EditText et_customerid;

    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;

    private GetServiceInterface apiInterface;

    private ArrayList<StaffDetails> staffDetailsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.login));
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        edit = preferences.edit();

        if (preferences.getBoolean("login", false)) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }


        check_permission();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkfor_noblankparam()) {

                    if(InternetUtils.getInstance(LoginActivity.this).available())
                        get_login_data();
                    else
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.pleasecheckinternet), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void get_login_data() {

        Retrofit retrofit = RetrofitClient.getRetrofit();
        apiInterface = retrofit.create(GetServiceInterface.class);

        Map<String, Object> params = new HashMap<>();

        params.put("customerid", et_customerid.getText().toString());
        params.put("user", et_staffid.getText().toString());
        params.put("password", et_password.getText().toString());

        edit.putString("customerid", et_customerid.getText().toString());


        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), (new JSONObject(params)).toString());

        Call<ArrayList<StaffDetails>> call = apiInterface.request_login(body);

        call.enqueue(new Callback<ArrayList<StaffDetails>>() {
            @Override
            public void onResponse(Call<ArrayList<StaffDetails>> call, Response<ArrayList<StaffDetails>> response) {

                if(response.isSuccessful() && response.body()!=null) {
                    staffDetailsArrayList = response.body();

                    if(staffDetailsArrayList.get(0).getColumn1().trim().equalsIgnoreCase("ERROR")){
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.pleasecontactadmin), Toast.LENGTH_LONG).show();
                    }else {
                        SaveDetails_async saveDetails_async = new SaveDetails_async();
                        saveDetails_async.execute();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.pleasecontactadmin), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<StaffDetails>> call, Throwable t) {
                Log.d("failed is ", t.getMessage());
            }
        });


    }


    private boolean checkfor_noblankparam() {
        if(!et_customerid.getText().toString().trim().equals("")) {
            if (!et_staffid.getText().toString().trim().equals("")) {
                if (!et_password.getText().toString().trim().equals("")) {
                    return true;
                } else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.pleaseenterpassword), Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(LoginActivity.this, getResources().getString(R.string.pleaseenterstaffid), Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{Toast.makeText(LoginActivity.this, getResources().getString(R.string.pleaseentercustomerid), Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    private class SaveDetails_async extends AsyncTask<ArrayList<StaffDetails>, Void, Void> {

        StaffDetailsRoom staffDetailsRoom;
        @Override
        protected Void doInBackground(ArrayList<StaffDetails>... voids) {

            staffDetailsRoom = new StaffDetailsRoom(staffDetailsArrayList.get(0).getStaffId(), staffDetailsArrayList.get(0).getPName(), staffDetailsArrayList.get(0).getMobile1(), staffDetailsArrayList.get(0).getEmail1(),
                    staffDetailsArrayList.get(0).getGender(), staffDetailsArrayList.get(0).getJoiningDate(), staffDetailsArrayList.get(0).getCompanyName(), staffDetailsArrayList.get(0).getBranchName(),
                    staffDetailsArrayList.get(0).getDepartment(), staffDetailsArrayList.get(0).getDesignation(), staffDetailsArrayList.get(0).getStaffPhoto(), staffDetailsArrayList.get(0).getDomainUrl());
            edit.putString("staffid", staffDetailsArrayList.get(0).getStaffId());
            edit.putString("company_name", staffDetailsArrayList.get(0).getCompanyName());

            Paysmart_roomdatabase.get_PaysmartDatabase(LoginActivity.this).staffDetails_dao().insertAll(staffDetailsRoom);

            return null;
        }

        @Override
        protected void onPostExecute(Void s) {

            edit.putBoolean("login", true);
            edit.apply();
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();

        }
    }

    private void check_permission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        21);

        } else {
            // Permission has already been granted
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 21) {

        }else{
            //show_snackbar();
        }
    }

    /*private void show_snackbar() {
        showSnackbar(R.string.permission_rationale,
                android.R.string.ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Request permission
                        ActivityCompat.requestPermissions(this,
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                21);
                    }
                });
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }*/
}
