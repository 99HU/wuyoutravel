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
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import db.Guide;
import db.Order;

public class GuideOrderDetailActivity extends BaseActivity1 {

    Order order;
    TextView guideOrderDetailName;
    TextView guideOrderDetailGender;
    TextView guideOrderDetailAge;
    TextView guideOrderDetailNum;
    ImageView guideOrderDetailImage;
    Button guideOrderDetailCall;
    TextView guideOrderDetailId;
    TextView guideOrderDetailCreateTime;
    TextView guideOrderDetailPrice;
    TextView guideOrderDetailPuttime;
    TextView guideOrderDetailLoc;
    TextView guideOrderDetailCancelTime;
    Button guideOrderDetailUpdate;
    Button guideOrderDetailCancel;
    Button guideOrderDetailFinish;
    Guide guide;
    BmobDate createDate;
    BmobDate putDate;

    public void init() throws ParseException {
        Intent intent=getIntent();
        order=(Order)intent.getSerializableExtra("order");
        guideOrderDetailName=(TextView) findViewById(R.id.guide_order_detail_name);
        guideOrderDetailGender=(TextView) findViewById(R.id.guide_order_detail_gender);
        guideOrderDetailAge=(TextView) findViewById(R.id.guide_order_detail_age);
        guideOrderDetailNum=(TextView) findViewById(R.id.guide_order_detail_num);
        guideOrderDetailImage=(ImageView) findViewById(R.id.guide_order_detail_image);
        guideOrderDetailCall=(Button) findViewById(R.id.guide_order_detail_call);
        guideOrderDetailId=(TextView) findViewById(R.id.guide_order_detail_id);
        guideOrderDetailCreateTime=(TextView) findViewById(R.id.guide_order_detail_createtime);
        guideOrderDetailPrice=(TextView) findViewById(R.id.guide_order_detail_price);
        guideOrderDetailPuttime=(TextView) findViewById(R.id.guide_order_detail_puttime);
        guideOrderDetailLoc=(TextView) findViewById(R.id.guide_order_detail_loc);
        guideOrderDetailCancelTime=(TextView) findViewById(R.id.guide_order_detail_cancletime);
        guideOrderDetailUpdate=(Button) findViewById(R.id.guide_order_detail__updater);
        guideOrderDetailCancel=(Button) findViewById(R.id.guide_order_detail_cancel);
        guideOrderDetailFinish=(Button)findViewById(R.id.guide_order_detail_finish);




        BmobQuery<Guide> bmobQuery1 = new BmobQuery<>();
        bmobQuery1.addWhereEqualTo("objectId", order.getguide().getObjectId());
        bmobQuery1.findObjects(new FindListener<Guide>() {
            @Override
            public void done(List<Guide> list, BmobException e) {
                if (e == null) {
                    guide = list.get(0);
                    guideOrderDetailName.setText(guide.getNickName());
                    guideOrderDetailGender.setText("?????????"+guide.getGender());
                    guideOrderDetailAge.setText("?????????"+String.valueOf(guide.getAge()));
                    guideOrderDetailNum.setText("?????????"+String.valueOf(guide.getMouthNum())+"????????????");
                    //       guideOrderDetailImage.setImageResource(order.getguide().getGuideImageId());
                    guideOrderDetailId.setText(String.valueOf(order.getObjectId()));
                    guideOrderDetailCreateTime.setText(order.getCreatTime().getDate());
                    guideOrderDetailPrice.setText(String.valueOf(order.getOrderPrice())+"???");
                    guideOrderDetailPuttime.setText(order.getPutTime().getDate().substring(0,10));
                    guideOrderDetailLoc.setText(order.getOrderStartLoc());
                    guideOrderDetailCancelTime.setText(order.getPutTime().getDate().substring(0,10)+"???????????????????????????20%?????????????????????????????????");

                    if (guide.getHeadImage() != null) {
                        //??????????????????????????????????????????????????????
                        BmobFile PhotographerImageFile = guide.getHeadImage();
                        String stringImageUri = PhotographerImageFile.getFileUrl();
                        //??????ImageRequest??????????????????
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                        //??????ImageRequest ??????
                        ImageRequest request = new ImageRequest(stringImageUri, new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap response) {
                                //???imageView????????????
                                guideOrderDetailImage.setImageBitmap(response);
                            }
                        }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //???????????????????????????????????????ic_launcher??????
                                Toast.makeText(GuideOrderDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                guideOrderDetailImage.setImageResource(R.drawable.headimage);
                            }
                        });

                        requestQueue.add(request);
                    } else {
                        guideOrderDetailImage.setImageResource(R.drawable.headimage);
                    }

                } else {
                    Toast.makeText(GuideOrderDetailActivity.this, "Photographer????????????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });



        if (order.getOrderFinishState().equals(Order.Finish))
        {
            guideOrderDetailUpdate.setVisibility(View.GONE);
            guideOrderDetailCancel.setVisibility(View.GONE);
            guideOrderDetailFinish.setVisibility(View.GONE);
        }
        if (order.getOrderAcceptState().equals(Order.notAccept))
        {
            guideOrderDetailFinish.setVisibility(View.GONE);

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
                    if(ActivityCompat.checkSelfPermission(GuideOrderDetailActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(GuideOrderDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        call(guide.getMobilePhoneNumber());               }
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
                    order.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(GuideOrderDetailActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(GuideOrderDetailActivity.this, "??????????????????" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                        //?????????????????????
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
                    order.setOrderFinishState(Order.Finish);
                    order.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(GuideOrderDetailActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(GuideOrderDetailActivity.this, "??????????????????" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    //?????????????????????
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
        setContentView(R.layout.activity_guide_order_detail);
        try {
            init();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        guideOrderDetailCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog isExit = new AlertDialog.Builder(GuideOrderDetailActivity.this).create();

                isExit.setTitle("????????????");
// ?????????????????????
                isExit.setMessage("?????????????????????"+guide.getMobilePhoneNumber()+"???");
// ?????????????????????????????????
                isExit.setButton("??????", listener1);
                isExit.setButton2("??????", listener1);
// ???????????????
                isExit.show();
            }
        });
        guideOrderDetailFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog isExit = new AlertDialog.Builder(GuideOrderDetailActivity.this).create();

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

        guideOrderDetailCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog isExit = new AlertDialog.Builder(GuideOrderDetailActivity.this).create();

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
        guideOrderDetailUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GuideOrderDetailActivity.this,UpdateGuideOrderActivity.class);
                intent.putExtra("order",order);
                startActivity(intent);
            }
        });
    }
}