package com.example.xingli.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xingli.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.PhotographerRecAdapter;
import base.BaseActivity1;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import db.Photographer;
import db.User;

public class photographerActivity extends BaseActivity1 {
    User user;
    Photographer photographer;
    public RecyclerView photographerListView;
    public EditText editText;
    private List<Photographer> photographerList=new ArrayList<>();
    PhotographerRecAdapter photographerRecAdapter;

    private void init(){
        user =User.fetchUserInfo();
        photographerListView=(RecyclerView) findViewById(R.id.photographer_listview);
        editText=(EditText)findViewById(R.id.search_photographer);
        BmobQuery<Photographer> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("identity", "摄影师");
        bmobQuery.findObjects(new FindListener<Photographer>() {
            @Override
            public void done(List<Photographer> list, BmobException e) {
                for (Photographer photographer : list) {
                    photographerList.add(photographer);
                }
                //设置listview
                LinearLayoutManager layoutManager=new LinearLayoutManager(photographerActivity.this);
                photographerListView.setLayoutManager(layoutManager);
                photographerRecAdapter=new PhotographerRecAdapter(photographerList,photographerActivity.this);
                photographerListView.setAdapter(photographerRecAdapter);
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photographer);
        init();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                photographerRecAdapter.getFilter().filter(s);
                Log.d("PakegeActivity","检测文字的变化了");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}