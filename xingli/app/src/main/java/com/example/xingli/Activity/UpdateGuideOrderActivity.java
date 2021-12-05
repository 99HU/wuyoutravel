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

public class UpdateGuideOrderActivity extends BaseActivity1 {
    Order order;
    TextView GuideUpdateName;
    ImageView GuideUpdateImage;
    TextView GuideUpdateAge;
    TextView GuideUpdateGender;
    TextView GuideUpdateNum;
    EditText GuideUpdatePuttime;
    EditText GuideUpdateLoc;
    CheckBox GuideUpdateCheck;
    TextView GuideUpdatePrice;
    TextView GuideUpdateOrderPrice;
    TextView GuideUpdateCancelTime;
    CheckBox GuideUpdateNoticeCheck;
    Button GuideUpdatePayButton;
    BmobDate time;
    db.Guide guide;

    BmobDate putDate;

    public void init()
    {
        Intent intent =getIntent();
        order=(Order)intent.getSerializableExtra("order");
     GuideUpdateName=(TextView) findViewById(R.id.update_guide_order_name);
     GuideUpdateImage=(ImageView) findViewById(R.id.update_guide_order_image);
     GuideUpdateAge=(TextView) findViewById(R.id.update_guide_order_age);
     GuideUpdateGender=(TextView) findViewById(R.id.update_guide_order_gender);
     GuideUpdateNum=(TextView) findViewById(R.id.update_guide_order_num);
     GuideUpdatePuttime=(EditText) findViewById(R.id.update_guide_order_time);
     GuideUpdateLoc=(EditText) findViewById(R.id.update_guide_order_loc);
     GuideUpdateCheck=(CheckBox) findViewById(R.id.update_guide_order_check);
       
     GuideUpdatePrice=(TextView) findViewById(R.id.update_guide_order_price);
     GuideUpdateOrderPrice=(TextView) findViewById(R.id.update_guide_price);
     GuideUpdateCancelTime=(TextView) findViewById(R.id.update_guide_cancel_time);
     GuideUpdateNoticeCheck=(CheckBox) findViewById(R.id.update_guide_check);
     GuideUpdatePayButton=(Button) findViewById(R.id.update_guide_order_pay_button);

     guide=order.getguide();
        BmobQuery<db.Guide> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("objectId", guide.getObjectId());
        bmobQuery.findObjects(new FindListener<db.Guide>() {
            @Override
            public void done(List<db.Guide> list, BmobException e) {
                if (e == null) {
                    guide= list.get(0);

                    //PhtotgrapherUpdateImage.setImageResource(R.drawable.feng);
                    if (guide.getHeadImage() != null) {

                        BmobFile postImageFile = guide.getHeadImage();
                        String stringPostImageUri = postImageFile.getFileUrl();
                        //利用ImageRequest实现图片加载
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        //构建ImageRequest 实例
                        ImageRequest request = new ImageRequest(stringPostImageUri, new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                //给imageView设置图片
                                GuideUpdateImage.setImageBitmap(response);
                            }
                        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //设置一张错误的图片，临时用ic_launcher代替
                                Toast.makeText(UpdateGuideOrderActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                GuideUpdateImage.setImageResource(R.drawable.headimage);
                            }
                        });

                        requestQueue.add(request);

                    } else {
                        GuideUpdateImage.setImageResource(R.drawable.headimage);
                    }

                    GuideUpdateName.setText(guide.getNickName());
                    //    GuideUpdateImage.setImageResource(Guide.getGuideImageId());
                    GuideUpdateAge.setText("年龄："+String.valueOf(guide.getAge()));
                    GuideUpdateGender.setText("性别:"+guide.getGender());
                    GuideUpdateNum.setText("服务过"+String.valueOf(guide.getMouthNum())+"位旅行者");
                    GuideUpdatePuttime.setText(order.getPutTime().getDate().substring(0,10)+"(拍摄时间为08:00-06:00)");
                    GuideUpdateLoc.setText(order.getOrderStartLoc());
                    GuideUpdateCheck.setChecked(true);
                    GuideUpdatePrice.setText(String.valueOf(guide.getPriceNoUniform())+"元");
                    GuideUpdateOrderPrice.setText(String.valueOf(order.getOrderPrice())+"元");
                    GuideUpdateCancelTime.setText(order.getPutTime().getDate().substring(0,10)+"内取消订单需要支付20%的取消费，之后不可取消");
                    GuideUpdateNoticeCheck.setChecked(true);
                } else {
                    Toast.makeText(UpdateGuideOrderActivity.this,"查询摄影师失败",Toast.LENGTH_SHORT).show();
                }
            }
        });





    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_guide_order);
        init();
        GuideUpdatePuttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        UpdateGuideOrderActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker arg0, int year,
                                          int monthOfYear, int dayOfMonth) {
                        GuideUpdatePuttime.setText(year+"-"+String.valueOf(monthOfYear+1)+"-"+dayOfMonth+"(最早可存入时间07:00)");
                        time=new BmobDate(new Date(year-1900,monthOfYear,dayOfMonth));
                        GuideUpdateCancelTime.setText(String.valueOf(year)+"-"+String.valueOf(monthOfYear+1)+"-"+String.valueOf(dayOfMonth-2)
                                +" 24点 ~"+String.valueOf(year)+"-"+String.valueOf(monthOfYear+1)+"-"+String.valueOf(dayOfMonth)+
                                " 24点前取消订单收取订单金额的20%取消费，之后不可取消");
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
        GuideUpdateCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GuideUpdateCheck.isChecked()) {
                    GuideUpdateOrderPrice.setText(String.valueOf(guide.getPriceNoUniform()) + "元");
                }
                else {
                    GuideUpdateOrderPrice.setText("0.0元");
                }
            }
        });
        GuideUpdatePayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GuideUpdateOrderPrice.getText().toString().equals( "0.0元"))
                {
                    Toast.makeText(UpdateGuideOrderActivity.this,"请完善订单信息",Toast.LENGTH_SHORT).show();
                }
                if(GuideUpdateNoticeCheck.isChecked()!=true)
                {
                    Toast.makeText(UpdateGuideOrderActivity.this,"请接受预订须知",Toast.LENGTH_SHORT).show();
                }
                if(GuideUpdateLoc.getText().toString().equals("请输入您与摄影师约定好的地点"))
                {
                    Toast.makeText(UpdateGuideOrderActivity.this,"请完善订单信息",Toast.LENGTH_SHORT).show();
                }
                if(GuideUpdateNoticeCheck.isChecked()==true&&!GuideUpdateOrderPrice.getText().toString().equals( "0.0元")&&!GuideUpdateLoc.getText().toString().equals("请输入您与摄影师约定好的地点"))
                {
                    Calendar c = Calendar.getInstance();
                    //更新数据库的操作
                    order.setPutTime(time);
                    order.setOrderStartLoc(GuideUpdateLoc.getText().toString());
                    order.setOrderPrice(guide.getPriceNoUniform());
                    order.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Intent intent1=new Intent(UpdateGuideOrderActivity.this,createOrderSuccess.class);
                                intent1.putExtra("type","guide");
                                intent1.putExtra("order",order);
                                startActivity(intent1);
                            } else {
                                Toast.makeText(UpdateGuideOrderActivity.this, "更改失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }
}