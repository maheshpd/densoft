package com.densoftinfotech.densoftpayroll;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_staffid)
    EditText et_staffid;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.btn_login)
    Button btn_login;

    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        edit = preferences.edit();

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkfor_noblankparam()) {
                    edit.putBoolean("login", true);
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

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
}
