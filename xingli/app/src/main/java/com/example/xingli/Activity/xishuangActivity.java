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

public class xishuangActivity extends AppCompatActivity {

    Banner banner;
    MyImageLoader mImageLoader=new MyImageLoader();
    List<String> images=new ArrayList<>();
    List<String> titles=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xishuang);
        banner=(Banner) findViewById(R.id.xishuang_banner);
        initDate();
        initView();
    }
    public void initDate()
    {
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/06fa5537403aa37480c963a97ad2831f.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/c4c08c1b40217d53803e0b311ba28ad1.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/dfdcaa0f40ff6540801a079de820b7cb.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/820c22bc40a0495b80f01e780d0e3c26.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/05a01a1e40a7d16b80e24451cd3d9f78.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/1e77346f40e86f5c80ef484d92faf9c7.jpg");
        titles.add("1/6");
        titles.add("2/6");
        titles.add("3/6");
        titles.add("4/6");
        titles.add("5/6");
        titles.add("6/6");



    }
    public void initView()
    {
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(mImageLoader);
        //设置图片网址或地址的集合
        banner.setImages(images);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(titles);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
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