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

import Adapter.GuideRecAdapter;
import base.BaseActivity1;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import db.Guide;
import db.User;

public class GuideActivity extends BaseActivity1 {
    EditText searchGuide;
    User user;
    RecyclerView GuideRecycleView;
    private List<Guide> guideList=new ArrayList<>();
    GuideRecAdapter guideRecAdapter;
    public void  init()
    {
        searchGuide=(EditText) findViewById(R.id.search_guide);
        GuideRecycleView=(RecyclerView) findViewById(R.id.guide_recycleview);
        user = User.fetchUserInfo();

        BmobQuery<Guide> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("identity", "向导");
        bmobQuery.findObjects(new FindListener<Guide>() {
            @Override
            public void done(List<Guide> list, BmobException e) {
                for (Guide guide : list) {
                    guideList.add(guide);
                }
                //设置listview
                LinearLayoutManager layoutManager=new LinearLayoutManager(GuideActivity.this);
                GuideRecycleView.setLayoutManager(layoutManager);
                guideRecAdapter=new GuideRecAdapter(guideList,GuideActivity.this);
                GuideRecycleView.setAdapter(guideRecAdapter);
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        init();
        searchGuide.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                guideRecAdapter.getFilter().filter(s);
                Log.d("PakegeActivity","检测文字的变化了");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}