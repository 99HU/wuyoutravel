package com.example.xingli.Activity;

import android.app.DatePickerDialog;
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
import java.util.List;

import base.BaseActivity1;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import db.Order;
import db.Photographer;

public class UpdatePhotographerOrderActivity extends BaseActivity1 {
    Order order;
    TextView PhtotgrapherUpdateName;
    ImageView PhtotgrapherUpdateImage;
    TextView PhtotgrapherUpdateAge;
    TextView PhtotgrapherUpdateGender;
    TextView PhtotgrapherUpdateNum;
    EditText PhtotgrapherUpdatePuttime;
    EditText PhtotgrapherUpdateLoc;
    CheckBox PhtotgrapherUpdateCheck;
    CheckBox PhtotgrapherClothUpdateCheck;
    TextView PhtotgrapherUpdatePrice;
    TextView PhtotgrapherClothUpdatePrice;
    TextView PhtotgrapherUpdateOrderPrice;
    TextView PhtotgrapherUpdateCancelTime;
    CheckBox    PhtotgrapherUpdateNoticeCheck;
    Button PhtotgrapherUpdatePayButton;
    Photographer photographer;
    BmobDate time;
    public void init()
    {
        Intent intent =getIntent();
        order=(Order)intent.getSerializableExtra("order");

        PhtotgrapherUpdateName=(TextView) findViewById(R.id.update_photographer_order_name);
        PhtotgrapherUpdateImage=(ImageView) findViewById(R.id.update_photographer_order_image);
        PhtotgrapherUpdateAge=(TextView) findViewById(R.id.update_photographer_order_age);
        PhtotgrapherUpdateGender=(TextView) findViewById(R.id.update_photographer_order_gender);
        PhtotgrapherUpdateNum=(TextView) findViewById(R.id.update_photographer_order_num);
        PhtotgrapherUpdatePuttime=(EditText) findViewById(R.id.update_photographer_order_time);
        PhtotgrapherUpdateLoc=(EditText) findViewById(R.id.update_photographer_order_loc);
        PhtotgrapherUpdateCheck=(CheckBox) findViewById(R.id.update_photographer_order_check);
        PhtotgrapherClothUpdateCheck=(CheckBox) findViewById(R.id.update_photographer_cloth_order_check);
        PhtotgrapherUpdatePrice=(TextView) findViewById(R.id.update_photographer_order_price);
        PhtotgrapherClothUpdatePrice=(TextView) findViewById(R.id.update_photographer_cloth_order_price);
        PhtotgrapherUpdateOrderPrice=(TextView) findViewById(R.id.update_photographer_price);
        PhtotgrapherUpdateCancelTime=(TextView) findViewById(R.id.update_photographer_cancel_time);
        PhtotgrapherUpdateNoticeCheck=(CheckBox) findViewById(R.id.update_photographer_check);
        PhtotgrapherUpdatePayButton=(Button) findViewById(R.id.update_photographer_order_pay_button);



        photographer=order.getPhotographer();
        BmobQuery<Photographer> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("objectId", photographer.getObjectId());
        bmobQuery.findObjects(new FindListener<Photographer>() {
            @Override
            public void done(List<Photographer> list, BmobException e) {
                if (e == null) {
                    photographer= list.get(0);
                    PhtotgrapherUpdateName.setText(photographer.getNickName());

                    //PhtotgrapherUpdateImage.setImageResource(R.drawable.feng);
                    if (photographer.getHeadImage() != null) {

                        BmobFile postImageFile = photographer.getHeadImage();
                        String stringPostImageUri = postImageFile.getFileUrl();
                        //利用ImageRequest实现图片加载
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        //构建ImageRequest 实例
                        ImageRequest request = new ImageRequest(stringPostImageUri, new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                //给imageView设置图片
                                PhtotgrapherUpdateImage.setImageBitmap(response);
                            }
                        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //设置一张错误的图片，临时用ic_launcher代替
                                Toast.makeText(UpdatePhotographerOrderActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                PhtotgrapherUpdateImage.setImageResource(R.drawable.headimage);
                            }
                        });

                        requestQueue.add(request);

                    } else {
                        PhtotgrapherUpdateImage.setImageResource(R.drawable.headimage);
                    }

                    PhtotgrapherUpdateAge.setText("年龄："+String.valueOf(photographer.getAge()));
                    PhtotgrapherUpdateGender.setText("性别:"+photographer.getGender());
                    PhtotgrapherUpdateNum.setText("服务过"+String.valueOf(photographer.getMouthNum())+"位旅行者");
                    PhtotgrapherUpdatePuttime.setText(order.getPutTime().getDate().substring(0,10)+"(拍摄时间为08:00-06:00)");
                    PhtotgrapherUpdateLoc.setText(order.getOrderStartLoc());
                    PhtotgrapherUpdatePrice.setText(String.valueOf(photographer.getPriceNoUniform())+"元");
                    PhtotgrapherClothUpdatePrice.setText(String.valueOf(photographer.getPriceIncludeUniform())+"元");
                    PhtotgrapherUpdateOrderPrice.setText(String.valueOf(order.getOrderPrice())+"元");
                    PhtotgrapherUpdateCancelTime.setText(order.getPutTime().getDate().substring(0,10)+"前取消订单需要支付20%的取消费，之后不可取消");
                    PhtotgrapherUpdateNoticeCheck.setChecked(true);
                    if (order.getPhotographerType().equals(Photographer.PhotographerOnly))
                    {
                        PhtotgrapherUpdateCheck.setChecked(true);
                    }
                    if (order.getPhotographerType().equals(Photographer.PhotographerAndCloth))
                    {
                        PhtotgrapherClothUpdateCheck.setChecked(true);
                    }
                } else {
                    Toast.makeText(UpdatePhotographerOrderActivity.this,"查询摄影师失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_photographer_order);
        init();
        PhtotgrapherUpdatePuttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UpdatePhotographerOrderActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year,
                                          int monthOfYear, int dayOfMonth) {
                        PhtotgrapherUpdatePuttime.setText(year+"-"+String.valueOf(monthOfYear+1)+"-"+dayOfMonth+"(最早可存入时间07:00)");
                        time=new BmobDate(new Date(year-1900,monthOfYear,dayOfMonth));
                        PhtotgrapherUpdateCancelTime.setText(String.valueOf(year)+"-"+String.valueOf(monthOfYear+1)+"-"+String.valueOf(dayOfMonth-2)
                                +" 24点 ~"+String.valueOf(year)+"-"+String.valueOf(monthOfYear+1)+"-"+String.valueOf(dayOfMonth)+
                                " 24点前取消订单收取订单金额的20%取消费，之后不可取消");
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        PhtotgrapherUpdateCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PhtotgrapherUpdateCheck.isChecked()) {
                    PhtotgrapherClothUpdateCheck.setChecked(false);
                    PhtotgrapherUpdateOrderPrice.setText(String.valueOf(photographer.getPriceNoUniform()) + "元");
                }
                else {
                    PhtotgrapherUpdateOrderPrice.setText("0.0元");
                }
            }
        });
        PhtotgrapherClothUpdateCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PhtotgrapherClothUpdateCheck.isChecked()) {
                    PhtotgrapherUpdateCheck.setChecked(false);
                    PhtotgrapherUpdateOrderPrice.setText(String.valueOf(photographer.getPriceIncludeUniform())+"元");
                }
                else {
                    PhtotgrapherUpdateOrderPrice.setText("0.0元");
                }
            }
        });
        PhtotgrapherUpdatePayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PhtotgrapherUpdateOrderPrice.getText().toString().equals( "0.0元"))
                {
                    Toast.makeText(UpdatePhotographerOrderActivity.this,"请完善订单信息",Toast.LENGTH_SHORT).show();
                }
                if(PhtotgrapherUpdateNoticeCheck.isChecked()!=true)
                {
                    Toast.makeText(UpdatePhotographerOrderActivity.this,"请接受预订须知",Toast.LENGTH_SHORT).show();
                }
                if(PhtotgrapherUpdateLoc.getText().toString().equals("请输入您与摄影师约定好的地点"))
                {
                    Toast.makeText(UpdatePhotographerOrderActivity.this,"请完善订单信息",Toast.LENGTH_SHORT).show();
                }
                if(PhtotgrapherUpdateNoticeCheck.isChecked()==true&&!PhtotgrapherUpdateOrderPrice.getText().toString().equals( "0.0元")&&!PhtotgrapherUpdateLoc.getText().toString().equals("请输入您与摄影师约定好的地点"))
                {
                    Calendar c = Calendar.getInstance();
                   //更新数据库的操作
                    order.setPutTime(time);
                    order.setOrderStartLoc(PhtotgrapherUpdateLoc.getText().toString());
                    if(PhtotgrapherUpdateCheck.isChecked())
                    {
                        order.setPhotographerType(Photographer.PhotographerOnly);
                        order.setOrderPrice(photographer.getPriceNoUniform());
                    }
                    else
                    {
                        order.setOrderPrice(photographer.getPriceIncludeUniform());
                        order.setPhotographerType(Photographer.PhotographerAndCloth);
                    }
                    order.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Intent intent1=new Intent(UpdatePhotographerOrderActivity.this,createOrderSuccess.class);
                                intent1.putExtra("type","photographer");
                                intent1.putExtra("order",order);
                                startActivity(intent1);
                            } else {
                                Toast.makeText(UpdatePhotographerOrderActivity.this, "更改失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }
}