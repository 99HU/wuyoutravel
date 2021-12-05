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

public class xueshanActivity extends AppCompatActivity {

    Banner banner;
    MyImageLoader mImageLoader=new MyImageLoader();
    List<String> images=new ArrayList<>();
    List<String> titles=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xueshan);
        banner=(Banner) findViewById(R.id.xueshan_banner);
        initDate();
        initView();
    }
    public void initDate()
    {
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/c138c9a140f37dc68096807e159a0e82.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/db0d002c40b12b1680b11b6c93560b52.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/cd6b5b4440e5a92e801613e300a5249a.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/1313d83640d8488b80b472fa87a1d8aa.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/cc8d500940c66ec180590e41d9b9fb8f.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/4f445ae7402f9e6d809ba47df6748527.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/e271b71740880f6d80f3263b0a89f264.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/07a75316407ed0a48002753a0f6d271d.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/50cb7a404050236d80bc06b219e13a34.jpg");
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