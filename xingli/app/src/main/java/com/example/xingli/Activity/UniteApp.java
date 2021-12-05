package com.example.xingli.Activity;

import android.app.Application;

import db.DBMangerBudget;

/* 表示全局应用的类*/
public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化数据库
        DBMangerBudget.initDB(getApplicationContext());
    }
}
