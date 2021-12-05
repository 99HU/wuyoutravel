package com.example.guide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class GuideRegister extends AppCompatActivity {

    private EditText guideName;
    private EditText passWord;
    private EditText againPassword;
    private EditText age;
    private EditText email;
    private EditText tel;
    private Spinner gender;
    private Button determine;
    private ImageView isExist_guideName;
    private boolean b_guideName = false;
    private boolean b_passWord = false;

    private String s_guideName;
    private String s_passWord;
    private String s_againPassword;
    private int i_age;
    private String s_email;
    private String s_tel;
    private String s_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_register);

        //读取控件
        guideName = (EditText) findViewById(R.id.guide_name);
        passWord = (EditText) findViewById(R.id.password);
        againPassword = (EditText) findViewById(R.id.again_password);
        age = (EditText) findViewById(R.id.age);
        email = (EditText) findViewById(R.id.email);
        tel = (EditText) findViewById(R.id.tel);
        gender = (Spinner) findViewById(R.id.gender);
        determine = (Button) findViewById(R.id.determine);
        isExist_guideName = (ImageView) findViewById(R.id.isExist_userName);

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                s_gender = gender.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //register按钮
        determine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!b_guideName) {
                    Toast.makeText(GuideRegister.this, "请先按下\"isExist\"检查用户名规范", Toast.LENGTH_SHORT).show();
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




                    //密码规范
                    if (s_passWord.isEmpty()) {
                        passWord.setError(Html.fromHtml("<font color=#00ffff>密码不能为空</font>"));
                    } else if (!s_againPassword.equals(s_passWord)){
                        againPassword.setError(Html.fromHtml("<font color=#00ffff>两次密码不一致</font>"));
                    } else {
                        b_passWord = true;
                    }


                    if (!(b_guideName && b_passWord)) {
                        Toast.makeText(GuideRegister.this, "请规范输入用户名和密码", Toast.LENGTH_SHORT).show();
                    } else {
                        Guide guide = new Guide(s_guideName, s_passWord);

                        //涉及属性不允许为空
                        if (i_age != 0)
                            guide.setAge(i_age);
                        else
                            age.setError(Html.fromHtml("<font color=#00ffff>请填写年龄</font>"));

                        if (!s_email.isEmpty()) {
                            guide.setEmail(s_email);
                            guide.setEmailVerified(false);
                        } else {
                            email.setError(Html.fromHtml("<font color=#00ffff>请填写邮箱</font>"));
                        }

                        if (!s_tel.isEmpty()) {
                            guide.setMobilePhoneNumber(s_tel);
                            guide.setMobilePhoneNumberVerified(false);
                        }
                        else
                            tel.setError(Html.fromHtml("<font color=#00ffff>请填写电话</font>"));

                        guide.setGender(s_gender);


                        if ((String.valueOf(i_age) != null) && (!s_email.isEmpty()) && (!s_tel.isEmpty()) /*&& (b_tel == true)*/) {
                            guide.signUp(new SaveListener<Guide>() {
                                @Override
                                public void done(Guide photographer, BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(GuideRegister.this, "注册成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(GuideRegister.this, "注册失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(GuideRegister.this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        //isExist按钮
        isExist_guideName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //状态初始化
                b_guideName = false;
                //存储输入框相应的值
                s_guideName = guideName.getText().toString();
                //用户名规范
                if (s_guideName.isEmpty()) {
                    guideName.setError(Html.fromHtml("<font color=#00ffff>用户名不能为空</font>"));
                } else {
                    BmobQuery<Guide> userNameBmobQuery = new BmobQuery<>();
                    userNameBmobQuery.addWhereEqualTo("username", s_guideName);
                    userNameBmobQuery.findObjects(new FindListener<Guide>() {
                        @Override
                        public void done(List<Guide> list, BmobException e) {
                            if (list.size() != 0) {
                                guideName.setError(Html.fromHtml("<font color=#00ffff>用户名已存在</font>"));
                            } else {
                                b_guideName = true;
                                Toast.makeText(GuideRegister.this, "用户名合法", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}