package com.example.poststation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.http.I;
import cn.bmob.v3.listener.FindListener;


public class PostHomePage extends AppCompatActivity {
    public static boolean UpdateFlag=false;
    EditText searchOrder;
    RecyclerView orderRecView;
    OrderRecAdapter adapterRec;
    ImageView updateOrderList;
    Post post;
    Manager manager;

    private List<Order> orderList = new ArrayList<>();
    public void update(){
        BmobQuery<Order> bmobQueryStart = new BmobQuery<>();
        bmobQueryStart.addWhereEqualTo("startPost", post);
        BmobQuery<Order> bmobQueryEnd = new BmobQuery<>();
        bmobQueryEnd.addWhereEqualTo("endPost", post);
        List<BmobQuery<Order>> queries = new ArrayList<BmobQuery<Order>>();
        queries.add(bmobQueryStart);
        queries.add(bmobQueryEnd);
        BmobQuery<Order> mainQuery = new BmobQuery<>();
        mainQuery.or(queries);
        mainQuery.findObjects(new FindListener<Order>() {
            @Override
            public void done(List<Order> list, BmobException e) {
                if (e == null) {

                    for (Order o : list) {
                        orderList.add(o);
                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(PostHomePage.this);
                    orderRecView.setLayoutManager(layoutManager);
                    adapterRec = new OrderRecAdapter(orderList, PostHomePage.this);
                    orderRecView.setAdapter(adapterRec);
                } else {
                    Toast.makeText(PostHomePage.this, "从订单获取驿站失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void init() {
        searchOrder = (EditText) findViewById(R.id.search_post_order);
        orderRecView = (RecyclerView) findViewById(R.id.post_order_view);
        updateOrderList = (ImageView) findViewById(R.id.update_orderlist);

        manager = BmobUser.getCurrentUser(Manager.class);
        BmobQuery<Post> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("manager", manager);
        bmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    post = list.get(0);
                    BmobQuery<Order> bmobQueryStart = new BmobQuery<>();
                    bmobQueryStart.addWhereEqualTo("startPost", post);
                    BmobQuery<Order> bmobQueryEnd = new BmobQuery<>();
                    bmobQueryEnd.addWhereEqualTo("endPost", post);
                    List<BmobQuery<Order>> queries = new ArrayList<BmobQuery<Order>>();
                    queries.add(bmobQueryStart);
                    queries.add(bmobQueryEnd);
                    BmobQuery<Order> mainQuery = new BmobQuery<>();
                    mainQuery.or(queries);
                    mainQuery.findObjects(new FindListener<Order>() {
                        @Override
                        public void done(List<Order> list, BmobException e) {
                            if (e == null) {

                                for (Order o : list) {
                                    if (o.getOrderType().equals(Order.Post))
                                        orderList.add(o);
                                }
                                LinearLayoutManager layoutManager = new LinearLayoutManager(PostHomePage.this);
                                orderRecView.setLayoutManager(layoutManager);
                                adapterRec = new OrderRecAdapter(orderList, PostHomePage.this);
                                orderRecView.setAdapter(adapterRec);
                            } else {
                                Toast.makeText(PostHomePage.this, "从订单获取驿站失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(PostHomePage.this, "从管理者获取驿站失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mine_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.person_info:
                Intent intent1 = new Intent(PostHomePage.this, PostPersonInfo.class);
                startActivity(intent1);
                break;
            case R.id.station_info:
                Intent intent2 = new Intent(PostHomePage.this, PostStationInfo.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        if (UpdateFlag)
        {
            orderList.clear();
            update();
        }
        super.onResume();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_home_page);
        init();
        updateOrderList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderList.clear();
                update();
                Toast.makeText(PostHomePage.this,"点击了",Toast.LENGTH_SHORT).show();

            }
        });
        searchOrder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterRec.getFilter().filter(s);
                Log.d("PakegeActivity", "检测文字的变化了");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}