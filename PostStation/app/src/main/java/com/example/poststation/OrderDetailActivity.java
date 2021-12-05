package com.example.poststation;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.util.V;

public class OrderDetailActivity extends AppCompatActivity {
    public SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    public TextView OrderDetailPostType;
    public TextView PostName;
    public TextView PostOpenTime;
    public ImageView PostImage;
    public TextView PostLocation;
    public Button PostCall;
    public TextView UserName;
    public ImageView PackageImage;
    public TextView OrderTime1;
    public Button UserCall;
    public Button SeeMessage;
    public TextView Message;
    public TextView OrderId;
    public TextView OrderCreateTime;
    public TextView OrderPrice;
    public TextView OrderTimeLen;
    public TextView OrderPackageNum;
    public Button AcceptOrder;
    public Button FinishOrder;
    public Order order;
    public Manager manager;
    public Post startPost;
    public Post endPost;
    public User user;
    public BmobFile postImageFile;
    public BmobFile packageImageFile;
    public String stringImageUri;

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

    DialogInterface.OnClickListener startPostListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    if (ActivityCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(OrderDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        call(startPost.getPostTel());
                    }
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    DialogInterface.OnClickListener endPostListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    if (ActivityCompat.checkSelfPermission(OrderDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(OrderDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        call(endPost.getPostTel());
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

        Intent intent=getIntent();
        order=(Order)intent.getSerializableExtra("order");
        manager= BmobUser.getCurrentUser(Manager.class);
        //查询当前用户
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.getObject(order.getUser().getObjectId(), new QueryListener<User>() {
            @Override
            public void done(User object,BmobException e) {
                if(e==null){
                    user=object;
                    UserName.setText(user.getNickName());

                    PackageImage.setImageResource(R.drawable.bag);
                    if (order.getPackageImage() != null) {

                        packageImageFile = order.getPackageImage() ;
                        stringImageUri = packageImageFile.getFileUrl();
                        //利用ImageRequest实现图片加载
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        //构建ImageRequest 实例
                        ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                //给imageView设置图片
                                PackageImage.setImageBitmap(response);
                            }
                        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //设置一张错误的图片，临时用ic_launcher代替
                                Toast.makeText(OrderDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                PackageImage.setImageResource(R.drawable.empty_image);
                            }
                        });

                        requestQueue.add(request);

                    } else {
                        PackageImage.setImageResource(R.drawable.empty_image);
                    }
                    Toast.makeText(OrderDetailActivity.this,"查询用户成功",Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(OrderDetailActivity.this,"查询用户失败",Toast.LENGTH_SHORT).show();
                }
            }

        });
        //查询起始和终点驿站
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
                        if (manager.getPost().getObjectId().equals(startPost.getObjectId())) {
                            OrderDetailPostType.setText("终点驿站");
                            PostName.setText(endPost.getPostName());
                            PostOpenTime.setText(endPost.getPostOpenTime());
                            PostLocation.setText(endPost.getPostLoc());
                            OrderTime1.setText("寄件时间：" + order.getPutTime().getDate().substring(0, 10));
                            if (BmobUser.isLogin()) {
                                //读取数据库的头像，若无则设置默认头像
                                if (endPost.getPostImage() != null) {

                                    postImageFile = endPost.getPostImage();
                                    stringImageUri = postImageFile.getFileUrl();
                                    //利用ImageRequest实现图片加载
                                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                    //构建ImageRequest 实例
                                    ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap response) {
                                            //给imageView设置图片
                                            PostImage.setImageBitmap(response);
                                        }
                                    }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            //设置一张错误的图片，临时用ic_launcher代替
                                            Toast.makeText(OrderDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                            PostImage.setImageResource(R.drawable.headimage);
                                        }
                                    });

                                    requestQueue.add(request);

                                } else {
                                    PostImage.setImageResource(R.drawable.headimage);
                                }
                            }
                        }
                        //当前是终点驿站 需要看到起始驿站
                        if (manager.getPost().getObjectId().equals(endPost.getObjectId()))
                        {
                            OrderDetailPostType.setText("起点驿站");
                            PostName.setText(startPost.getPostName());
                            PostOpenTime.setText(startPost.getPostOpenTime());
                            PostImage.setImageResource(startPost.getPostImageId());
                            PostLocation.setText(startPost.getPostLoc());
                            OrderTime1.setText("取件时间："+order.getGetTime().getDate().substring(0,10));
                            if (BmobUser.isLogin()) {
                                //读取数据库的头像，若无则设置默认头像
                                if (startPost.getPostImage() != null) {

                                    postImageFile = startPost.getPostImage();
                                    stringImageUri = postImageFile.getFileUrl();
                                    //利用ImageRequest实现图片加载
                                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                    //构建ImageRequest 实例
                                    ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
                                        @Override
                                        public void onResponse(Bitmap response) {
                                            //给imageView设置图片
                                            PostImage.setImageBitmap(response);
                                        }
                                    }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            //设置一张错误的图片，临时用ic_launcher代替
                                            Toast.makeText(OrderDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                            PostImage.setImageResource(R.drawable.headimage);
                                        }
                                    });

                                    requestQueue.add(request);

                                } else {
                                    PostImage.setImageResource(R.drawable.headimage);
                                }
                            }
                        }
                        Message.setText(String.valueOf(order.getOrderMessage()));
                        OrderId.setText(order.getObjectId());
                        OrderCreateTime.setText(order.getCreatTime().getDate());
                        OrderPrice.setText(String.valueOf(order.getOrderPrice())+"元");
                        OrderTimeLen.setText(order.getPutTime().getDate().substring(0,10)+"~"+order.getGetTime().getDate().substring(0,10));
                        OrderPackageNum.setText("行李箱类 X" + String.valueOf(order.getOrderBigNum()) + " 小包类 X" + String.valueOf(order.getOrderSmallNum()));

                    } else {
                        Toast.makeText(OrderDetailActivity.this, "Post相关信息查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        });


        user=order.getUser();

        OrderDetailPostType=(TextView) findViewById(R.id.order_post_title);
        PostName=(TextView) findViewById(R.id.orderdetail_post_name);
        PostOpenTime=(TextView) findViewById(R.id.orderdetail_post_time);
        PostImage=(ImageView) findViewById(R.id.orderdetail_post_image);
        PostLocation=(TextView) findViewById(R.id.orderdetail_post_loc);
        PostCall=(Button) findViewById(R.id.orderdetail_post_call);
        UserName=(TextView) findViewById(R.id.orderdetail_user_name);
        PackageImage=(ImageView) findViewById(R.id.orderdetail_package_image);
        OrderTime1=(TextView) findViewById(R.id.orderdetail_time);
        UserCall=(Button) findViewById(R.id.orderdetail_call_user);
        SeeMessage=(Button) findViewById(R.id.orderdetail_see_ordermessage);
        Message=(TextView) findViewById(R.id.orderdetail_order_message);
        OrderId=(TextView) findViewById(R.id.orderdetail_order_idnum);
        OrderCreateTime=(TextView) findViewById(R.id.orderdetail_order_time);
        OrderPrice=(TextView) findViewById(R.id.orderdetail_order_price);
        OrderTimeLen=(TextView) findViewById(R.id.orderdetail_order_timeleng);
        OrderPackageNum=(TextView) findViewById(R.id.orderdetail_order_pakegenum);
        AcceptOrder=(Button) findViewById(R.id.orderdetail_accept_order);
        FinishOrder=(Button) findViewById(R.id.orderdetail_finish_order);
        if (order.getOrderAcceptState().equals(Order.Accept))
        {
            AcceptOrder.setVisibility(View.GONE);
        }
        else
        {
            AcceptOrder.setVisibility(View.VISIBLE);
        }
        SeeMessage.setVisibility(View.VISIBLE);
        Message.setVisibility(View.GONE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        init();
        SeeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeeMessage.setVisibility(View.GONE);
                Message.setVisibility(View.VISIBLE);
            }
        });
        Message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeeMessage.setVisibility(View.VISIBLE);
                Message.setVisibility(View.GONE);
            }
        });
        PostCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.getPost().getObjectId().equals(order.getStartPost().getObjectId()))
                {
                    //callEndPost
                    if (endPost.getPostTel().isEmpty())
                    {
                        Toast.makeText(OrderDetailActivity.this,"该用户未设置手机号",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        AlertDialog isExit = new AlertDialog.Builder(OrderDetailActivity.this).create();

                        isExit.setTitle("系统提示");
// 设置对话框消息
                        isExit.setMessage("确定要拨打电话" + endPost.getPostTel() + "吗");
// 添加选择按钮并注册监听
                        isExit.setButton("确定", endPostListener);
                        isExit.setButton2("取消",endPostListener);
// 显示对话框
                        isExit.show();
                    }
                }
                //当前是重点驿站
                if (manager.getPost().getObjectId().equals(order.getEndPost().getObjectId()))
                {
                    //callStartPost
                    if (startPost.getPostTel().isEmpty())
                    {
                        Toast.makeText(OrderDetailActivity.this,"该用户未设置手机号",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        AlertDialog isExit = new AlertDialog.Builder(OrderDetailActivity.this).create();

                        isExit.setTitle("系统提示");
// 设置对话框消息
                        isExit.setMessage("确定要拨打电话" + startPost.getPostTel() + "吗");
// 添加选择按钮并注册监听
                        isExit.setButton("确定", startPostListener);
                        isExit.setButton2("取消", startPostListener);
// 显示对话框
                        isExit.show();
                    }
                }
            }
        });
        UserCall.setOnClickListener(new View.OnClickListener() {
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
        AcceptOrder.setOnClickListener(new View.OnClickListener() {
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
