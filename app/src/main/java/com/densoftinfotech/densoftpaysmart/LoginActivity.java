package com.densoftinfotech.densoftpaysmart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.densoftinfotech.densoftpaysmart.app_utilities.CommonActivity;
import com.densoftinfotech.densoftpaysmart.app_utilities.URLS;
import com.densoftinfotech.densoftpaysmart.room_database.Paysmart_roomdatabase;
import com.densoftinfotech.densoftpaysmart.room_database.Staff.StaffDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.preference.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends CommonActivity {

    @BindView(R.id.et_staffid)
    EditText et_staffid;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.btn_login)
    Button btn_login;

    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    //RequestQueue requestQueue;
    StringRequest request_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fullscreen();
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        edit = preferences.edit();

        if(preferences.getBoolean("login", false)){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkfor_noblankparam()) {
                    get_login_data();
                }
            }
        });

    }

    private void get_login_data() {

        edit.putBoolean("login", true);
        edit.apply();
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
        //RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);

        /*request_login = new StringRequest(Request.Method.POST, "http://www.google.com",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("Response is: ", response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("That didn't work!", "oops");
            }
        });*/

        /*request_login = new StringRequest(Request.Method.POST, URLS.login_api()*//*"http://167.71.229.74/barcodescanner/tagdata.php"*//*, response -> {
            try {
                Log.d("response " , response.toString());

                *//*JSONArray array = new JSONArray(response);
                JSONObject obj_details = array.optJSONObject(0);

                if(obj_details.has("Patientid")){
                    StaffDetails staffDetails = new StaffDetails();
                    staffDetails.setPatientid(obj_details.optString("Patientid"));
                    staffDetails.setPName(obj_details.optString("PName"));
                    staffDetails.setMobile1(obj_details.optString("Mobile1"));
                    staffDetails.setEmail1(obj_details.optString("Email1"));
                    staffDetails.setDOB(obj_details.optString("DOB"));
                    staffDetails.setGender(obj_details.optString("Gender"));
                    staffDetails.setJoiningDate(obj_details.optString("JoiningDate"));

                    SaveDetails_async saveDetails_async = new SaveDetails_async();
                    saveDetails_async.execute(staffDetails);

                }else {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.pleasecontactbranch), Toast.LENGTH_LONG).show();
                }*//*


            }catch (Exception e){
                e.printStackTrace();
            }
        }, error -> Log.d("volley err ", error + "")){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("user", et_staffid.getText().toString());
                params.put("password", et_password.getText().toString());
                Log.d("param ", params + "");
                return params;
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            *//*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> header = new HashMap<>();
                header.put("Content-Type", "application/json");
                return header;
            }*//*

        };

        requestQueue.add(request_login);*/

    }

    private boolean checkfor_noblankparam() {
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
    }


    private class SaveDetails_async extends AsyncTask<StaffDetails, String, Void> {

        @Override
        protected Void doInBackground(StaffDetails... voids) {
            Paysmart_roomdatabase.get_PaysmartDatabase(LoginActivity.this).staffDetails_dao().insertAll(voids[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void s) {
            edit.putBoolean("login", true);
            edit.apply();
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
            super.onPostExecute(s);
        }
    }
}
