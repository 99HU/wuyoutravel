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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.xingli.R;

import base.BaseActivity1;
import cn.bmob.v3.datatype.BmobFile;
import db.Post;
import db.User;

public class postDetailActivity extends BaseActivity1 {
    public User user;
    public Post post;
    public Post poststart;
    public TextView postName;
    public ImageView postImage;
    public TextView postProfile;
    public TextView postLoc;
    public TextView postOpenTime;
    public TextView postPriceBig;
    public TextView postPriceSmall;
    public TextView postOwnerSay;
    public TextView postOrderNum;
    public TextView postNotice1;
    public TextView postNotice2;
    public TextView postNotice3;
    public Button callPostTel;
    public Button creatOrder;
    public Button choosePost;

    String bool;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(permissions.length!=0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED ) {
                        Toast.makeText(postDetailActivity.this, "请允许拨号权限后重试", Toast.LENGTH_SHORT).show();
                     } else {
                         call(post.getPostTel());
                    }
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
                    if(ActivityCompat.checkSelfPermission(postDetailActivity.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(postDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    } else {
                        call(post.getPostTel());               }
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
        setContentView(R.layout.activity_post_detail);
        postName=(TextView)findViewById(R.id.post_name);
        postImage=(ImageView)findViewById(R.id.post_image);
        postProfile=(TextView)findViewById(R.id.post_profile);
        postLoc=(TextView)findViewById(R.id.post_location);
        postPriceBig=(TextView)findViewById(R.id.post_price_big);
        postPriceSmall=(TextView)findViewById(R.id.post_price_small);
        postOwnerSay=(TextView)findViewById(R.id.post_owner_say);
        postNotice1=(TextView)findViewById(R.id.post_notice1);
        postNotice2=(TextView)findViewById(R.id.post_notice2);
        postNotice3=(TextView)findViewById(R.id.post_notice3);
        postOrderNum=(TextView)findViewById(R.id.post_ordernum);
        postOpenTime=(TextView)findViewById(R.id.post_opentime);
        callPostTel=(Button)findViewById(R.id.post_tel);
        creatOrder=(Button)findViewById(R.id.create_order);
        choosePost=(Button)findViewById(R.id.choose_post);
        Intent intent=getIntent();
        bool=intent.getStringExtra("choose");
        if(bool.equals("see"))
        {
            post=(Post) intent.getSerializableExtra("post");
            choosePost.setVisibility(View.GONE);
            creatOrder.setVisibility(View.GONE);
        }
        else if(bool.equals("start"))
        {
            creatOrder.setVisibility(View.GONE);
            post=(Post) intent.getSerializableExtra("post");
            poststart=(Post)intent.getSerializableExtra("startpost") ;
        }
        else if(bool.equals("end"))
        {
            choosePost.setVisibility(View.GONE);
            post=(Post) intent.getSerializableExtra("post");
            poststart=(Post)intent.getSerializableExtra("startpost") ;
        }
        postName.setText(post.getPostName());

        if (post.getPostImage() != null) {

            BmobFile postImageFile = post.getPostImage();
            String stringPostImageUri = postImageFile.getFileUrl();
            //利用ImageRequest实现图片加载
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            //构建ImageRequest 实例
            ImageRequest request = new ImageRequest(stringPostImageUri, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    //给imageView设置图片
                    postImage.setImageBitmap(response);
                }
            }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //设置一张错误的图片，临时用ic_launcher代替
                    Toast.makeText(postDetailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    postImage.setImageResource(R.drawable.headimage);
                }
            });

            requestQueue.add(request);

        } else {
            postImage.setImageResource(R.drawable.headimage);
        }

        postProfile.setText(post.getPostProfile());
        postLoc.setText("位置："+post.getPostLoc());
        postPriceBig.setText("行李箱类￥"+String.valueOf(post.getLargePrice())+"/件/天");
        postPriceSmall.setText( "小包类￥"+String.valueOf(post.getSmallPrice())+"元/件/天");
        postOwnerSay.setText(post.getPostHolderSay());
        postNotice1.setText("1."+post.getPostNotice1());
        postNotice2.setText("2."+post.getPostNotice2());
        postNotice3.setText("3."+post.getPostNotice3());
        postOrderNum.setText("订单总量："+String.valueOf(post.getPostOrderNum()));
        postOpenTime.setText(post.getPostOpenTime());
        callPostTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog isExit = new AlertDialog.Builder(postDetailActivity.this).create();

                isExit.setTitle("系统提示");
// 设置对话框消息
                isExit.setMessage("确定要拨打电话"+post.getPostTel()+"吗");
// 添加选择按钮并注册监听
                isExit.setButton("确定", listener);
                isExit.setButton2("取消", listener);
// 显示对话框
                isExit.show();

            }
        });
        creatOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(postDetailActivity.this,createOrderActivity.class);
                intent1.putExtra("post",post);
                intent1.putExtra("poststart",poststart);
                startActivity(intent1);
            }
        });

        choosePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("poststart",poststart);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

    }
}