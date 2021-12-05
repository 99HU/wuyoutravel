package com.example.poststation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PostForgetPasswordActivity extends AppCompatActivity {

    private Button byEmail;
    private Button byTel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_forget_password);

        byEmail = (Button) findViewById(R.id.forget_by_email);
        byTel = (Button) findViewById(R.id.forget_by_tel);

        byEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostForgetPasswordActivity.this, PostForgetByEmailActivity.class);
                startActivity(intent);
            }
        });

        byTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostForgetPasswordActivity.this, PostForgetByTelActivity.class);
                startActivity(intent);
            }
        });
    }
}