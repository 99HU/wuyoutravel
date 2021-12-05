package db;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class Photographer extends BmobUser {
    public static String PhotographerOnly="只提供摄影师跟拍服务";
    public static String PhotographerAndCloth="提供摄影师跟拍服务和配套服装";
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
    private String photographerLoc;
    //年龄
    private int age;
    //昵称
    private String nickName = "小影";
    //身份
    final private String identity = "摄影师";

    String profile;
    int mouthNum;
    BmobFile imageShow1;
    String imageShowText1;
    BmobFile imageShow2;
    String imageShowText2;
    BmobFile imageShow3;
    String imageShowText3;
    double priceIncludeUniform;
    double priceNoUniform;
    String photographerSay;
    String notice1;
    String notice2;
    String notice3;

    public Photographer(String photographerName, String photographerPassword) {
        this.setUsername(photographerName);
        this.setPassword(photographerPassword);
    }

    public BmobFile getHeadImage() {
        return headImage;
    }

    public void setHeadImage(BmobFile headImage) {
        this.headImage = headImage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhotographerLoc() {
        return photographerLoc;
    }

    public void setPhotographerLoc(String photographerLoc) {
        this.photographerLoc = photographerLoc;
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

    public String getIdentity() {
        return identity;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getMouthNum() {
        return mouthNum;
    }

    public void setMouthNum(int mouthNum) {
        this.mouthNum = mouthNum;
    }

    public BmobFile getImageShow1() {
        return imageShow1;
    }

    public void setImageShow1(BmobFile imageShow1) {
        this.imageShow1 = imageShow1;
    }

    public String getImageShowText1() {
        return imageShowText1;
    }

    public void setImageShowText1(String imageShowText1) {
        this.imageShowText1 = imageShowText1;
    }

    public BmobFile getImageShow2() {
        return imageShow2;
    }

    public void setImageShow2(BmobFile imageShow2) {
        this.imageShow2 = imageShow2;
    }

    public String getImageShowText2() {
        return imageShowText2;
    }

    public void setImageShowText2(String imageShowText2) {
        this.imageShowText2 = imageShowText2;
    }

    public BmobFile getImageShow3() {
        return imageShow3;
    }

    public void setImageShow3(BmobFile imageShow3) {
        this.imageShow3 = imageShow3;
    }

    public String getImageShowText3() {
        return imageShowText3;
    }

    public void setImageShowText3(String imageShowText3) {
        this.imageShowText3 = imageShowText3;
    }

    public double getPriceIncludeUniform() {
        return priceIncludeUniform;
    }

    public void setPriceIncludeUniform(double priceIncludeUniform) {
        this.priceIncludeUniform = priceIncludeUniform;
    }

    public double getPriceNoUniform() {
        return priceNoUniform;
    }

    public void setPriceNoUniform(double priceNoUniform) {
        this.priceNoUniform = priceNoUniform;
    }

    public String getPhotographerSay() {
        return photographerSay;
    }

    public void setPhotographerSay(String photographerSay) {
        this.photographerSay = photographerSay;
    }

    public String getNotice1() {
        return notice1;
    }

    public void setNotice1(String notice1) {
        this.notice1 = notice1;
    }

    public String getNotice2() {
        return notice2;
    }

    public void setNotice2(String notice2) {
        this.notice2 = notice2;
    }

    public String getNotice3() {
        return notice3;
    }

    public void setNotice3(String notice3) {
        this.notice3 = notice3;
    }
}
