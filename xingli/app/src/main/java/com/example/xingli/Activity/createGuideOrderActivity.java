package com.example.xingli.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.xingli.R;

import java.util.Calendar;
import java.util.Date;

import base.BaseActivity1;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import db.Guide;
import db.Order;
import db.User;

public class createGuideOrderActivity extends BaseActivity1 {
    Guide guide;
    Order order;
    ImageView guideImage;
    TextView guideName;
    TextView guideGender;
    TextView guideAge;
    TextView guideOrderNum;
    EditText guideOrderTime;
    EditText guideOrderLoc;
    TextView guidePrice;
    CheckBox guideCheck;
    TextView guideOrderPrice;
    TextView guideOrderCancelTime;
    CheckBox acceptCheck;
    TextView orderNotice;
    Button payButton;
    BmobDate time;
    User user;
    public void init(){
        user= BmobUser.getCurrentUser(User.class);
        Intent intent=getIntent();
        guide=(Guide) intent.getSerializableExtra("guide");
        guideImage=(ImageView) findViewById(R.id.guide__order_image);
        guideName=(TextView) findViewById(R.id.guide__order_name);
        guideGender=(TextView) findViewById(R.id.guide__order_gender);
        guideAge=(TextView) findViewById(R.id.guide__order_age);
        guideOrderNum=(TextView) findViewById(R.id.guide__order_orderNum);
        guideOrderTime=(EditText) findViewById(R.id.guide_order_time);
        guideOrderLoc=(EditText)findViewById(R.id.guide_order_loc) ;
        guidePrice=(TextView) findViewById(R.id.guide_order_price);
        guideCheck=(CheckBox) findViewById(R.id.guide_check);
        guideOrderPrice=(TextView) findViewById(R.id.guide_order_pay_price);
        guideOrderCancelTime=(TextView) findViewById(R.id.guide_order_cancel_time);
        acceptCheck=(CheckBox) findViewById(R.id.guide_order_check);
        orderNotice=(TextView) findViewById(R.id.guide_order_notice);
        payButton=(Button) findViewById(R.id.guide_order_pay_button);

        if (guide.getHeadImage() != null) {
            showImageOfDateBase(guide.getHeadImage(), guideImage);
        } else {
            guideImage.setImageResource(R.drawable.empty_image);
        }

        guideName.setText(guide.getNickName());
        guideGender.setText("性别："+guide.getGender());
        guideAge.setText("年龄："+String.valueOf(guide.getAge()));
        guideOrderNum.setText("订单数量："+String.valueOf(guide.getMouthNum()));
        Calendar c = Calendar.getInstance();
        time=new BmobDate(new Date(c.get(Calendar.YEAR)-1900,c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)));
        guideOrderTime.setText(c.get(Calendar.YEAR)+"-"+String.valueOf(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH)+"(早上8:00到下午6:00)");
        guidePrice.setText(String.valueOf( guide.getPriceNoUniform())+"元");
        guideOrderPrice.setText("0.0元");
        guideOrderCancelTime.setText(time.getDate().substring(0,10)+ " 24点前取消订单收取订单金额的20%取消费，之后不可取消");
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_guide_order);
        init();
        guideOrderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        createGuideOrderActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year,
                                          int monthOfYear, int dayOfMonth) {
                        guideOrderTime.setText(year+"-"+String.valueOf(monthOfYear+1)+"-"+dayOfMonth+"(最早可存入时间07:00)");
                        time=new BmobDate(new Date(year-1900,monthOfYear,dayOfMonth));
                        guideOrderCancelTime.setText(String.valueOf(year)+"-"+String.valueOf(monthOfYear+1)+"-"+String.valueOf(dayOfMonth-2)
                                +" 24点 ~"+String.valueOf(year)+"-"+String.valueOf(monthOfYear+1)+"-"+String.valueOf(dayOfMonth)+
                                " 24点前取消订单收取订单金额的20%取消费，之后不可取消");
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        guideCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(guideCheck.isChecked()) {
                    guideOrderPrice.setText(String.valueOf(guide.getPriceNoUniform()) + "元");
                }
                else {
                    guideOrderPrice.setText("0.0元");
                }
            }
        });
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (guideOrderPrice.getText().toString().equals( "0.0元"))
                {
                    Toast.makeText(createGuideOrderActivity.this,"请完善订单信息",Toast.LENGTH_SHORT).show();
                }
                if(acceptCheck.isChecked()!=true)
                {
                    Toast.makeText(createGuideOrderActivity.this,"请接受预订须知",Toast.LENGTH_SHORT).show();
                }
                if(guideOrderLoc.getText().toString().equals("请输入您与向导约定好的地点"))
                {
                    Toast.makeText(createGuideOrderActivity.this,"请完善订单信息",Toast.LENGTH_SHORT).show();
                }
                if(acceptCheck.isChecked()==true&&!guideOrderPrice.getText().toString().equals( "0.0元")&&!guideOrderLoc.getText().toString().equals("请输入您与向导约定好的地点"))
                {
                    ProgressDialog progressDialog=new ProgressDialog(createGuideOrderActivity.this);
                    progressDialog.setTitle("正在下单");
                    progressDialog.setTitle("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    Order order = new Order();
                    Calendar c = Calendar.getInstance();
                    order.setCreatTime(new BmobDate(new Date(c.get(Calendar.YEAR)-1900,c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.HOUR),c.get(Calendar.MINUTE),c.get(Calendar.SECOND))));
                    order.setPutTime(time);
                    order.setOrderPrice(guide.getPriceNoUniform());
                    order.setOrderStartLoc(guideOrderLoc.getText().toString());

                    order.setOrderPayState(Order.Pay);
                    order.setOrderAcceptState(Order.notAccept);
                    order.setOrderFinishState(Order.notFinish);

                    order.setOrderType(Order.Guide);
                    order.setUser(user);
                    order.setGuide(guide);
                    order.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(createGuideOrderActivity.this,"下单成功",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intent1=new Intent(createGuideOrderActivity.this,createOrderSuccess.class);
                                intent1.putExtra("type","guide");
                                intent1.putExtra("order",order);
                                startActivity(intent1);
                                startActivity(intent1);
                            } else {
                                Toast.makeText(createGuideOrderActivity.this,"下单失败" + e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }


    public void showImageOfDateBase(BmobFile image, ImageView imageView) {
        String stringImageUri = image.getFileUrl();
        //利用ImageRequest实现图片加载
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //构建ImageRequest 实例
        ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                //给imageView设置图片
                imageView.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //设置一张错误的图片，临时用ic_launcher代替
                Toast.makeText(createGuideOrderActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                if (imageView.equals(guideImage)) {
                    guideImage.setImageResource(R.drawable.headimage);
                } else {
                    guideImage.setImageResource(R.drawable.empty_image);
                }
            }
        });
        requestQueue.add(request);
    }
}