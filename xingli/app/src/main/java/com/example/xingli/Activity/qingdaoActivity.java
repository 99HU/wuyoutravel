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

public class qingdaoActivity extends AppCompatActivity {
    Banner banner;
    MyImageLoader mImageLoader=new MyImageLoader();
    List<String> images=new ArrayList<>();
    List<String> titles=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qingdao);
        banner=(Banner) findViewById(R.id.qingdao_banner);
        initDate();
        initView();
    }
    public void initDate()
    {
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/92ad9d0740dfc1e9808c44bbe0ec8107.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/e43d4dd740c3bf548054fb95274307dd.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/f4fab40340ecfeaa8080ff9e8e3d7f39.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/3890c61940bd3b7880ef56d117db6f33.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/cbfc54914029c1cc80eb19392ccdf80e.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/e51c8e6f4072e05a80d54a921ebcd47b.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/6ab5f48940b5127a80efd99ee5b5b95c.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/c135c074401900008030fbc97334974d.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/4e108747403c7a9e80d517415810fce5.jpg");
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