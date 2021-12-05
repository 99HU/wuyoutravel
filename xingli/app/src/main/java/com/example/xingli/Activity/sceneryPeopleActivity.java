package com.example.xingli.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xingli.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import Adapter.SceneryRecAdapter;
import db.City;
import db.Scenery;

public class sceneryPeopleActivity extends AppCompatActivity {
    TextView cityName;
    TextView mainSceneryName1;
    TextView mainSceneryName2;
    TextView mainSceneryName3;
    TextView mainSceneryName4;
    TextView mainSceneryTime1;
    TextView mainSceneryTime2;
    TextView mainSceneryTime3;
    TextView mainSceneryTime4;
    TextView mainSceneryUpdateTime;
    TextView mainSceneryNowNum;
    TextView mainSceneryFillNum;
    RecyclerView sceneryRecycleView;
    String CityName;
    SceneryRecAdapter adapterRec;
    List<Scenery> SceneryList=new ArrayList<>();
    private List<City> CityList=new ArrayList<>();
    public String randomTime()
    {
        String s1="09:00-21:30";
        String s2="09:00-18:00";
        String s3="09:00-17:00";
        String s4="09:00-22:00";
        String s5="09:00-21:00";
        String s6="08:30-17:00";
        String s7="09:00-16:30";
        String s8="08:30-21:30";
        String s9="08:30-16:30";
        String s10="00:00-17:00";
        List<String> timeList=new ArrayList<>();
        timeList.add(s1); timeList.add(s2); timeList.add(s3); timeList.add(s4); timeList.add(s5);
        timeList.add(s6); timeList.add(s7); timeList.add(s8); timeList.add(s9); timeList.add(s10);
        Random random = new Random();
        int i=random.nextInt(10)+1;
        return timeList.get(i-1);
    }
    public String getFillNumString(int i)
    {
        String s1="人/"+String.valueOf(i)+"人";
        return s1;
    }
    public int  randomFillNum()
    {
        int i1=15000;
        int i2=96000;
        int i3=13500;
        int i4=15000;
        int i5=5900;
        int i6=3161;
        int i7=9533;
        int i8=4933;
        int i9=1200;
        int i10=45000;
        List<Integer> numList=new ArrayList<>();
        numList.add(i1); numList.add(i2); numList.add(i3); numList.add(i4); numList.add(i5);
        numList.add(i6); numList.add(i7); numList.add(i8); numList.add(i9); numList.add(i10);
        Random random = new Random();
        int i=random.nextInt(10)+1;
        return numList.get(i-1);
    }
    public int randomNowNum(int i)
    {
        Random random = new Random();
        int x=random.nextInt(i);
        return  x;
    }
    public int getHour(int a)
    {
        switch (a){
            case 0:
                return 0;
            case 1:
                return 0;
            case 2:
                return 2;
            case 3:
                return 2;
            case 4:
                return 4;
            case 5:
                return 4;
            case 6:
                return 6;
            case 7:
                return 6;
            case 8:
                return 8;
            case 9:
                return 8;
            case 10:
                return 10;
            case 11:
                return 10;
        }
        return 0;
    }
    private void init(){
        Scenery s1=new Scenery("华山","5A级","华山");
        Scenery s2=new Scenery("秦始皇陵博物馆（兵马俑）","5A级","临潼兵马俑旅游区");
        Scenery s3=new Scenery("西安城墙","5A级","钟鼓楼/回民街");
        Scenery s4=new Scenery("华清宫","5A级","临潼兵马俑旅游区");
        Scenery s5=new Scenery("西安钟楼","5A级","钟鼓楼/回民街");
        Scenery s6=new Scenery("大明宫国家遗址公园","5A级","大明宫遗址公园");
        Scenery s7=new Scenery("大唐芙蓉园","5A级","大雁塔/曲江度假区");
        Scenery s8=new Scenery("秦岭野生动物园","5A级","秦岭野生动物园");
        Scenery s9=new Scenery("西安碑林博物馆","5A级","钟鼓楼/回民街");
        Scenery s10=new Scenery("曲江海洋极地公园","5A级","大雁塔/曲江度假区");
        SceneryList.add(s1); SceneryList.add(s2); SceneryList.add(s3); SceneryList.add(s4); SceneryList.add(s5);
        SceneryList.add(s6); SceneryList.add(s7); SceneryList.add(s8); SceneryList.add(s9); SceneryList.add(s10);
        City xian=new City("西安",SceneryList);
        CityList.add(xian);
        Intent intent=getIntent();
        CityName=intent.getStringExtra("city");
        cityName=(TextView) findViewById(R.id.city_name);
        mainSceneryName1=(TextView) findViewById(R.id.main_scnery_name1);
        mainSceneryName2=(TextView) findViewById(R.id.main_scnery_name2);
        mainSceneryName3=(TextView) findViewById(R.id.main_scnery_name3);
        mainSceneryName4=(TextView) findViewById(R.id.main_scnery_name4);
        mainSceneryTime1=(TextView) findViewById(R.id.main_scenery_time1);
        mainSceneryTime2=(TextView) findViewById(R.id.main_scenery_time2);
        mainSceneryTime3=(TextView) findViewById(R.id.main_scenery_time3);
        mainSceneryTime4=(TextView) findViewById(R.id.main_scenery_time4);
        mainSceneryUpdateTime=(TextView) findViewById(R.id.main_scenery_update_time);
        mainSceneryFillNum=(TextView) findViewById(R.id.fill_num);
        mainSceneryNowNum=(TextView) findViewById(R.id.now_num);
        sceneryRecycleView=(RecyclerView) findViewById(R.id.scenery_num_list);

        cityName.setText(CityName);
        mainSceneryName1.setText(xian.getScenery().get(0).getSceneryName());
        mainSceneryName2.setText(xian.getScenery().get(1).getSceneryName());
        mainSceneryName3.setText(xian.getScenery().get(2).getSceneryName());
        mainSceneryName4.setText(xian.getScenery().get(3).getSceneryName());
        mainSceneryTime1.setText(randomTime());
        mainSceneryTime2.setText(randomTime());
        mainSceneryTime3.setText(randomTime());
        mainSceneryTime4.setText(randomTime());
        Calendar c = Calendar.getInstance();
        mainSceneryUpdateTime.setText(String.valueOf(c.get(Calendar.YEAR))+"-"+String.valueOf(c.get(Calendar.MONTH)+1)
                +"-"+String.valueOf(c.get(Calendar.DAY_OF_MONTH))+" "+String.valueOf(getHour(c.get(Calendar.HOUR)))
                +":00:00");
        int num=randomFillNum();
        mainSceneryFillNum.setText(getFillNumString(num));
        mainSceneryNowNum.setText(String.valueOf(randomNowNum(num)));
        LinearLayoutManager layoutManager=new LinearLayoutManager(sceneryPeopleActivity.this);
        sceneryRecycleView.setLayoutManager(layoutManager);
        adapterRec=new SceneryRecAdapter(xian.getScenery().subList(4,xian.getScenery().size()),sceneryPeopleActivity.this);
        sceneryRecycleView.setAdapter(adapterRec);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenery_people);
        init();
    }
}