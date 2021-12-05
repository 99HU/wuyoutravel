package com.example.xingli.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.xingli.R;

import java.util.List;

import base.BaseActivity1;
import base.UniteApp;
import db.Order;

public class createOrderSuccess extends BaseActivity1 {
    public TextView orderPrcice;
    public Button startMyorderActivtiy;
    public Order order;
    TextView orderNotice;
    TextView orderNotice1;
    TextView orderNotice2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order_success);
        orderPrcice=(TextView)findViewById(R.id.order_price);
        startMyorderActivtiy=(Button)findViewById(R.id.goto_order);
        orderNotice=(TextView)findViewById(R.id.order_notice0);
        orderNotice1=(TextView)findViewById(R.id.order_notice1);
        Intent intent=getIntent();
        order=(Order) intent.getSerializableExtra("order");
        String type=intent.getStringExtra("type");
        if (type.equals("post"))
        {
            orderNotice1.setVisibility(View.INVISIBLE);
        }
        if(type.equals("photographer"))
        {
            orderNotice.setVisibility(View.INVISIBLE);

        }
        if (type.equals("guide"))
        {
            orderNotice.setVisibility(View.INVISIBLE);
        }
        orderPrcice.setText("支付金额： "+order.getOrderPrice()+"元");
        startMyorderActivtiy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                MainActivity.toOrder=true;
                List<Activity> activities = ((UniteApp)getApplication()).getActivities();
                for(Activity activity:activities){
                    if(activity instanceof MainActivity) {
                        continue;
                    }
                        activity.finish();
                }
            }

        });
    }
}