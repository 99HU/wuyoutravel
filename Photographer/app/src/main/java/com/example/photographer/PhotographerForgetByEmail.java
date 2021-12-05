package com.example.photographer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class PhotographerForgetByEmail extends AppCompatActivity {

    private EditText photographerName;
    private EditText managerEmail;

    private Button sendCode;

    private String s_photographerName;
    private String s_photographerEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photographer_forget_by_email);

        photographerName = (EditText) findViewById(R.id.photographer_name);
        managerEmail = (EditText) findViewById(R.id.user_email);

        sendCode = (Button) findViewById(R.id.send_code);
        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_photographerName = photographerName.getText().toString();
                s_photographerEmail = managerEmail.getText().toString();

                if (s_photographerName.isEmpty()) {
                    photographerName.setError(Html.fromHtml("<font color=#00ffff>请输入用户名</font>"));
                } else {
                    BmobQuery<Photographer> bmobQueryName = new BmobQuery<>();
                    bmobQueryName.addWhereEqualTo("username", s_photographerName);
                    bmobQueryName.findObjects(new FindListener<Photographer>() {
                        @Override
                        public void done(List<Photographer> list, BmobException e) {

                            //TODO: 邮箱激活最后要放到个人信息更改那里，此处仅为测试
                            emailVerify();

                            if (list.size() != 0) {
                                if (list.get(0).getEmail() == null){
                                    managerEmail.setError(Html.fromHtml("<font color=#00ffff>您的邮箱未注册</font>"));
                                } else if (list.get(0).getEmailVerified() == null) {
                                    managerEmail.setError(Html.fromHtml("<font color=#00ffff>您的邮箱状态缺失，请联系管理员</font>"));
                                } else if (list.get(0).getEmailVerified().toString().equals("false")) {
                                    managerEmail.setError(Html.fromHtml("<font color=#00ffff>您的邮箱未激活</font>"));
                                } else if (list.get(0).getEmailVerified().toString().equals("missing")) {
                                    managerEmail.setError(Html.fromHtml("<font color=#00ffff>应用验证功能未开启</font>"));
                                } else if (!s_photographerEmail.equals(list.get(0).getEmail().toString())) {
                                    managerEmail.setError(Html.fromHtml("<font color=#00ffff>邮箱填写错误</font>"));
                                } else {
                                    // TODO: 进行这里之前先进行邮箱的激活
                                    Toast.makeText(PhotographerForgetByEmail.this, "进行邮箱验证", Toast.LENGTH_SHORT).show();
                                    resetPasswordByEmail();
                                    finish();
                                }
                            } else {
                                Toast.makeText(PhotographerForgetByEmail.this, "用户名不存在", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    //用户邮箱激活
    private void emailVerify() {

        BmobUser.requestEmailVerify(s_photographerEmail, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(PhotographerForgetByEmail.this, "请求验证邮件成功，请到" + s_photographerEmail + "邮箱中进行激活账户。", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PhotographerForgetByEmail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //用户密码忘记--邮箱验证
    private void resetPasswordByEmail() {

        BmobUser.resetPasswordByEmail(s_photographerEmail, new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(PhotographerForgetByEmail.this, "重置密码请求成功，请到" + s_photographerEmail + "邮箱进行密码重置操作", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PhotographerForgetByEmail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}