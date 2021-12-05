package com.example.xingli.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.location.PoiRegion;
import com.baidu.mapapi.SDKInitializer;
import com.bumptech.glide.Glide;
import com.example.xingli.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FirstViewModel mViewModel;
    private ImageView startPakegeActivity;
    private ImageView startWeatherActivity;
    private ImageView startBudgetActivity;
    private ImageView startPhotographerActivity;
    private ImageView startGuideActivity;
    private ImageView updateLocation;
    private ImageView seeLocation;
    private TextView location;
    LocationClientOption option ;
    public LocationClient mlocationClient;
    public BDLocation location1;
    private Banner banner;
    MyImageLoader mImageLoader=new MyImageLoader();
    List<String> images=new ArrayList<>();
    List<String> titles=new ArrayList<>();

    public static FirstFragment newInstance() {
        return new FirstFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.first_fragment, container, false);
        startPakegeActivity=(ImageView) view.findViewById(R.id.pakege_activity);
        startPakegeActivity.setOnClickListener(new PakegeListener());
        startWeatherActivity=(ImageView)view.findViewById(R.id.weather_activity);
        startBudgetActivity=(ImageView) view.findViewById(R.id.budget_activity);
        updateLocation=(ImageView)view.findViewById(R.id.first_update_location);
        seeLocation=(ImageView)view.findViewById(R.id.first_see_location);
        location=(TextView)view.findViewById(R.id.first_location_textview);
        banner=(Banner)view.findViewById(R.id.first_banner);

        updaLoc();
        startWeatherActivity.setOnClickListener(new WeatherListener());
        startBudgetActivity.setOnClickListener(new BudgetListener());
        startPhotographerActivity=(ImageView) view.findViewById(R.id.shot_activity);
        startGuideActivity=(ImageView) view.findViewById(R.id.guide_activity);
        startPhotographerActivity.setOnClickListener(new photographerListener());
        startGuideActivity.setOnClickListener(new guideListener());
        updateLocation.setOnClickListener(new UpdateLocationListener());
        seeLocation.setOnClickListener(new SeeLocationListener());

        initDate();
        initView();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(FirstViewModel.class);
        // TODO: Use the ViewModel
    }
    class SeeLocationListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(getActivity(),MapActivity.class);
            intent.putExtra("location_data",location1);
            startActivity(intent);
        }
    }
    class UpdateLocationListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {
            updaLoc();
        }
    }
    class photographerListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(getActivity(),photographerActivity.class);
            getActivity().startActivity(intent);
        }
    }
    class guideListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(getActivity(),GuideActivity.class);
            getActivity().startActivity(intent);
        }
    }
    class BudgetListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent=new Intent();
            intent.setClass(getActivity(),BudgetActivity.class);
            getActivity().startActivity(intent);
        }
    }
    class WeatherListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), juhe.MainActivity.class);  //从前者跳到后者，特别注意的是，在fragment中，用getActivity()来获取当前的activity
            getActivity().startActivity(intent);
        }
    }
    class PakegeListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), PakegeActivity.class);  //从前者跳到后者，特别注意的是，在fragment中，用getActivity()来获取当前的activity
            getActivity().startActivity(intent);

        }
    }

    public void updaLoc()
    {

        mlocationClient=new LocationClient(getActivity().getApplicationContext());
        mlocationClient.registerLocationListener(new FirstFragment.MylocationListener());
        SDKInitializer.initialize(getActivity().getApplicationContext());
        option = new LocationClientOption();
        //设置是否使用gps
        option.setOpenGps(true);
        //获取详细地址
        option.setIsNeedAddress(true);
        option.setNeedNewVersionRgc(true);
        //获取详细地址描述
        option.setIsNeedLocationDescribe(true);
        //获取周边POI
        option.setIsNeedLocationPoiList(true);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mlocationClient.setLocOption(option);
        List<String> permissionList=new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);}
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);}
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);}
        if (!permissionList.isEmpty()){
            String []permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(),permissions,1);
        }else {
            requestLocation();
        }
    }

    private void requestLocation(){
        mlocationClient.start();
    }
    public class MylocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder currentPositon=new StringBuilder();
                    location1=bdLocation;
                    Poi poi = bdLocation.getPoiList().get(0);
                    String poiName = poi.getName();    //获取POI名称
                    String poiTags = poi.getTags();    //获取POI类型
                    String poiAddr = poi.getAddr();    //获取POI地址 //获取周边POI信息
                    PoiRegion poiRegion= bdLocation.getPoiRegion();
                    String poiDerectionDesc = poiRegion.getDerectionDesc();    //获取PoiRegion位置关系
                    String poiRegionName = poiRegion.getName();    //获取PoiRegion名称
                    String poiTags1 = poiRegion.getTags();    //获取PoiRegion类型
                    currentPositon.append("当前位置：").append(poiAddr).append("\n");
                    location.setText(currentPositon);
                }
            });
        }
    }

    private class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context)
                    .load(path)
                    .into(imageView);
        }
    }
    public void initDate()
    {
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/e769e34340d6142380d93052fde7e1e2.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/e4bb91764005c04580692ac09d973117.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/92ad9d0740dfc1e9808c44bbe0ec8107.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/06fa5537403aa37480c963a97ad2831f.jpg");
        images.add("http://www.wuyoutravel.diamondog.online/2021/10/26/c138c9a140f37dc68096807e159a0e82.jpg");
        titles.add("长沙旅游|人均1000，四天三晚的长沙攻略");
        titles.add( "杭州桂花季|灵隐寺满觉陇一日攻略（附地图）");
        titles.add("青岛学生党三天两夜超全旅游攻略");
        titles.add( "意外发现比西双版纳曼听公园还美的取景地");
        titles.add("在离雪山最近的地方感受一日美好|宛如仙境");
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
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (position==0)
                {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), changshaActivity.class);  //从前者跳到后者，特别注意的是，在fragment中，用getActivity()来获取当前的activity
                    getActivity().startActivity(intent);
                }
                if (position==1)
                {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), hangzhouActivity.class);  //从前者跳到后者，特别注意的是，在fragment中，用getActivity()来获取当前的activity
                    getActivity().startActivity(intent);
                }
                if (position==2)
                {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), qingdaoActivity.class);  //从前者跳到后者，特别注意的是，在fragment中，用getActivity()来获取当前的activity
                    getActivity().startActivity(intent);
                }
                if (position==3)
                {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), xishuangActivity.class);  //从前者跳到后者，特别注意的是，在fragment中，用getActivity()来获取当前的activity
                    getActivity().startActivity(intent);
                }
                if (position==4)
                {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), xueshanActivity.class);  //从前者跳到后者，特别注意的是，在fragment中，用getActivity()来获取当前的activity
                    getActivity().startActivity(intent);
                }
            }
        });
    }
}