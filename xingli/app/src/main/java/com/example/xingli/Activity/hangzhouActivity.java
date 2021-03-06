package com.example.xingli.Activity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.xingli.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class hangzhouActivity extends AppCompatActivity {
    Banner banner;
    MyImageLoader mImageLoader=new MyImageLoader();
    List<String> images=new ArrayList<>();
    List<String> titles=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hangzhou);
        banner=(Banner) findViewById(R.id.hangzhou_banner);
        initDate();
        initView();
    }
    public void initDate()
    {
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/e4bb91764005c04580692ac09d973117.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/b98833e940161994802d1e0586459be2.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/3f272dea40fd0e9380f2e08f072fe408.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/60ae3f6e4086751480a6b1990c2ee1f7.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/02249cf3402102e880860edc2f79d2c1.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/9973cfa440a2161d809221401bea237c.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/cd094ef5400420b480f49a652031f697.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/940fff0b408f3a58804e121b66f7d654.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/ec8cfb304040f7628093b7898d093fd3.jpg");
        titles.add("1/9");
        titles.add("2/9");
        titles.add("3/9");
        titles.add("4/9");
        titles.add("5/9");
        titles.add("6/9");
        titles.add("7/9");
        titles.add("8/9");
        titles.add("9/9");


    }
    public void initView()
    {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //????????????????????????????????????????????????
        banner.setImageLoader(mImageLoader);
        //????????????????????????????????????
        banner.setImages(images);
        //?????????????????????????????????????????????????????????????????????????????????????????????
        banner.setBannerAnimation(Transformer.Default);
        //??????????????????????????????
        banner.setBannerTitles(titles);
        //????????????????????????
        banner.setDelayTime(3000);
        //???????????????????????????????????????????????????
        banner.isAutoPlay(true);
        //???????????????????????????????????????????????????
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.start();
    }
    private class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context)
                    .load(path)
                    .into(imageView);
        }
    }
}