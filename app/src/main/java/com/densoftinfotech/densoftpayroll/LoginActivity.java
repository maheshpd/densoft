package com.densoftinfotech.densoftpayroll;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText et_staffid, et_password;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_staffid = findViewById(R.id.et_staffid);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkfor_noblankparam()) {
                    Intent i = new Intent(LoginActivity.this, PlannerActivity.class);
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
