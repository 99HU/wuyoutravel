package com.example.guide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class GuideHomePage extends AppCompatActivity {
    public static boolean UpdateFlag=false;
    private ImageButton person_info;
    EditText searchOrder;
    RecyclerView orderRecView;
    OrderRecAdapter adapterRec;
    ImageView updateOrderList;
    Guide guide;
    private List<Order> orderList = new ArrayList<>();

    public void init() {

        guide = BmobUser.getCurrentUser(Guide.class);
        BmobQuery<Order> bmobQueryPhotographer = new BmobQuery<>();
        bmobQueryPhotographer.addWhereEqualTo("guide", guide);
        bmobQueryPhotographer.findObjects(new FindListener<Order>() {
            @Override
            public void done(List<Order> list, BmobException e) {
                if (e == null) {
                    for (Order o : list) {
                        orderList.add(o);
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(GuideHomePage.this);
                    orderRecView.setLayoutManager(layoutManager);
                    adapterRec = new OrderRecAdapter(orderList, GuideHomePage.this);
                    orderRecView.setAdapter(adapterRec);
                } else {
                    Toast.makeText(GuideHomePage.this, "向导订单失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    protected void onResume() {
        if (UpdateFlag)
        {
            orderList.clear();
            init();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        UpdateFlag = false;
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_home_page);
        getSupportActionBar().hide();// 隐藏ActionBar
        person_info = (ImageButton) findViewById(R.id.person_info);
        searchOrder=(EditText)findViewById(R.id.search_photographer_order);
        orderRecView=(RecyclerView)findViewById(R.id.photographer_order_view);
        updateOrderList=(ImageView)findViewById(R.id.update_orderlist) ;

        UpdateFlag = false;

        init();
        updateOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderList.clear();
                init();
            }
        });
        person_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuideHomePage.this, GuidePersonInfo.class);
                startActivity(intent);
            }
        });
        searchOrder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterRec.getFilter().filter(s);
                Log.d("PakegeActivity","检测文字的变化了");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}