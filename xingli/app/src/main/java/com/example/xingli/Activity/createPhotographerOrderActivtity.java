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
import db.Order;
import db.Photographer;
import db.User;

public class createPhotographerOrderActivtity extends BaseActivity1 {
    User user;
    Photographer photographer;
    Order order;
    ImageView photographerImage;
    TextView photographerName;
    TextView photographerGender;
    TextView photographerAge;
    TextView photographerOrderNum;
    EditText photographerOrderTime;
    EditText photographerOrderLoc;
    TextView photographerPrice;
    TextView photographerClothPrice;
    CheckBox photographerCheck;
    CheckBox photographerClothCheck;
    TextView photographerOrderPrice;
    TextView photographerOrderCancelTime;
    CheckBox acceptCheck;
    TextView orderNotice;
    Button payButton;
    BmobDate time;

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
                Toast.makeText(createPhotographerOrderActivtity.this, error.toString(), Toast.LENGTH_SHORT).show();
                if (imageView.equals(photographerImage)) {
                    photographerImage.setImageResource(R.drawable.headimage);
                } else {
                    photographerImage.setImageResource(R.drawable.empty_image);
                }
            }
        });
        requestQueue.add(request);
    }

    public void init(){
        user=  BmobUser.getCurrentUser(User.class);
        Intent intent=getIntent();
        photographer=(Photographer) intent.getSerializableExtra("photographer");
        photographerImage=(ImageView) findViewById(R.id.photographer__order_image);
        photographerName=(TextView) findViewById(R.id.photographer__order_name);
        photographerGender=(TextView) findViewById(R.id.photographer__order_gender);
        photographerAge=(TextView) findViewById(R.id.photographer__order_age);
        photographerOrderNum=(TextView) findViewById(R.id.photographer__order_orderNum);
        photographerOrderTime=(EditText) findViewById(R.id.photographer_order_time);
        photographerOrderLoc=(EditText)findViewById(R.id.photographer_order_loc) ;
        photographerPrice=(TextView) findViewById(R.id.photographer_order_price);
        photographerClothPrice=(TextView) findViewById(R.id.photographer_cloth_order_price);
        photographerCheck=(CheckBox) findViewById(R.id.photographer_check);
        photographerClothCheck=(CheckBox) findViewById(R.id.photographer_cloth_check);
        photographerOrderPrice=(TextView) findViewById(R.id.photographer_order_pay_price);
        photographerOrderCancelTime=(TextView) findViewById(R.id.photographer_order_cancel_time);
        acceptCheck=(CheckBox) findViewById(R.id.photographer_order_check);
        orderNotice=(TextView) findViewById(R.id.photographer_order_notice);
        payButton=(Button) findViewById(R.id.photographer_order_pay_button);


        if (photographer.getHeadImage() != null) {
            showImageOfDateBase(photographer.getHeadImage(), photographerImage);
        } else {
            photographerImage.setImageResource(R.drawable.empty_image);
        }

        photographerName.setText(photographer.getNickName());
        photographerGender.setText("性别："+photographer.getGender());
        photographerAge.setText("年龄："+String.valueOf(photographer.getAge()));
        photographerOrderNum.setText("订单数量："+String.valueOf(photographer.getMouthNum()));
        Calendar c = Calendar.getInstance();
        time=new BmobDate(new Date(c.get(Calendar.YEAR)-1900,c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)));
        photographerOrderTime.setText(c.get(Calendar.YEAR)+"-"+String.valueOf(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH)+"(早上8:00到下午6:00)");
        photographerPrice.setText(String.valueOf( photographer.getPriceNoUniform())+"元");
        photographerClothPrice.setText(String.valueOf( photographer.getPriceIncludeUniform())+"元");
        photographerOrderPrice.setText("0.0元");
        photographerOrderCancelTime.setText(time.getDate().substring(0,10)+ " 24点前取消订单收取订单金额的20%取消费，之后不可取消");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_photographer_order_activtity);
        init();
        photographerOrderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        createPhotographerOrderActivtity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year,
                                          int monthOfYear, int dayOfMonth) {
                        photographerOrderTime.setText(year+"-"+String.valueOf(monthOfYear+1)+"-"+dayOfMonth+"(最早可存入时间07:00)");
                        time=new BmobDate(new Date(year-1900,monthOfYear,dayOfMonth));
                        photographerOrderCancelTime.setText(String.valueOf(year)+"-"+String.valueOf(monthOfYear+1)+"-"+String.valueOf(dayOfMonth-2)
                                +" 24点 ~"+String.valueOf(year)+"-"+String.valueOf(monthOfYear+1)+"-"+String.valueOf(dayOfMonth)+
                                " 24点前取消订单收取订单金额的20%取消费，之后不可取消");
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        photographerCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(photographerCheck.isChecked()) {
                    photographerClothCheck.setChecked(false);
                    photographerOrderPrice.setText(String.valueOf(photographer.getPriceNoUniform()) + "元");
                }
                else {
                    photographerOrderPrice.setText("0.0元");
                }
            }
        });
        photographerClothCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(photographerClothCheck.isChecked()) {
                    photographerCheck.setChecked(false);
                    photographerOrderPrice.setText(String.valueOf(photographer.getPriceIncludeUniform())+"元");
                }
                else {
                    photographerOrderPrice.setText("0.0元");
                }
            }
        });
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photographerOrderPrice.getText().toString().equals( "0.0元"))
                {
                    Toast.makeText(createPhotographerOrderActivtity.this,"请完善订单信息",Toast.LENGTH_SHORT).show();
                }
                if(acceptCheck.isChecked()!=true)
                {
                    Toast.makeText(createPhotographerOrderActivtity.this,"请接受预订须知",Toast.LENGTH_SHORT).show();
                }
                if(photographerOrderLoc.getText().toString().equals("请输入您与摄影师约定好的地点"))
                {
                    Toast.makeText(createPhotographerOrderActivtity.this,"请完善订单信息",Toast.LENGTH_SHORT).show();
                }
                if(acceptCheck.isChecked()==true&&!photographerOrderPrice.getText().toString().equals( "0.0元")&&!photographerOrderLoc.getText().toString().equals("请输入您与摄影师约定好的地点"))
                {
                    ProgressDialog progressDialog=new ProgressDialog(createPhotographerOrderActivtity.this);
                    progressDialog.setTitle("正在下单");
                    progressDialog.setTitle("Loading...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    Calendar c = Calendar.getInstance();
                    Order order = new Order();
//                    Order order = new Order(new Date(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.HOUR),c.get(Calendar.MINUTE),c.get(Calendar.SECOND)),
//                            start, end, Integer.parseInt(amountBig.getAmount()), Integer.parseInt(amountSmall.getAmount()),
//                             moneyCal(), startPost.getPostLoc(), post.getPostLoc(), 12314564, Order.Pay, Order.notAccept, Order.notFinish, Order.Post, startPost, post, user);
                    order.setCreatTime(new BmobDate(new Date(c.get(Calendar.YEAR)-1900,c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.HOUR),c.get(Calendar.MINUTE),c.get(Calendar.SECOND))));
                    order.setPutTime(time);
                    if(photographerCheck.isChecked())
                    {
                        order.setPhotographerType(Photographer.PhotographerOnly);
                        order.setOrderPrice(photographer.getPriceNoUniform());
                    }
                    else
                    {
                        order.setOrderPrice(photographer.getPriceIncludeUniform());
                        order.setPhotographerType(Photographer.PhotographerAndCloth);
                    }
                    order.setOrderStartLoc(photographerOrderLoc.getText().toString());
                    order.setOrderPayState(Order.Pay);
                    order.setOrderAcceptState(Order.notAccept);
                    order.setOrderFinishState(Order.notFinish);
                    order.setOrderType(Order.PhotoGrapher);
                    order.setUser(user);
                    order.setPhotographer(photographer);
                    order.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Toast.makeText(createPhotographerOrderActivtity.this,"下单成功",Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intent1=new Intent(createPhotographerOrderActivtity.this,createOrderSuccess.class);
                                intent1.putExtra("type","photographer");
                                intent1.putExtra("order",order);
                                startActivity(intent1);
                            } else {
                                Toast.makeText(createPhotographerOrderActivtity.this,"下单失败" + e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
//                    order=new Order(1,1,1,new Date(c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH),c.get(Calendar.HOUR),c.get(Calendar.MINUTE),c.get(Calendar.SECOND)),
//                            new Date(time.getYear(),time.getMonth(),time.getDay()),Order.PhotographerOnly,photographer.getPhotographerPrice(),
//                            photographerOrderLoc.getText().toString(),Order.Pay,Order.notAccept,Order.notAccept,Order.PhotoGrapher,photographer);

                }
            }
        });
    }
}