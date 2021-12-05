package com.example.photographer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class OrderDetailActivity extends AppCompatActivity {
    Order order;
    TextView photographerOrderDetailName;
    ImageView photographerOrderDetailImage;
    Button photographerOrderDetailCall;
    TextView photographerOrderDetailId;
    TextView photographerOrderDetailCreateTime;
    TextView photographerOrderDetailPrice;
    TextView photographerOrderDetailPuttime;
    TextView photographerOrderDetailLoc;
    TextView photographerOrderDetailPackege;
    TextView photographerOrderDetailCancelTime;
    Button photographerOrderDetailAccept;
    User user;

    DialogInterface.OnClickListener AcceptListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    order.setOrderAcceptState(Order.Accept);
                    order.update(order.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(OrderDetailActivity.this,"接受成功",Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(OrderDetailActivity.this,"接受失败",Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    DialogInterface.OnClickListener UserListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    if (ActivityCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(OrderDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        call(user.getMobilePhoneNumber());
                    }
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    public void init(){
        photographerOrderDetailName=(TextView) findViewById(R.id.photographer_order_detail_name);
        photographerOrderDetailImage=(ImageView) findViewById(R.id.photographer_order_detail_image);
        photographerOrderDetailCall=(Button) findViewById(R.id.photographer_order_detail_call);
        photographerOrderDetailId=(TextView) findViewById(R.id.photographer_order_detail_id);
        photographerOrderDetailCreateTime=(TextView) findViewById(R.id.photographer_order_detail_createtime);
        photographerOrderDetailPrice=(TextView) findViewById(R.id.photographer_order_detail_price);
        photographerOrderDetailPuttime=(TextView) findViewById(R.id.photographer_order_detail_puttime);
        photographerOrderDetailLoc=(TextView) findViewById(R.id.photographer_order_detail_loc);
        photographerOrderDetailPackege=(TextView) findViewById(R.id.photographer_order_detail_package);
        photographerOrderDetailCancelTime=(TextView) findViewById(R.id.photographer_order_detail_cancletime);
        photographerOrderDetailAccept=(Button) findViewById(R.id.photographer_order_detail_accept);

        Intent intent=getIntent();
        order=(Order)intent.getSerializableExtra("order");

        BmobQuery<User> bmobQuery1 = new BmobQuery<>();
        bmobQuery1.addWhereEqualTo("objectId", order.getUser().getObjectId());
        bmobQuery1.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    user = list.get(0);
                    photographerOrderDetailName.setText(user.getNickName());
                    photographerOrderDetailId.setText(String.valueOf(order.getObjectId()));
                    photographerOrderDetailCreateTime.setText(order.getCreatTime().getDate());
                    photographerOrderDetailPrice.setText(String.valueOf(order.getOrderPrice())+"元");
                    photographerOrderDetailPuttime.setText(order.getPutTime().getDate().substring(0,10));
                    photographerOrderDetailLoc.setText(order.getOrderStartLoc());
                    photographerOrderDetailPackege.setText(order.getPhotographerType());
                    photographerOrderDetailCancelTime.setText(order.getPutTime().getDate().substring(0,10)+"内取消订单需要支付20%的取消费，之后不可取消");

                    if (user.getUserImage() != null) {
                        //读取数据库的头像，若无则设置默认头像
                        BmobFile PhotographerImageFile = user.getUserImage();
                        String stringImageUri = PhotographerImageFile.getFileUrl();
                        //利用ImageRequest实现图片加载
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        //构建ImageRequest 实例
                        ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                //给imageView设置图片
                                photographerOrderDetailImage.setImageBitmap(response);
                            }
                        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //设置一张错误的图片，临时用ic_launcher代替
                                Toast.makeText(OrderDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                photographerOrderDetailImage.setImageResource(R.drawable.headimage);
                            }
                        });

                        requestQueue.add(request);
                    } else {
                        photographerOrderDetailImage.setImageResource(R.drawable.headimage);
                    }

                } else {
                    Toast.makeText(OrderDetailActivity.this, "Photographer相关信息查询失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (order.getOrderAcceptState().equals(Order.Accept))
        {
            photographerOrderDetailAccept.setVisibility(View.GONE);
        }
        else
        {
            photographerOrderDetailAccept.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        init();
        photographerOrderDetailCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getMobilePhoneNumber().isEmpty())
                {
                    Toast.makeText(OrderDetailActivity.this,"该用户未设置手机号",Toast.LENGTH_SHORT).show();
                }
                else {
                    AlertDialog isExit = new AlertDialog.Builder(OrderDetailActivity.this).create();

                    isExit.setTitle("系统提示");
// 设置对话框消息
                    isExit.setMessage("确定要拨打电话" + user.getMobilePhoneNumber() + "吗");
// 添加选择按钮并注册监听
                    isExit.setButton("确定", UserListener);
                    isExit.setButton2("取消", UserListener);
// 显示对话框
                    isExit.show();
                }
            }
        });
        photographerOrderDetailAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog isExit = new AlertDialog.Builder(OrderDetailActivity.this).create();

                isExit.setTitle("系统提示");
// 设置对话框消息
                isExit.setMessage("您确定要接受这条订单吗");
// 添加选择按钮并注册监听
                isExit.setButton("确定", AcceptListener);
                isExit.setButton2("取消", AcceptListener);
// 显示对话框
                isExit.show();
            }
        });
    }
    public void call(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }
}