package db;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Post extends BmobObject implements Serializable {
    private String PostName;

    private int PostImageId;
    private String PostTel;
    private String PostLoc;
    private String PostProfile;
    private int PostOrderNum;
    private double largePrice;
    private double smallPrice;
    private String PostNotice1;
    private String PostNotice2;
    private String PostNotice3;
    private String PostHolderSay;
    private String PostOpenTime;
    private int largerNum;
    private int smallNum;

    private BmobFile PostImage;
    //此驿站的管理者
    private Manager manager;

    public Post(String postName, String postTel, String postLoc, String postProfile, int postOrderNum, double largePrice, double smallPrice, String postNotice1, String postNotice2, String postNotice3, String postHolderSay, String postOpenTime, int largerNum, int smallNum, Manager manager) {
        PostName = postName;
        PostTel = postTel;
        PostLoc = postLoc;
        PostProfile = postProfile;
        PostOrderNum = postOrderNum;
        this.largePrice = largePrice;
        this.smallPrice = smallPrice;
        PostNotice1 = postNotice1;
        PostNotice2 = postNotice2;
        PostNotice3 = postNotice3;
        PostHolderSay = postHolderSay;
        PostOpenTime = postOpenTime;
        this.largerNum = largerNum;
        this.smallNum = smallNum;
        this.manager = manager;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public BmobFile getPostImage() {
        return PostImage;
    }

    public void setPostImage(BmobFile postImage) {
        PostImage = postImage;
    }

    public String getPostName() {
        return PostName;
    }

    public void setPostName(String postName) {
        PostName = postName;
    }

    public int getPostImageId() {
        return PostImageId;
    }

    public void setPostImageId(int postImageId) {
        PostImageId = postImageId;
    }

    public String getPostTel() {
        return PostTel;
    }

    public void setPostTel(String postTel) {
        PostTel = postTel;
    }

    public String getPostLoc() {
        return PostLoc;
    }

    public void setPostLoc(String postLoc) {
        PostLoc = postLoc;
    }

    public String getPostProfile() {
        return PostProfile;
    }

    public void setPostProfile(String postProfile) {
        PostProfile = postProfile;
    }

    public int getPostOrderNum() {
        return PostOrderNum;
    }

    public void setPostOrderNum(int postOrderNum) {
        PostOrderNum = postOrderNum;
    }

    public double getLargePrice() {
        return largePrice;
    }

    public void setLargePrice(double largePrice) {
        this.largePrice = largePrice;
    }

    public double getSmallPrice() {
        return smallPrice;
    }

    public void setSmallPrice(double smallPrice) {
        this.smallPrice = smallPrice;
    }

    public String getPostNotice1() {
        return PostNotice1;
    }

    public void setPostNotice1(String postNotice1) {
        PostNotice1 = postNotice1;
    }

    public String getPostNotice2() {
        return PostNotice2;
    }

    public void setPostNotice2(String postNotice2) {
        PostNotice2 = postNotice2;
    }

    public String getPostNotice3() {
        return PostNotice3;
    }

    public void setPostNotice3(String postNotice3) {
        PostNotice3 = postNotice3;
    }

    public String getPostHolderSay() {
        return PostHolderSay;
    }

    public void setPostHolderSay(String postHolderSay) {
        PostHolderSay = postHolderSay;
    }

    public String getPostOpenTime() {
        return PostOpenTime;
    }

    public void setPostOpenTime(String postOpenTime) {
        PostOpenTime = postOpenTime;
    }

    public int getLargerNum() {
        return largerNum;
    }

    public void setLargerNum(int largerNum) {
        this.largerNum = largerNum;
    }

    public int getSmallNum() {
        return smallNum;
    }

    public void setSmallNum(int smallNum) {
        this.smallNum = smallNum;
    }
}