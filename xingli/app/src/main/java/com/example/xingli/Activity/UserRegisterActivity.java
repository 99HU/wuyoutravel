package com.example.xingli.Activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xingli.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import db.User;

public class UserRegisterActivity extends AppCompatActivity {

    private EditText userName;
    private EditText passWord;
    private EditText againPassword;
    private EditText age;
    private EditText email;
    private EditText tel;
    private Spinner gender;
    private Button determine;
    private ImageView isExist_userName;
    private boolean b_userName = false;
    private boolean b_passWord = false;

    private String s_userName;
    private String s_passWord;
    private String s_againPassword;
    private int i_age;
    private String s_email;
    private String s_tel;
    private String s_gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        //读取控件
        userName = (EditText) findViewById(R.id.username);
        passWord = (EditText) findViewById(R.id.password);
        againPassword = (EditText) findViewById(R.id.again_password);
        age = (EditText) findViewById(R.id.age);
        email = (EditText) findViewById(R.id.email);
        tel = (EditText) findViewById(R.id.tel);
        gender = (Spinner) findViewById(R.id.gender);
        determine = (Button) findViewById(R.id.determine);
        isExist_userName = (ImageView) findViewById(R.id.isExist_userName);


        //register按钮
        determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!b_userName) {
                    Toast.makeText(UserRegisterActivity.this, "请先按下\"用户名\"右边的按钮检查用户名规范", Toast.LENGTH_SHORT).show();
                } else {
                    //状态初始化
                    b_passWord = false;
                    //存储输入框相应的值
                    s_passWord = passWord.getText().toString();
                    s_againPassword = againPassword.getText().toString();
                    if (!age.getText().toString().isEmpty())
                        i_age = Integer.parseInt(age.getText().toString());
                    s_email = email.getText().toString();
                    s_tel = tel.getText().toString();
                    gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            s_gender = gender.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });


                    //密码规范
                    if (s_passWord.isEmpty()) {
                        passWord.setError(Html.fromHtml("<font color=#00ffff>密码不能为空</font>"));
                    } else if (!s_againPassword.equals(s_passWord)){
                        againPassword.setError(Html.fromHtml("<font color=#00ffff>两次密码不一致</font>"));
                    } else {
                        b_passWord = true;
                    }


                    if (!(b_userName && b_passWord)) {
                        Toast.makeText(UserRegisterActivity.this, "请规范输入用户名和密码", Toast.LENGTH_SHORT).show();
                    } else {
                        User user = new User();
                        user.setUsername(s_userName);
                        user.setPassword(s_passWord);

                        //涉及属性不允许为空
                        if (i_age != 0)
                            user.setAge(i_age);
                        else
                            age.setError(Html.fromHtml("<font color=#00ffff>请填写年龄</font>"));

                        if (!s_email.isEmpty()) {
                            user.setEmail(s_email);
                            user.setEmailVerified(false);
                        } else {
                            email.setError(Html.fromHtml("<font color=#00ffff>请填写邮箱</font>"));
                        }

                        if (!s_tel.isEmpty()) {
                            user.setMobilePhoneNumber(s_tel);
                            user.setMobilePhoneNumberVerified(false);
                        }
                        else
                            tel.setError(Html.fromHtml("<font color=#00ffff>请填写电话</font>"));

                        user.setUserGender(s_gender);

                        user.signUp(new SaveListener<User>() {
                            @Override
                            public void done(User user, BmobException e) {
                                if (e == null) {
                                    Toast.makeText(UserRegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(UserRegisterActivity.this, "注册失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });

        //isExist按钮
        isExist_userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //状态初始化
                b_userName = false;
                //存储输入框相应的值
                s_userName = userName.getText().toString();
                //用户名规范
                if (s_userName.isEmpty()) {
                    userName.setError(Html.fromHtml("<font color=#00ffff>用户名不能为空</font>"));
                } else {
                    BmobQuery<User> userNameBmobQuery = new BmobQuery<>();
                    userNameBmobQuery.addWhereEqualTo("username", s_userName);
                    userNameBmobQuery.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if (list.size() != 0) {
                                userName.setError(Html.fromHtml("<font color=#00ffff>用户名已存在</font>"));
                            } else {
                                b_userName = true;
                                Toast.makeText(UserRegisterActivity.this, "用户名合法", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}