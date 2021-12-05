package com.example.guide;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private Button login;
    private Button register;
    private TextView forget;
    private EditText accountEdit;
    private EditText passwordEdit;
    private CheckBox rememberPass;

    private String s_account;
    private String s_passWord;

    DialogInterface.OnClickListener listener2 = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    Intent intent = new Intent(MainActivity.this, GuideForgetByTel.class);
                    startActivity(intent);
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    Intent intent1 = new Intent(MainActivity.this, GuideForgetByEmail.class);
                    startActivity(intent1);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this, "f34bb86178bb7aba446a1c3df046e24d");

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        accountEdit = (EditText) findViewById(R.id.account);
        passwordEdit = (EditText) findViewById(R.id.password);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);

        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            String account = pref.getString("account", "");
            String password = pref.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }

        //login按钮
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                s_account = accountEdit.getText().toString();
                s_passWord = passwordEdit.getText().toString();

                if (s_account.isEmpty() || s_passWord.isEmpty()) {
                    Toast.makeText(MainActivity.this, "请先输入账号/密码", Toast.LENGTH_SHORT).show();

                } else {
                    BmobQuery<Guide> userBmobQuery = new BmobQuery<>();
                    userBmobQuery.addWhereEqualTo("username", s_account);
                    userBmobQuery.findObjects(new FindListener<Guide>() {
                        @Override
                        public void done(List<Guide> list, BmobException e) {
                            if (list.size() != 0) {
                                final Guide photographer = new Guide(s_account, s_passWord);
                                photographer.login(new SaveListener<Guide>() {
                                    @Override
                                    public void done(Guide bmobPostStation, BmobException e) {
                                        if (e == null) {
                                            //账号密码正确，复选框操作
                                            editor = pref.edit();
                                            if (rememberPass.isChecked()) {
                                                editor.putBoolean("remember_password", true);
                                                editor.putString("account", s_account);
                                                editor.putString("password", s_passWord);
                                            } else {
                                                editor.clear();
                                            }
                                            editor.apply();
                                            //登录操作
                                            //Manager postStation = BmobUser.getCurrentUser(Manager.class);
                                            Toast.makeText(MainActivity.this, "欢迎" + photographer.getUsername(), Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(MainActivity.this, GuideHomePage.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(MainActivity.this, "登录失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(MainActivity.this, "用户不存在，请先注册", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        //register按钮
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GuideRegister.class);
                startActivity(intent);
            }
        });

        //forget按钮
        forget = (TextView) findViewById(R.id.forget);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog isExit = new AlertDialog.Builder(MainActivity.this).create();

                isExit.setTitle("系统提示");
                // 设置对话框消息
                isExit.setMessage("请您选择找回密码的方式");
                // 添加选择按钮并注册监听
                isExit.setButton("手机号验证", listener2);
                isExit.setButton2("电子邮箱验证", listener2);
                // 显示对话框
                isExit.show();
            }
        });
    }
}