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
import db.Photographer;

public class photographerDetailActivity extends BaseActivity1 {
    Photographer photographer;
    TextView photographerName;
    ImageView photographerImage;
    TextView photographerLoc;
    TextView photographerprofile;
    TextView photographerImage1Text;
    ImageView photographerImage1;
    TextView photographerImage2Text;
    ImageView photographerImage2;
    TextView photographerImage3Text;
    ImageView photographerImage3;
    TextView photographerPrice;
    TextView photographerClothPrice;
    TextView photographerSay;
    TextView photographerNotice1;
    TextView photographerNotice2;
    TextView photographerNotice3;
    Button photographerCall;
    Button createPhotographerOrder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photographer_detail);
        init();
        createPhotographerOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(photographerDetailActivity.this,createPhotographerOrderActivtity.class);
                intent.putExtra("photographer",photographer);
                startActivity(intent);
            }
        });
        photographerCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog isExit = new AlertDialog.Builder(photographerDetailActivity.this).create();

                isExit.setTitle("系统提示");
// 设置对话框消息
                isExit.setMessage("确定要拨打电话"+photographer.getMobilePhoneNumber()+"吗");
// 添加选择按钮并注册监听
                isExit.setButton("确定", listener);
                isExit.setButton2("取消", listener);
// 显示对话框
                isExit.show();
            }
        });

    }
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    if(ActivityCompat.checkSelfPermission(photographerDetailActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(photographerDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        call(photographer.getMobilePhoneNumber());               }
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

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
                Toast.makeText(photographerDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                if (imageView.equals(photographerImage)) {
                    photographerImage.setImageResource(R.drawable.headimage);
                } else {
                    photographerImage.setImageResource(R.drawable.empty_image);
                }
            }
        });
        requestQueue.add(request);
    }

    public void call(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    public void init(){
        Intent intent=getIntent();
        photographer=(Photographer) intent.getSerializableExtra("photographer");
        String bool=intent.getStringExtra("choose");
        photographerName=(TextView) findViewById(R.id.photographer_name);
        photographerImage=(ImageView) findViewById(R.id.photographer_image);
        photographerLoc=(TextView) findViewById(R.id.photographer_location);
        photographerprofile=(TextView) findViewById(R.id.photographer_profile);
        photographerImage1Text=(TextView) findViewById(R.id.show_image1_profile);
        photographerImage1=(ImageView) findViewById(R.id.photographer_show_image1);
        photographerImage2Text=(TextView) findViewById(R.id.show_image2_profile);
        photographerImage2=(ImageView) findViewById(R.id.photographer_show_image2);
        photographerImage3Text=(TextView) findViewById(R.id.show_image3_profile);
        photographerImage3=(ImageView) findViewById(R.id.photographer_show_image3);
        photographerPrice=(TextView) findViewById(R.id.photographer_price);
        photographerClothPrice=(TextView) findViewById(R.id.photographer_cloth_price);
        photographerSay=(TextView) findViewById(R.id.photographer_owner_say);
        photographerNotice1=(TextView) findViewById(R.id.photographer_notice1);
        photographerNotice2=(TextView) findViewById(R.id.photographer_notice2);
        photographerNotice3=(TextView) findViewById(R.id.photographer_notice3);
        photographerCall=(Button) findViewById(R.id.photographer_tel);
        createPhotographerOrder=(Button) findViewById(R.id.create_photographer_order);

        photographerName.setText(photographer.getNickName());

        if (photographer.getHeadImage() != null) {
            showImageOfDateBase(photographer.getHeadImage(), photographerImage);
        } else {
            photographerImage.setImageResource(R.drawable.headimage);
        }

        if (photographer.getImageShow1() != null) {
            showImageOfDateBase(photographer.getImageShow1(), photographerImage1);
        } else {
            photographerImage1.setImageResource(R.drawable.empty_image);
        }

        if (photographer.getImageShow2() != null) {
            showImageOfDateBase(photographer.getImageShow2(), photographerImage2);
        } else {
            photographerImage2.setImageResource(R.drawable.empty_image);
        }

        if (photographer.getImageShow3() != null) {
            showImageOfDateBase(photographer.getImageShow3(), photographerImage3);
        } else {
            photographerImage3.setImageResource(R.drawable.empty_image);
        }

        photographerLoc.setText(photographer.getPhotographerLoc());
        photographerprofile.setText(photographer.getProfile());
        photographerImage1Text.setText(photographer.getImageShowText1());

        photographerImage2Text.setText(photographer.getImageShowText2());
        photographerImage3Text.setText(photographer.getImageShowText3());
        photographerPrice.setText(String.valueOf(photographer.getPriceNoUniform()));
        photographerClothPrice.setText(String.valueOf(photographer.getPriceIncludeUniform()));
        photographerSay.setText(photographer.getPhotographerSay());
        photographerNotice1.setText(photographer.getNotice1());
        photographerNotice2.setText(photographer.getNotice2());
        photographerNotice3.setText(photographer.getNotice3());

        if (bool.equals("see")){
            createPhotographerOrder.setVisibility(View.GONE);
        }
    }
}