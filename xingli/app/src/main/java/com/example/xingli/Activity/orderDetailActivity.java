package com.example.xingli.Activity;

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

import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.xingli.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import base.BaseActivity1;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import db.Order;
import db.Post;

public class orderDetailActivity extends BaseActivity1 {
    public Order order;
    public Post start;
    public Post end;
    public String bool;
    TextView startPostName;
    TextView startPostTime;
    TextView startPostLoc;
    ImageView startPostImg;
    Button callStartPost;
    Button mapStartPostLoc;
    TextView endPostName;
    TextView endPostTime;
    TextView endPostLoc;
    ImageView endPostImg;

    ImageView packageImage;

    Button callEndPost;
    Button mapEndPostLoc;
    Button seeMessage;
    TextView Message;
    TextView orderId;
    TextView orderCreateTime;
    TextView orderPrice;
    TextView orderTime;
    TextView orderPakegeNum;
    TextView orderCancelTime;
    Button updateOrder;
    Button cancelOrder;
    Button finishOrder;
    TextView orderDetailPutTime;
    TextView orderDetailGetTime;

    Date getDate;
    Date putDate;
    Date createDate;

    public void init() {
        startPostName = (TextView) findViewById(R.id.orderdetail_start_post_name);
        startPostTime = (TextView) findViewById(R.id.orderdetail_start_post_time);
        startPostLoc = (TextView) findViewById(R.id.orderdetail_start_post_loc);
        startPostImg = (ImageView) findViewById(R.id.orderdetail_start_post_image);
        callStartPost = (Button) findViewById(R.id.orderdetail_call_start);
        mapStartPostLoc = (Button) findViewById(R.id.orderdetail_map_start);
        endPostName = (TextView) findViewById(R.id.orderdetail_end_post_name);
        endPostTime = (TextView) findViewById(R.id.orderdetail_end_post_time);
        endPostLoc = (TextView) findViewById(R.id.orderdetail_end_post_loc);
        endPostImg = (ImageView) findViewById(R.id.orderdetail_end_post_image);

        packageImage = (ImageView) findViewById(R.id.order_detail_package_image);

        callEndPost = (Button) findViewById(R.id.orderdetail_call_end);
        mapEndPostLoc = (Button) findViewById(R.id.orderdetail_map_end);
        seeMessage = (Button) findViewById(R.id.orderdetail_see_ordermessage);
        Message = (TextView) findViewById(R.id.orderdetail_order_message);
        orderId = (TextView) findViewById(R.id.orderdetail_order_idnum);
        orderCreateTime = (TextView) findViewById(R.id.orderdetail_order_time);
        orderPrice = (TextView) findViewById(R.id.orderdetail_order_price);
        orderTime = (TextView) findViewById(R.id.orderdetail_order_timeleng);
        orderPakegeNum = (TextView) findViewById(R.id.orderdetail_order_pakegenum);
        orderCancelTime = (TextView) findViewById(R.id.orderdetail_order_cancel);
        updateOrder = (Button) findViewById(R.id.orderdetail_update_order);
        cancelOrder = (Button) findViewById(R.id.orderdetail_cancel_order);
        orderDetailPutTime = (TextView) findViewById(R.id.orderdetail_put_time);
        orderDetailGetTime = (TextView) findViewById(R.id.orderdetail_get_time);
        finishOrder = (Button) findViewById(R.id.orderdetail_finish_order);


        updateOrder.setVisibility(View.VISIBLE);
        cancelOrder.setVisibility(View.VISIBLE);
        finishOrder.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        bool = intent.getStringExtra("choose");
        order = (Order) intent.getSerializableExtra("order");
        start = (Post) intent.getSerializableExtra("startpost");
        end = (Post) intent.getSerializableExtra("endpost");
        startPostName.setText(start.getPostName());
        startPostTime.setText(start.getPostOpenTime());
        startPostLoc.setText(start.getPostLoc());

        //startPostImg.setImageResource(start.getPostImageId());
        //读取数据库的头像，若无则设置默认头像
        if (start.getPostImage() != null) {

            BmobFile postImageFile = start.getPostImage();
            String stringImageUri = postImageFile.getFileUrl();
            //利用ImageRequest实现图片加载
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            //构建ImageRequest 实例
            ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    //给imageView设置图片
                    startPostImg.setImageBitmap(response);
                }
            }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //设置一张错误的图片，临时用ic_launcher代替
                    Toast.makeText(orderDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    startPostImg.setImageResource(R.drawable.headimage);
                }
            });

            requestQueue.add(request);

        } else {
            startPostImg.setImageResource(R.drawable.headimage);
        }


        endPostName.setText(end.getPostName());
        endPostTime.setText(end.getPostOpenTime());
        endPostLoc.setText(end.getPostLoc());

        //endPostImg.setImageResource(end.getPostImageId());
        //读取数据库的头像，若无则设置默认头像
        if (end.getPostImage() != null) {

            BmobFile postImageFile = end.getPostImage();
            String stringImageUri = postImageFile.getFileUrl();
            //利用ImageRequest实现图片加载
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            //构建ImageRequest 实例
            ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    //给imageView设置图片
                    endPostImg.setImageBitmap(response);
                }
            }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //设置一张错误的图片，临时用ic_launcher代替
                    Toast.makeText(orderDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    endPostImg.setImageResource(R.drawable.headimage);
                }
            });

            requestQueue.add(request);

        } else {
            endPostImg.setImageResource(R.drawable.headimage);
        }

        //读取数据库的头像，若无则设置默认头像
        if (order.getPackageImage() != null) {

            BmobFile postImageFile = order.getPackageImage();
            String stringImageUri = postImageFile.getFileUrl();
            //利用ImageRequest实现图片加载
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            //构建ImageRequest 实例
            ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    //给imageView设置图片
                    packageImage.setImageBitmap(response);
                }
            }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //设置一张错误的图片，临时用ic_launcher代替
                    Toast.makeText(orderDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    packageImage.setImageResource(R.drawable.empty_image);
                }
            });

            requestQueue.add(request);

        } else {
            packageImage.setImageResource(R.drawable.empty_image);
        }

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            putDate = sf.parse(order.getPutTime().getDate());
            getDate = sf.parse(order.getGetTime().getDate());
            createDate = sf.parse(order.getCreatTime().getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Message.setText(String.valueOf(order.getOrderMessage()));
//        orderId.setText(String.valueOf(order.getOrderId()));
        orderDetailPutTime.setText("寄存时间：" + order.getPutTime().getDate().substring(0, 10));
        orderDetailGetTime.setText("取件时间：" + order.getGetTime().getDate().substring(0, 10));
        orderId.setText(order.getObjectId());
        orderCreateTime.setText(order.getCreatTime().getDate());
        //        orderCreateTime.setText(String.valueOf(createDate.getYear()) + "-" + String.valueOf(createDate.getMonth()) + "-" + String.valueOf(createDate.getDay()
//                + "  " + String.valueOf(createDate.getHours())) + ":" + String.valueOf(createDate.getMinutes()) + ":" + String.valueOf(createDate.getSeconds()));
        orderPrice.setText(String.valueOf(order.getOrderPrice()) + "元");
        orderTime.setText(order.getPutTime().getDate().substring(0, 10) + "~" + order.getGetTime().getDate().substring(0, 10));
        orderPakegeNum.setText("行李箱类 X" + String.valueOf(order.getOrderBigNum()) + " 小包类 X" + String.valueOf(order.getOrderSmallNum()));
        //2021-10-28 24点前可免费取消，2021-10-2924点前取消收取订单金额20%取消费，之后默认已寄存不可取消
        orderCancelTime.setText(order.getPutTime().getDate().substring(0, 10) + " 24点前取消收取订单金额20%取消费，之后默认已寄存不可取消");
        if (order.getOrderFinishState().equals(Order.Finish)) {
            updateOrder.setVisibility(View.GONE);
            cancelOrder.setVisibility(View.GONE);
            finishOrder.setVisibility(View.GONE);
        }
        if (order.getOrderAcceptState().equals(Order.notAccept)) {
            finishOrder.setVisibility(View.GONE);
        }
    }

    public void call(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    if (ActivityCompat.checkSelfPermission(orderDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(orderDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        call(start.getPostTel());
                    }
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    DialogInterface.OnClickListener listener1 = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    if (ActivityCompat.checkSelfPermission(orderDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(orderDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        call(end.getPostTel());
                    }
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    DialogInterface.OnClickListener listener2 = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    //从数据库里删除
                    order.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(orderDetailActivity.this, "订单取消成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(orderDetailActivity.this, "订单取消失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    finish();

                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    DialogInterface.OnClickListener listener3 = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    //从数据库里完成
                    order.setOrderFinishState(Order.Finish);
                    order.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(orderDetailActivity.this, "订单已完成", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(orderDetailActivity.this, "订单完成失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    finish();

                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        init();
        callStartPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog isExit = new AlertDialog.Builder(orderDetailActivity.this).create();

                isExit.setTitle("系统提示");
                // 设置对话框消息
                isExit.setMessage("确定要拨打电话" + start.getPostTel() + "吗");
                // 添加选择按钮并注册监听
                isExit.setButton("确定", listener);
                isExit.setButton2("取消", listener);
                // 显示对话框
                isExit.show();

            }
        });
        finishOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog isExit = new AlertDialog.Builder(orderDetailActivity.this).create();

                isExit.setTitle("系统提示");
                // 设置对话框消息
                isExit.setMessage("请在确定完成所有服务后再点击完成订单，否则无法保护您的权益。您已完成这条订单吗");
                // 添加选择按钮并注册监听
                isExit.setButton("确定", listener3);
                isExit.setButton2("取消", listener3);
                // 显示对话框
                isExit.show();
            }
        });
        callEndPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog isExit = new AlertDialog.Builder(orderDetailActivity.this).create();

                isExit.setTitle("系统提示");
                // 设置对话框消息
                isExit.setMessage("确定要拨打电话" + end.getPostTel() + "吗");
                // 添加选择按钮并注册监听
                isExit.setButton("确定", listener1);
                isExit.setButton2("取消", listener1);
                // 显示对话框
                isExit.show();

            }
        });
        mapStartPostLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(orderDetailActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
        mapEndPostLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(orderDetailActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
        seeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeMessage.setVisibility(View.GONE);
                Message.setVisibility(View.VISIBLE);
            }
        });
        updateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(orderDetailActivity.this, upDateActivity.class);
                intent.putExtra("order", order);
                intent.putExtra("type", "post");
                startActivity(intent);
            }
        });
        cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog isExit = new AlertDialog.Builder(orderDetailActivity.this).create();

                isExit.setTitle("系统提示");
                // 设置对话框消息
                isExit.setMessage("确定要取消这条订单吗");
                // 添加选择按钮并注册监听
                isExit.setButton("确定", listener2);
                isExit.setButton2("取消", listener2);
                // 显示对话框
                isExit.show();
            }
        });
    }
}