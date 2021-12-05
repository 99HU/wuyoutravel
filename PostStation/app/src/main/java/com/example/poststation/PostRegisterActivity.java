package com.example.poststation;

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
import cn.bmob.v3.listener.UpdateListener;

public class PostRegisterActivity extends AppCompatActivity {

    private EditText managerName;
    private EditText passWord;
    private EditText againPassword;
    private EditText age;
    private EditText email;
    private EditText tel;
    private Spinner gender;
    private Button determine;
    private ImageView isExist_managerName;
    private boolean b_managerName = false;
    private boolean b_passWord = false;

    private String s_managerName;
    private String s_passWord;
    private String s_againPassword;
    private int i_age;
    private String s_email;
    private String s_tel;
    private String s_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_register);

        //读取控件
        managerName = (EditText) findViewById(R.id.managername);
        passWord = (EditText) findViewById(R.id.password);
        againPassword = (EditText) findViewById(R.id.again_password);
        age = (EditText) findViewById(R.id.age);
        email = (EditText) findViewById(R.id.email);
        tel = (EditText) findViewById(R.id.tel);
        gender = (Spinner) findViewById(R.id.gender);
        determine = (Button) findViewById(R.id.determine);
        isExist_managerName = (ImageView) findViewById(R.id.isExist_userName);

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
                if (!b_managerName) {
                    Toast.makeText(PostRegisterActivity.this, "请先按下\"isExist\"检查用户名规范", Toast.LENGTH_SHORT).show();
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


                    if (!(b_managerName && b_passWord)) {
                        Toast.makeText(PostRegisterActivity.this, "请规范输入用户名和密码", Toast.LENGTH_SHORT).show();
                    } else {
                        Manager manager = new Manager(s_managerName, s_passWord);

                        //涉及属性不允许为空
                        if (i_age != 0)
                            manager.setAge(i_age);
                        else
                            age.setError(Html.fromHtml("<font color=#00ffff>请填写年龄</font>"));

                        if (!s_email.isEmpty()) {
                            manager.setEmail(s_email);
                            manager.setEmailVerified(false);
                        } else {
                            email.setError(Html.fromHtml("<font color=#00ffff>请填写邮箱</font>"));
                        }

                        if (!s_tel.isEmpty()) {
                            manager.setMobilePhoneNumber(s_tel);
                            manager.setMobilePhoneNumberVerified(false);
                        }
                        else
                            tel.setError(Html.fromHtml("<font color=#00ffff>请填写电话</font>"));

                        manager.setGender(s_gender);


                        if ((String.valueOf(i_age) != null) && (!s_email.isEmpty()) && (!s_tel.isEmpty()) /*&& (b_tel == true)*/) {
                            manager.signUp(new SaveListener<Manager>() {
                                @Override
                                public void done(Manager manager, BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(PostRegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                        create_post(manager);
                                        finish();
                                    } else {
                                        Toast.makeText(PostRegisterActivity.this, "注册失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(PostRegisterActivity.this, "请将信息填写完整", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        //isExist按钮
        isExist_managerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //状态初始化
                b_managerName = false;
                //存储输入框相应的值
                s_managerName = managerName.getText().toString();
                //用户名规范
                if (s_managerName.isEmpty()) {
                    managerName.setError(Html.fromHtml("<font color=#00ffff>用户名不能为空</font>"));
                } else {
                    BmobQuery<Manager> userNameBmobQuery = new BmobQuery<>();
                    userNameBmobQuery.addWhereEqualTo("username", s_managerName);
                    userNameBmobQuery.findObjects(new FindListener<Manager>() {
                        @Override
                        public void done(List<Manager> list, BmobException e) {
                            if (list.size() != 0) {
                                managerName.setError(Html.fromHtml("<font color=#00ffff>用户名已存在</font>"));
                            } else {
                                b_managerName = true;
                                Toast.makeText(PostRegisterActivity.this, "用户名合法", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void create_post(Manager manager) {
        Post post1 = new Post(manager.getNickName() + "的驿站", "", "", "", 0,
                0, 0, "", "", "", "",
                "", 0, 0, manager);
        post1.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(PostRegisterActivity.this, "专属管理员保存成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PostRegisterActivity.this, "专属管理员保存失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}