package com.example.xingli.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xingli.R;

public class UserForgetPasswordActivity extends AppCompatActivity {

    private Button byEmail;
    private Button byTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forget_password);

        byEmail = (Button) findViewById(R.id.forget_by_email);
        byTel = (Button) findViewById(R.id.forget_by_tel);

        byEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserForgetPasswordActivity.this, UserForgetByEmailActivity.class);
                startActivity(intent);
            }
        });

        byTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserForgetPasswordActivity.this, UserForgetByTelActivity.class);
                startActivity(intent);
            }
        });
    }
}