package com.example.photographer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class PhotographerForgetByTel extends AppCompatActivity {

    private EditText photographerName;
    private EditText photographerTel;
    private EditText code;
    private EditText newPassword;

    private ImageView sendCode;
    private Button verify;

    private String s_photographerName;
    private String s_photographerTel;
    private String s_code;
    private String s_newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photographer_forget_by_tel);

        photographerName = (EditText) findViewById(R.id.photographer_name);
        photographerTel = (EditText) findViewById(R.id.photographer_tel);
        code = (EditText) findViewById(R.id.code);
        newPassword = (EditText) findViewById(R.id.new_password);

        sendCode = (ImageView) findViewById(R.id.send_code);
        verify = (Button) findViewById(R.id.verify);

        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_photographerName = photographerName.getText().toString();
                s_photographerTel = photographerTel.getText().toString();

                if (s_photographerName.isEmpty()) {
                    photographerName.setError(Html.fromHtml("<font color=#00ffff>请输入用户名</font>"));
                } else {
                    BmobQuery<Photographer> bmobQueryName = new BmobQuery<>();
                    bmobQueryName.addWhereEqualTo("username", s_photographerName);
                    bmobQueryName.findObjects(new FindListener<Photographer>() {
                        @Override
                        public void done(List<Photographer> list, BmobException e) {

                            //TODO:此处为手机号激活过程，最后要放到个人信息更改那里，此处仅为测试
                            /*BmobSMS.requestSMSCode(s_userTel, "", new QueryListener<Integer>() {
                                @Override
                                public void done(Integer smsId, BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(UserForgetByTelActivity.this, "发送验证码成功，短信ID：" + smsId + "\n", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(UserForgetByTelActivity.this, "发送验证码失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            BmobSMS.verifySmsCode(s_userTel, "727338", new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(UserForgetByTelActivity.this, "验证码验证成功，您可以在此时进行绑定操作！\n", Toast.LENGTH_SHORT).show();
                                        User user = BmobUser.getCurrentUser(User.class);
                                        user.setMobilePhoneNumber(s_userTel);
                                        user.setMobilePhoneNumberVerified(true);
                                        user.update(new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if (e == null) {
                                                    Toast.makeText(UserForgetByTelActivity.this,"绑定手机号码成功",Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(UserForgetByTelActivity.this, "绑定手机号码失败：" + e.getErrorCode() + "-" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(UserForgetByTelActivity.this, "验证码验证失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });*/

                            //此处为需要留下的代码
                            BmobSMS.requestSMSCode(s_photographerTel, "", new QueryListener<Integer>() {
                                @Override
                                public void done(Integer smsId, BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(PhotographerForgetByTel.this, "发送验证码成功，短信ID：" + smsId + "\n", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(PhotographerForgetByTel.this, "发送验证码失败：" + e.getErrorCode() + "-" + e.getMessage() + "\n", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    });
                }
            }
        });
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                s_code = code.getText().toString();
                s_newPassword = newPassword.getText().toString();

                BmobUser.resetPasswordBySMSCode(s_code, s_newPassword, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(PhotographerForgetByTel.this, "重置成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(PhotographerForgetByTel.this, "重置失败：" + e.getErrorCode() + "-" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}