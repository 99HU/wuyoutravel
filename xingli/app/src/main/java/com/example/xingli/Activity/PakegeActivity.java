package com.example.xingli.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.location.PoiRegion;
import com.baidu.mapapi.SDKInitializer;
import com.example.xingli.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.PostRecAdapter;
import base.BaseActivity1;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import db.Post;
import db.User;

public class PakegeActivity extends BaseActivity1 {
    User user;
    public static Post poststart;
    public TextView startPost;
    ImageView chooseStartPost;
    ImageView seeLocation;
    TextView location;
    LocationClientOption option ;
    public LocationClient mlocationClient;
    public BDLocation location1;
    public RecyclerView pakegeListView;
    public EditText editText;
    private List<Post> PostList=new ArrayList<>();
    PostRecAdapter adapterRec;
    private void init(){
        user =User.fetchUserInfo();
        BmobQuery<Post> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                for (Post post : list) {
                    PostList.add(post);
                }
                //??????listview
                pakegeListView=(RecyclerView) findViewById(R.id.pakege_listview);
                LinearLayoutManager layoutManager=new LinearLayoutManager(PakegeActivity.this);
                pakegeListView.setLayoutManager(layoutManager);
                adapterRec=new PostRecAdapter(PostList,PakegeActivity.this);
                pakegeListView.setAdapter(adapterRec);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pakege);
        chooseStartPost=(ImageView) findViewById(R.id.update_location);
        seeLocation=(ImageView) findViewById(R.id.see_location);
        location=(TextView) findViewById(R.id.location_textview);
        editText=(EditText)findViewById(R.id.search_post);
        startPost=(TextView)findViewById(R.id.start_post);
        init();


        updaLoc();
        chooseStartPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PakegeActivity.this,chooseStartPostActivity.class);
                intent.putExtra("user",user);
                startActivityForResult(intent,1);
            }
        });
        seeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PakegeActivity.this,MapActivity.class);
                intent.putExtra("location_data",location1);
                startActivity(intent);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterRec.getFilter().filter(s);
                Log.d("PakegeActivity","????????????????????????");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK) {
                    poststart=(Post) data.getSerializableExtra("poststart");
                    startPost.setText(poststart.getPostName()+"  "+poststart.getPostLoc());
                }
                break;
            default:
        }

    }
    private void requestLocation(){
        mlocationClient.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    for(int result:grantResults){
                        if(result!=PackageManager.PERMISSION_GRANTED)
                        {
                            Toast.makeText(this,"?????????????????????????????????????????????",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    Toast.makeText(this,"??????????????????",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }
    public void updaLoc()
    {
        mlocationClient=new LocationClient(getApplicationContext());
        mlocationClient.registerLocationListener(new MylocationListener());
        SDKInitializer.initialize(getApplicationContext());
        option = new LocationClientOption();
        //??????????????????gps
        option.setOpenGps(true);
        //??????????????????
        option.setIsNeedAddress(true);
        option.setNeedNewVersionRgc(true);
        //????????????????????????
        option.setIsNeedLocationDescribe(true);
        //????????????POI
        option.setIsNeedLocationPoiList(true);
        option.setCoorType("bd09ll"); // ??????????????????
        option.setScanSpan(1000);
        mlocationClient.setLocOption(option);
        List<String> permissionList=new ArrayList<>();
        if (ContextCompat.checkSelfPermission(PakegeActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);}
        if (ContextCompat.checkSelfPermission(PakegeActivity.this,
                Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);}
        if (ContextCompat.checkSelfPermission(PakegeActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);}
        if (!permissionList.isEmpty()){
            String []permissions=permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(PakegeActivity.this,permissions,1);
        }else {
            requestLocation();
        }
    }
    public class MylocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder currentPositon=new StringBuilder();
                    location1=bdLocation;
                    Poi poi = bdLocation.getPoiList().get(0);
                    String poiName = poi.getName();    //??????POI??????
                    String poiTags = poi.getTags();    //??????POI??????
                    String poiAddr = poi.getAddr();    //??????POI?????? //????????????POI??????
                    PoiRegion poiRegion= bdLocation.getPoiRegion();
                    String poiDerectionDesc = poiRegion.getDerectionDesc();    //??????PoiRegion????????????
                    String poiRegionName = poiRegion.getName();    //??????PoiRegion??????
                    String poiTags1 = poiRegion.getTags();    //??????PoiRegion??????
                    currentPositon.append("???????????????").append(poiAddr).append("\n");
                    location.setText(currentPositon);
                }
            });
        }
    }

}