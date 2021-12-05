package com.example.xingli.Activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.xingli.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import base.BaseActivity1;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import db.Order;
import db.Post;

public class upDateActivity extends BaseActivity1 {
    Order order;
    Post startPost;
    Post endPost;
    public TextView postStartName;
    public TextView postStartLoc;
    public Button updatePostStart;
    public TextView  postEndName;
    public TextView  postEndLoc;
    public Button updatePostEnd;
    public EditText putTime;
    public EditText getTime;
    public TextView postBigNum;
    public TextView postSmallNum;
    public TextView cancalTime;
    public TextView orderNotice;
    public AmountView amountBig;
    public AmountView amountSmall;
    public TextView orderPrice;
    public BmobDate start;
    public BmobDate end;
    public CheckBox checkOrderNotice;
    public Button payButton;

    public Date putDate;
    public Date getDate;

    public Date startDate;
    public Date endDate;
    public void init() throws ParseException {
        postStartName=(TextView) findViewById(R.id.update_start_post_name);
        postStartLoc=(TextView) findViewById(R.id.update_start_post_loc);
        updatePostStart=(Button) findViewById(R.id.update_order_start_post);
        postEndName=(TextView) findViewById(R.id.update_end_post_name);
        postEndLoc=(TextView) findViewById(R.id.update_end_post_loc);
        updatePostEnd=(Button) findViewById(R.id.update_order_end_post);
        putTime=(EditText) findViewById(R.id.update_order_puttime);
        getTime=(EditText) findViewById(R.id.update_order_gettime);
        postBigNum=(TextView) findViewById(R.id.updateorder_big_num);
        postSmallNum=(TextView) findViewById(R.id.updateorder_small_num);
        cancalTime=(TextView) findViewById(R.id.update_order_canceltime);
        orderNotice=(TextView) findViewById(R.id.update_order_notice);
        amountBig=(AmountView) findViewById(R.id.update_amount_big_view);
        amountSmall=(AmountView) findViewById(R.id.update_amount_small_view);
        orderPrice=(TextView)findViewById(R.id.update_order_price) ;
        checkOrderNotice=(CheckBox) findViewById(R.id.update_order_check_rules);
        payButton=(Button) findViewById(R.id.update_order_pay_button);

        Intent intent=getIntent();
        order=(Order) intent.getSerializableExtra("order");
        Log.d("TAG", order.getObjectId());
        Toast.makeText(upDateActivity.this, order.getObjectId(), Toast.LENGTH_SHORT).show();


        BmobQuery<Post> bmobQueryStart = new BmobQuery<>();
        bmobQueryStart.addWhereEqualTo("objectId", order.getEndPost().getObjectId());
        BmobQuery<Post> bmobQueryEnd = new BmobQuery<>();
        bmobQueryEnd.addWhereEqualTo("objectId", order.getStartPost().getObjectId());
        List<BmobQuery<Post>> queries = new ArrayList<BmobQuery<Post>>();
        queries.add(bmobQueryStart);
        queries.add(bmobQueryEnd);
        BmobQuery<Post> mainQuery = new BmobQuery<>();
        mainQuery.or(queries);
        mainQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if (e == null) {
                    if (list.get(0).getObjectId().equals(order.getStartPost().getObjectId())) {
                        startPost = list.get(0);
                        endPost = list.get(1);
                    } else {
                        startPost = list.get(1);
                        endPost = list.get(0);
                    }

                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        getDate = sf.parse(order.getPutTime().getDate());
                        putDate = sf.parse(order.getGetTime().getDate());
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }

                    amountBig.setGoods_storage(endPost.getLargerNum()+order.getOrderBigNum());
                    amountSmall.setGoods_storage(endPost.getSmallNum()+order.getOrderSmallNum());
                    postStartName.setText(startPost.getPostName());
                    postStartLoc.setText(startPost.getPostLoc());
                    postEndName.setText(endPost.getPostName());
                    postEndLoc.setText(endPost.getPostLoc());
                    putTime.setText(order.getPutTime().getDate().substring(0,10)+"(最早可存入时间07:00)");
                    getTime.setText(order.getGetTime().getDate().substring(0,10)+"(最晚可取回时间23:30)");
                    postBigNum.setText("可存余量："+String.valueOf(startPost.getLargerNum()));
                    postSmallNum.setText("可存余量"+String.valueOf(endPost.getSmallNum()));
                    cancalTime.setText(order.getGetTime().getDate().substring(0,10)+"  24点前取消订单收取订单金额20%取消费，之后不可取消");
                    SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");

                    start = order.getPutTime();
                    end = order.getGetTime();
                    Log.d("TAG:start", start.getDate());
                    Log.d("TAG:end", end.getDate());
                    amountBig.setAmount(order.getOrderBigNum());
                    amountSmall.setAmount(order.getOrderSmallNum());
                    orderPrice.setText(String.valueOf(order.getOrderPrice())+"元");
                    checkOrderNotice.setChecked(true);



                } else {
                    Toast.makeText(upDateActivity.this, "Post相关信息查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_date);
        try {
            init();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        putTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        upDateActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year,
                                          int monthOfYear, int dayOfMonth) {
                        putTime.setText(year+"-"+String.valueOf(monthOfYear+1)+"-"+dayOfMonth+"(最早可存入时间07:00)");
                        start=new BmobDate(new Date(year-1900,monthOfYear,dayOfMonth));
                        cancalTime.setText(year+"-"+String.valueOf(monthOfYear+1)+"-"+dayOfMonth+"  24点前取消订单收取订单金额20%取消费，之后不可取消");
                        Log.d("TAG:start2", start.getDate());
                        if (!getTime.getText().toString().isEmpty()) {
                            double money = moneyCal();
                            orderPrice.setText(String.valueOf(money) + "元");
                        }
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        getTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        upDateActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year,
                                          int monthOfYear, int dayOfMonth) {

                        getTime.setText(year+"-"+String.valueOf(monthOfYear+1)+"-"+dayOfMonth+"(最晚可取回时间23:30)");
                        end=new BmobDate(new Date(year-1900,monthOfYear,dayOfMonth));
                        Log.d("TAG:end2", end.getDate());
                        double money= 0;

                            money = moneyCal();

                        orderPrice.setText(String.valueOf(money)+"元");
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        orderNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(upDateActivity.this,NoticeActivity.class);
                startActivity(intent1);
            }
        });
        amountBig.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                if (!getTime.getText().toString().isEmpty()) {
                    double money = moneyCal();
                    orderPrice.setText(String.valueOf(money) + "元");
                }
            }
        });
        amountSmall.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                if (!getTime.getText().toString().isEmpty()) {
                    double money = moneyCal();
                    orderPrice.setText(String.valueOf(money) + "元");
                }
            }
        });
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderPrice.getText().toString().equals( "0.0元")||orderPrice.getText().toString().equals( "0元"))
                {
                    Toast.makeText(upDateActivity.this,"请完善订单信息",Toast.LENGTH_SHORT).show();
                }
                if(checkOrderNotice.isChecked()!=true)
                {
                    Toast.makeText(upDateActivity.this,"请接受预订须知",Toast.LENGTH_SHORT).show();
                }
                if(checkOrderNotice.isChecked()==true&&!orderPrice.getText().toString().equals( "0.0元")&&!orderPrice.getText().toString().equals( "0元"))
                {
                    order.setPutTime(start);
                    order.setGetTime(end);
                    order.setOrderBigNum(Integer.parseInt(amountBig.getAmount()));
                    order.setOrderSmallNum(Integer.parseInt(amountSmall.getAmount()));
                    order.setOrderPrice(moneyCal());
                    order.setOrderAcceptState(Order.notAccept);
                    order.setStartPost(startPost);
                    order.setEndPost(endPost);
                    order.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(upDateActivity.this, "更改成功", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(upDateActivity.this, createOrderSuccess.class);
                                intent1.putExtra("order", order);
                                intent1.putExtra("type", "post");
                                startActivity(intent1);
                            } else {
                                Toast.makeText(upDateActivity.this, "更改失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
//                    order.setProviderStartId();
//                    Order order=new Order( 1,1, post.getPostId(),startPost.getPostId(),new Date(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.HOUR),c.get(Calendar.MINUTE),c.get(Calendar.SECOND)),
//                            start,end,Integer.parseInt(amountBig.getAmount()),Integer.parseInt(amountSmall.getAmount()),
//                            moneyCal(),startPost.getPostLoc(),post.getPostLoc(), 12314564,Order.Pay,Order.notAccept,Order.notFinish,Order.Post,startPost,post);
//                    order.setOrderPrice(moneyCal());
//                    order.setStartPost(startPost); order.setEndPost(endPost);
                }
            }
        });
        updatePostEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(upDateActivity.this,chooseStartPostActivity.class);
                startActivityForResult(intent,3);
            }
        });
        updatePostStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(upDateActivity.this,chooseStartPostActivity.class);
                startActivityForResult(intent,4);
            }
        });
    }
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 4:
                if (resultCode == RESULT_OK) {
                    startPost=(Post) data.getSerializableExtra("poststart");
                    postStartName.setText(startPost.getPostName());
                    postStartLoc.setText(startPost.getPostLoc());

                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    endPost=(Post) data.getSerializableExtra("poststart");
                    postEndName.setText(endPost.getPostName());
                    postEndLoc.setText(endPost.getPostLoc());
                    amountBig.setGoods_storage(endPost.getLargerNum()+order.getOrderBigNum());
                    amountSmall.setGoods_storage(endPost.getSmallNum()+order.getOrderSmallNum());
                    orderPrice.setText(String.valueOf(moneyCal())+"元");
                }
            default:
        }

    }
    public int dayCal()
    {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            startDate = sf.parse(start.getDate());
            endDate = sf.parse(end.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int days = (int) ((endDate.getTime() - startDate.getTime()) / (1000*3600*24));

        Log.d("day", String.valueOf(days));
        if (startDate.getTime()==endDate.getTime())
            return 1;
        else
            return days;

    }
    public double moneyCal()
    {
        int day=dayCal();
        double money;
        money=day*(Integer.parseInt(amountBig.getAmount())*endPost.getLargePrice()+Integer.parseInt(amountSmall.getAmount())*endPost.getSmallPrice());
        Log.d("money",String.valueOf(money));
        return money;
    }
}