package com.example.xingli.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xingli.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.PostSimpleRecAdapter;
import base.BaseActivity1;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import db.Post;
import db.User;

public class chooseStartPostActivity extends BaseActivity1 {
    Post poststart;
    User user;
    public EditText searchStartPost;
    public RecyclerView startPostListView;
    PostSimpleRecAdapter adapterRec;

    private List<Post> PostList=new ArrayList<>();
    private void init(){

        BmobQuery<Post> bmobQuery = new BmobQuery<>();
        bmobQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                for (Post post : list) {
                    PostList.add(post);
                }

                LinearLayoutManager layoutManager=new LinearLayoutManager(chooseStartPostActivity.this);
                startPostListView.setLayoutManager(layoutManager);
                adapterRec=new PostSimpleRecAdapter(PostList,chooseStartPostActivity.this,getApplication());
                startPostListView.setAdapter(adapterRec);
            }
        });

//        Post p1=new Post("长安大学",1,R.drawable.changan,"18834161552","陕西省西安市雁塔区长安中路239号","长安大学（Chang’an University）座落于陕西省会西安，直属中华人民共和国教育部，是教育部和交通运输部、自然资源部、住房和城乡建设部、陕西省人民政府共建的国家“世界一流学科建设高校”，国家“211工程”重点建设大学，国家“985工程优势学科创新平台”建设高校；入选国家“111计划”、“双万计划”、教育部“卓越工程师教育培养计划”、国家建设高水平大学公派研究生项目、国家级大学生创新创业训练计划、国家级新工科研究与实践项目；是高水平行业特色大学优质资源共享联盟、中俄交通大学联盟成员高校，是中国政府奖学金来华留学生接收院校、国家大学生文化素质教育基地、首批高等学校科技成果转化和技术转移基地。"
//                ,10,10,5,"禁止寄存违禁物品,不可寄存贵重物品(包括不限于电脑、现金等)","请先下单再到店寄存，没有订单信息,无法寄存","存入日期0点前免费取消，存入日期24点前取消收取订单金额20%取消费，之后不可取消","大家好，非常高兴能帮各位寄存行李，我们靠近钟楼和回民街，欢迎大家的到来哦~",
//                "周一-周日00:00~24:00",10,5);
//        Post p2=new Post("中北大学",2,R.drawable.zhongbei,"15035711377","山西省太原市尖草坪区学院路3号","中北大学（North University of China）位于山西省太原市，是山西省人民政府与工业和信息化部、国家国防科技工业局共建高校，山西省重点建设大学，B8协同创新联盟、中国航天科技教育联盟、联合国教科文组织中国创业教育联盟、丝绸之路大学联盟、全国高等军工院校课程思政联盟单位，入选中西部高校基础能力建设工程、卓越工程师教育培养计划、国家级新工科研究与实践项目、国家级大学生创新创业训练计划、国家大学生文化素质教育基地、深化创新创业教育改革示范高校、山西省1331工程、山西省首批创新创业教育改革示范高校，国家二级保密单位，被誉为“人民兵工第一校” ",
//                12,12,6,"禁止寄存违禁物品,不可寄存贵重物品(包括不限于电脑、现金等)","请先下单再到店寄存，没有订单信息,无法寄存","存入日期0点前免费取消，存入日期24点前取消收取订单金额20%取消费，之后不可取消","大家好，非常高兴能帮各位寄存行李，我们靠近钟楼和回民街，欢迎大家的到来哦~",
//                "周一-周日00:00~24:00",10,5);
//        Post p3=new Post("海南大学",3,R.drawable.hainan,"13335448077","海南省儋州市那大镇宝岛新村","海南大学（Hainan University），简称海大，坐落于海南省海口市，是教育部与海南省人民政府“部省合建高校”、海南省人民政府与财政部共建高校、海南省属综合性重点大学、海南省“国内一流大学建设”重点支持高校，位列世界一流学科建设高校、“211工程”，入选“卓越工程师教育培养计划”、“卓越法律人才教育培养计划”、“卓越农林人才教育培养计划”、“中西部高校基础能力建设工程”、“中西部高校综合实力提升工程”、国家建设高水平大学公派研究生项目、中国政府奖学金来华留学生接收院校、教育部来华留学示范基地、国家大学生文化素质教育基地、国家级大学生创新创业训练计划，为中西部“一省一校”国家重点建设大学（Z14）联盟、CDIO工程教育联盟成员单位。学校由天津大学对口合作建设。",
//                8,12,5 ,"禁止寄存违禁物品,不可寄存贵重物品(包括不限于电脑、现金等)","请先下单再到店寄存，没有订单信息,无法寄存","存入日期0点前免费取消，存入日期24点前取消收取订单金额20%取消费，之后不可取消","大家好，非常高兴能帮各位寄存行李，我们靠近钟楼和回民街，欢迎大家的到来哦~",
//                "周一-周日00:00~24:00",10,5);
//        PostList.add(p1); PostList.add(p2); PostList.add(p3);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_start_post);
        searchStartPost=(EditText) findViewById(R.id.search_start_post);
        startPostListView=(RecyclerView) findViewById(R.id.start_post_listview);
        init();



        searchStartPost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapterRec.getFilter().filter(s);
                Log.d("PakegeActivity","检测文字的变化了");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

}