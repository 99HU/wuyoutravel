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
        //??????????????????????????????????????????????????????
        if (start.getPostImage() != null) {

            BmobFile postImageFile = start.getPostImage();
            String stringImageUri = postImageFile.getFileUrl();
            //??????ImageRequest??????????????????
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            //??????ImageRequest ??????
            ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    //???imageView????????????
                    startPostImg.setImageBitmap(response);
                }
            }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //???????????????????????????????????????ic_launcher??????
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
        //??????????????????????????????????????????????????????
        if (end.getPostImage() != null) {

            BmobFile postImageFile = end.getPostImage();
            String stringImageUri = postImageFile.getFileUrl();
            //??????ImageRequest??????????????????
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            //??????ImageRequest ??????
            ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    //???imageView????????????
                    endPostImg.setImageBitmap(response);
                }
            }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //???????????????????????????????????????ic_launcher??????
                    Toast.makeText(orderDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    endPostImg.setImageResource(R.drawable.headimage);
                }
            });

            requestQueue.add(request);

        } else {
            endPostImg.setImageResource(R.drawable.headimage);
        }

        //??????????????????????????????????????????????????????
        if (order.getPackageImage() != null) {

            BmobFile postImageFile = order.getPackageImage();
            String stringImageUri = postImageFile.getFileUrl();
            //??????ImageRequest??????????????????
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            //??????ImageRequest ??????
            ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    //???imageView????????????
                    packageImage.setImageBitmap(response);
                }
            }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //???????????????????????????????????????ic_launcher??????
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
        orderDetailPutTime.setText("???????????????" + order.getPutTime().getDate().substring(0, 10));
        orderDetailGetTime.setText("???????????????" + order.getGetTime().getDate().substring(0, 10));
        orderId.setText(order.getObjectId());
        orderCreateTime.setText(order.getCreatTime().getDate());
        //        orderCreateTime.setText(String.valueOf(createDate.getYear()) + "-" + String.valueOf(createDate.getMonth()) + "-" + String.valueOf(createDate.getDay()
//                + "  " + String.valueOf(createDate.getHours())) + ":" + String.valueOf(createDate.getMinutes()) + ":" + String.valueOf(createDate.getSeconds()));
        orderPrice.setText(String.valueOf(order.getOrderPrice()) + "???");
        orderTime.setText(order.getPutTime().getDate().substring(0, 10) + "~" + order.getGetTime().getDate().substring(0, 10));
        orderPakegeNum.setText("???????????? X" + String.valueOf(order.getOrderBigNum()) + " ????????? X" + String.valueOf(order.getOrderSmallNum()));
        //2021-10-28 24????????????????????????2021-10-2924??????????????????????????????20%?????????????????????????????????????????????
        orderCancelTime.setText(order.getPutTime().getDate().substring(0, 10) + " 24??????????????????????????????20%?????????????????????????????????????????????");
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
                case AlertDialog.BUTTON_POSITIVE:// "??????"??????????????????
                    if (ActivityCompat.checkSelfPermission(orderDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(orderDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        call(start.getPostTel());
                    }
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "??????"??????????????????????????????
                    break;
                default:
                    break;
            }
        }
    };

    DialogInterface.OnClickListener listener1 = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "??????"??????????????????
                    if (ActivityCompat.checkSelfPermission(orderDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(orderDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        call(end.getPostTel());
                    }
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "??????"??????????????????????????????
                    break;
                default:
                    break;
            }
        }
    };

    DialogInterface.OnClickListener listener2 = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "??????"??????????????????
                    //?????????????????????
                    order.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(orderDetailActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(orderDetailActivity.this, "??????????????????" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    finish();

                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "??????"??????????????????????????????
                    break;
                default:
                    break;
            }
        }
    };

    DialogInterface.OnClickListener listener3 = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "??????"??????????????????
                    //?????????????????????
                    order.setOrderFinishState(Order.Finish);
                    order.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(orderDetailActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(orderDetailActivity.this, "??????????????????" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    finish();

                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "??????"??????????????????????????????
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

                isExit.setTitle("????????????");
                // ?????????????????????
                isExit.setMessage("?????????????????????" + start.getPostTel() + "???");
                // ?????????????????????????????????
                isExit.setButton("??????", listener);
                isExit.setButton2("??????", listener);
                // ???????????????
                isExit.show();

            }
        });
        finishOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog isExit = new AlertDialog.Builder(orderDetailActivity.this).create();

                isExit.setTitle("????????????");
                // ?????????????????????
                isExit.setMessage("?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????");
                // ?????????????????????????????????
                isExit.setButton("??????", listener3);
                isExit.setButton2("??????", listener3);
                // ???????????????
                isExit.show();
            }
        });
        callEndPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog isExit = new AlertDialog.Builder(orderDetailActivity.this).create();

                isExit.setTitle("????????????");
                // ?????????????????????
                isExit.setMessage("?????????????????????" + end.getPostTel() + "???");
                // ?????????????????????????????????
                isExit.setButton("??????", listener1);
                isExit.setButton2("??????", listener1);
                // ???????????????
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

                isExit.setTitle("????????????");
                // ?????????????????????
                isExit.setMessage("??????????????????????????????");
                // ?????????????????????????????????
                isExit.setButton("??????", listener2);
                isExit.setButton2("??????", listener2);
                // ???????????????
                isExit.show();
            }
        });
    }
}