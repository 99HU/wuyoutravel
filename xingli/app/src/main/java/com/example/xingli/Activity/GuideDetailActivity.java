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

import base.BaseActivity1;
import cn.bmob.v3.datatype.BmobFile;
import db.Guide;

public class GuideDetailActivity extends BaseActivity1 {
    Guide guide;
    TextView guideName;
    ImageView guideImage;
    TextView guideLoc;
    TextView guideprofile;
    TextView guideImage1Text;
    ImageView guideImage1;
    TextView guideImage2Text;
    ImageView guideImage2;
    TextView guideImage3Text;
    ImageView guideImage3;
    TextView guidePrice;
    TextView guideSay;
    TextView guideNotice1;
    TextView guideNotice2;
    TextView guideNotice3;
    Button guideCall;
    Button createguideOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_detail);

        init();
        createguideOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(GuideDetailActivity.this,createGuideOrderActivity.class);
                intent.putExtra("guide",guide);
                startActivity(intent);
            }
        });
        guideCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog isExit = new AlertDialog.Builder(GuideDetailActivity.this).create();

                isExit.setTitle("系统提示");
// 设置对话框消息
                isExit.setMessage("确定要拨打电话"+guide.getMobilePhoneNumber()+"吗");
// 添加选择按钮并注册监听
                isExit.setButton("确定", listener);
                isExit.setButton2("取消", listener);
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

    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    if(ActivityCompat.checkSelfPermission(GuideDetailActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(GuideDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        call(guide.getMobilePhoneNumber());               }
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
        guide=(Guide) intent.getSerializableExtra("guide");
        String bool=intent.getStringExtra("choose");
        guideName=(TextView) findViewById(R.id.guide_name);
        guideImage=(ImageView) findViewById(R.id.guide_image);
        guideLoc=(TextView) findViewById(R.id.guide_location);
        guideprofile=(TextView) findViewById(R.id.guide_profile);
        guideImage1Text=(TextView) findViewById(R.id.guide_show_image1_profile);
        guideImage1=(ImageView) findViewById(R.id.guide_show_image1);
        guideImage2Text=(TextView) findViewById(R.id.guide_show_image2_profile);
        guideImage2=(ImageView) findViewById(R.id.guide_show_image2);
        guideImage3Text=(TextView) findViewById(R.id.guide_show_image3_profile);
        guideImage3=(ImageView) findViewById(R.id.guide_show_image3);
        guidePrice=(TextView) findViewById(R.id.guide_price);
        guideSay=(TextView) findViewById(R.id.guide_owner_say);
        guideNotice1=(TextView) findViewById(R.id.guide_notice1);
        guideNotice2=(TextView) findViewById(R.id.guide_notice2);
        guideNotice3=(TextView) findViewById(R.id.guide_notice3);
        guideCall=(Button) findViewById(R.id.guide_tel);
        createguideOrder=(Button) findViewById(R.id.create_guide_order);

        if (guide.getHeadImage() != null) {
            showImageOfDateBase(guide.getHeadImage(), guideImage);
        } else {
            guideImage.setImageResource(R.drawable.headimage);
        }

        if (guide.getImageShow1() != null) {
            showImageOfDateBase(guide.getImageShow1(), guideImage1);
        } else {
            guideImage1.setImageResource(R.drawable.empty_image);
        }

        if (guide.getImageShow2() != null) {
            showImageOfDateBase(guide.getImageShow2(), guideImage2);
        } else {
            guideImage2.setImageResource(R.drawable.empty_image);
        }

        if (guide.getImageShow3() != null) {
            showImageOfDateBase(guide.getImageShow3(), guideImage3);
        } else {
            guideImage3.setImageResource(R.drawable.empty_image);
        }
        
        guideName.setText(guide.getNickName());
//        guideImage.setImageResource(guide.getGuideImageId());
        guideLoc.setText(guide.getGuideLoc());
        guideprofile.setText(guide.getProfile());

        guideImage1Text.setText(guide.getImageShowText1());
       // guideImage1.setImageResource(guide.getImageShow1());
        guideImage2Text.setText(guide.getImageShowText2());
    //    guideImage2.setImageResource(guide.getGuideImageShowId2());
        guideImage3Text.setText(guide.getImageShowText3());
    //    guideImage3.setImageResource(guide.getGuideImageShowId3());
        guidePrice.setText(String.valueOf(guide.getPriceNoUniform()));
        guideSay.setText(guide.getPhotographerSay());
        guideNotice1.setText(guide.getNotice1());
        guideNotice2.setText(guide.getNotice2());
        guideNotice3.setText(guide.getNotice3());

        if (bool.equals("see")){
            createguideOrder.setVisibility(View.GONE);
        }
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
                Toast.makeText(GuideDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
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