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

public class changshaActivity extends AppCompatActivity {
    Banner banner;
    MyImageLoader mImageLoader=new MyImageLoader();
    List<String> images=new ArrayList<>();
    List<String> titles=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changsha);
        banner=(Banner) findViewById(R.id.banner);
        initDate();
        initView();
    }
    public void initDate()
    {
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/e769e34340d6142380d93052fde7e1e2.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/0f1859a1409a57bd8015063d2e02ec72.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/11ce429a40d5f400807f3535c368eb8e.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/3890c61940bd3b7880ef56d117db6f33.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/aaf5c10e40b80a05802fb9a0355f703f.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/8539458940a0bc5d80e2d7233a2c0040.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/c89b9f82404c21fb8009fe3040b58289.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/b66e668340343a49800f5d9d2cbf9bec.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/edc517c440bd4e42800d7bc10f9b8220.jpg");
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