package com.example.photographer;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;

//此为驿站管理员类
public class Manager extends BmobUser {

    //账号
    //用户名
    //密码
    //邮箱
    //邮箱认证状态
    //手机号码
    //手机号码认证状态

    //头像
    private BmobFile headImage;
    //性别
    private String gender ;
    //位置
    private BmobGeoPoint managerLoc;
    //年龄
    private int age;
    //昵称
    private String nickName = "小驿";
    //身份
    final private String identity = "驿站管理者";
    //所管理驿站
    private Post post;

    public Manager(String managerName, String managerPassword) {
        this.setUsername(managerName);
        this.setPassword(managerPassword);
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public BmobFile getManagerImage() {
        return headImage;
    }

    public void setManagerImage(BmobFile headImage) {
        this.headImage = headImage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String managerGender) {
        this.gender = managerGender;
    }

    public BmobGeoPoint getManagerLoc() {
        return managerLoc;
    }

    public void setManagerLoc(BmobGeoPoint managerLoc) {
        this.managerLoc = managerLoc;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
