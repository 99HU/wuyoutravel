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
import java.util.List;

import base.BaseActivity1;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import db.Order;
import db.Photographer;

public class PhotographerOrderDetailActivity extends BaseActivity1 {
    Order order;
    Photographer photographer;
    TextView photographerOrderDetailName;
    TextView photographerOrderDetailGender;
    TextView photographerOrderDetailAge;
    TextView photographerOrderDetailNum;
    ImageView photographerOrderDetailImage;
    Button photographerOrderDetailCall;
    TextView photographerOrderDetailId;
    TextView photographerOrderDetailCreateTime;
    TextView photographerOrderDetailPrice;
    TextView photographerOrderDetailPuttime;
    TextView photographerOrderDetailLoc;
    TextView photographerOrderDetailPackege;
    TextView photographerOrderDetailCancelTime;
    Button photographerOrderDetailUpdate;
    Button photographerOrderDetailCancel;
    Button photographerOrderDetailFinish;


    public void init() throws ParseException {

        photographerOrderDetailName=(TextView) findViewById(R.id.photographer_order_detail_name);
        photographerOrderDetailGender=(TextView) findViewById(R.id.photographer_order_detail_gender);
        photographerOrderDetailAge=(TextView) findViewById(R.id.photographer_order_detail_age);
        photographerOrderDetailNum=(TextView) findViewById(R.id.photographer_order_detail_num);
        photographerOrderDetailImage=(ImageView) findViewById(R.id.photographer_order_detail_image);
        photographerOrderDetailCall=(Button) findViewById(R.id.photographer_order_detail_call);
        photographerOrderDetailId=(TextView) findViewById(R.id.photographer_order_detail_id);
        photographerOrderDetailCreateTime=(TextView) findViewById(R.id.photographer_order_detail_createtime);
        photographerOrderDetailPrice=(TextView) findViewById(R.id.photographer_order_detail_price);
        photographerOrderDetailPuttime=(TextView) findViewById(R.id.photographer_order_detail_puttime);
        photographerOrderDetailLoc=(TextView) findViewById(R.id.photographer_order_detail_loc);
        photographerOrderDetailPackege=(TextView) findViewById(R.id.photographer_order_detail_package);
        photographerOrderDetailCancelTime=(TextView) findViewById(R.id.photographer_order_detail_cancletime);
        photographerOrderDetailUpdate=(Button) findViewById(R.id.photographer_order_detail__updater);
        photographerOrderDetailCancel=(Button) findViewById(R.id.photographer_order_detail_cancel);
        photographerOrderDetailFinish=(Button)findViewById(R.id.photographer_order_detail_finish);

        Intent intent=getIntent();
        order=(Order)intent.getSerializableExtra("order");
        BmobQuery<Photographer> bmobQuery1 = new BmobQuery<>();
        bmobQuery1.addWhereEqualTo("objectId", order.getPhotographer().getObjectId());
        bmobQuery1.findObjects(new FindListener<Photographer>() {
            @Override
            public void done(List<Photographer> list, BmobException e) {
                if (e == null) {
                    photographer = list.get(0);
                    photographerOrderDetailName.setText(photographer.getNickName());
                    photographerOrderDetailGender.setText("?????????"+photographer.getGender());
                    photographerOrderDetailAge.setText("?????????"+String.valueOf(photographer.getAge()));
                    photographerOrderDetailNum.setText("?????????"+String.valueOf(photographer.getMouthNum())+"????????????");
                    photographerOrderDetailId.setText(String.valueOf(order.getObjectId()));
                    photographerOrderDetailCreateTime.setText(order.getCreatTime().getDate());
                    photographerOrderDetailPrice.setText(String.valueOf(order.getOrderPrice())+"???");
                    photographerOrderDetailPuttime.setText(order.getPutTime().getDate().substring(0,10));
                    photographerOrderDetailLoc.setText(order.getOrderStartLoc());
                    photographerOrderDetailPackege.setText(order.getPhotographerType());
                    photographerOrderDetailCancelTime.setText(order.getPutTime().getDate().substring(0,10)+"???????????????????????????20%?????????????????????????????????");

                    if (photographer.getHeadImage() != null) {
                        //??????????????????????????????????????????????????????
                        BmobFile PhotographerImageFile = photographer.getHeadImage();
                        String stringImageUri = PhotographerImageFile.getFileUrl();
                        //??????ImageRequest??????????????????
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        //??????ImageRequest ??????
                        ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                //???imageView????????????
                                photographerOrderDetailImage.setImageBitmap(response);
                            }
                        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //???????????????????????????????????????ic_launcher??????
                                Toast.makeText(PhotographerOrderDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                photographerOrderDetailImage.setImageResource(R.drawable.headimage);
                            }
                        });

                        requestQueue.add(request);
                    } else {
                        photographerOrderDetailImage.setImageResource(R.drawable.headimage);
                    }

                } else {
                    Toast.makeText(PhotographerOrderDetailActivity.this, "Photographer????????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });





        if (order.getOrderFinishState().equals(Order.Finish))
        {
            photographerOrderDetailUpdate.setVisibility(View.GONE);
            photographerOrderDetailCancel.setVisibility(View.GONE);
            photographerOrderDetailFinish.setVisibility(View.GONE);
        }
        if (order.getOrderAcceptState().equals(Order.notAccept))
        {
            photographerOrderDetailFinish.setVisibility(View.GONE);
        }
    }

    public void call(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    DialogInterface.OnClickListener listener1 = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "??????"??????????????????
                    if(ActivityCompat.checkSelfPermission(PhotographerOrderDetailActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PhotographerOrderDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        call(order.getPhotographer().getMobilePhoneNumber());               }
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "??????"??????????????????????????????
                    break;
                default:
                    break;
            }
        }
    };

    DialogInterface.OnClickListener listener2 = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "??????"??????????????????
                        //?????????????????????
                        order.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(PhotographerOrderDetailActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(PhotographerOrderDetailActivity.this, "??????????????????" + e.getMessage(), Toast.LENGTH_SHORT).show();
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


    DialogInterface.OnClickListener listener3 = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "??????"??????????????????
                    //?????????????????????
                    order.setOrderFinishState(Order.Finish);
                    order.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(PhotographerOrderDetailActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PhotographerOrderDetailActivity.this, "??????????????????" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_photographer_order_detail);
        try {
            init();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        photographerOrderDetailCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog isExit = new AlertDialog.Builder(PhotographerOrderDetailActivity.this).create();

                isExit.setTitle("????????????");
// ?????????????????????
                isExit.setMessage("?????????????????????"+photographer.getMobilePhoneNumber()+"???");
// ?????????????????????????????????
                isExit.setButton("??????", listener1);
                isExit.setButton2("??????", listener1);
// ???????????????
                isExit.show();
            }
        });
        photographerOrderDetailFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog isExit = new AlertDialog.Builder(PhotographerOrderDetailActivity.this).create();

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

        photographerOrderDetailCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog isExit = new AlertDialog.Builder(PhotographerOrderDetailActivity.this).create();

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
        photographerOrderDetailUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PhotographerOrderDetailActivity.this,UpdatePhotographerOrderActivity.class);
                intent.putExtra("order",order);
                startActivity(intent);
            }
        });
    }
}