package com.example.photographer;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;

public class Order extends BmobObject implements Serializable {
    public static String Pay="已支付";
    public static String notPat="未支付";
    public static String Accept="已接受";
    public static String notAccept="未接受";
    public static String Finish="已完成";
    public static String notFinish="未完成";
    public static String Post="行李订单";
    public static String PhotoGrapher="摄影订单";
    public static String Guide="向导订单";
    public static String PhotographerOnly="只提供拍摄服务";
    public static String PhotographerAndCloth="提供拍摄服务和拍摄服装准备服务";


//    int orderId;
//    int userId;
//    int guideId;
//    int providerStartId;
//    int providerEndId;
//    int photographerId;


    BmobDate creatTime;
    BmobDate doneTime;
    BmobDate putTime;
    BmobDate getTime;
    int orderBigNum;
    int orderSmallNum;
    double orderPrice;
    String orderStartLoc;

    int orderMessage;//取货凭证
    String orderPayState;
    String orderAcceptState;
    String orderFinishState;
    String orderType;


    Post startPost;
    Post endPost;
    User user;
    Photographer photographer;
    Guide guide;


    String photographerType;

    BmobFile PackageImage;
    //向导订单

//    public Order(int orderId , int userId, int guideId, Date creatTime, Date putTime, double orderPrice, String orderStartLoc, String orderPayState, String orderAcceptState, String orderFinishState, String orderType, Guide guide)
//    {
//        this.orderId=orderId;
//        this.userId=userId;
//        this.guideId=guideId;
//        this.creatTime=creatTime;
//        this.putTime=putTime;
//        this.orderPrice=orderPrice;
//        this.orderStartLoc=orderStartLoc;
//        this.orderPayState=orderPayState;
//        this.orderAcceptState=orderAcceptState;
//        this.orderFinishState=orderFinishState;
//        this.orderType=orderType;
//        this.guide=guide;
//    }

    //摄影师订单
//    public Order(int orderId ,int userId,int photographerId,Date creatTime,Date putTime,String photographerOrderType,double orderPrice,String orderStartLoc,String orderPayState, String orderAcceptState, String orderFinishState,String orderType,Photographer photographer)
//    {
//        this.orderId=orderId;
//        this.userId=userId;
//        this.photographerId=photographerId;
//        this.creatTime=creatTime;
//        this.putTime=putTime;
//        this.photographerOrderType=photographerOrderType;
//        this.orderPrice=orderPrice;
//        this.orderStartLoc=orderStartLoc;
//        this.orderPayState=orderPayState;
//        this.orderAcceptState=orderAcceptState;
//        this.orderFinishState=orderFinishState;
//        this.orderType=orderType;
//        this.photographer=photographer;
//    }
    //驿站订单
    public Order() {}

    public Order(BmobDate creatTime, BmobDate putTime, BmobDate getTime, int orderBigNum, int orderSmallNum, double orderPrice, String orderStartLoc, int orderMessage, String orderPayState, String orderAcceptState, String orderFinishState, String orderType, Post startPost, Post endPost, User user) {
        this.creatTime = creatTime;
        this.putTime = putTime;
        this.getTime = getTime;
        this.orderBigNum = orderBigNum;
        this.orderSmallNum = orderSmallNum;
        this.orderPrice = orderPrice;
        this.orderStartLoc = orderStartLoc;
        this.orderMessage = orderMessage;
        this.orderPayState = orderPayState;
        this.orderAcceptState = orderAcceptState;
        this.orderFinishState = orderFinishState;
        this.orderType = orderType;
        this.startPost = startPost;
        this.endPost = endPost;
        this.user = user;
    }


//    public Order(int orderId, int userId, int providerStartId, int providerEndId,Date creatTime, Date putTime, Date getTime, int orderBigNum, int orderSmallNum, double orderPrice, String orderStartLoc, String orderEndLoc,  int orderMessage, String orderPayState, String orderAcceptState, String orderFinishState,String orderType,Post startPost,Post endPost) {
//        this.orderId = orderId;
//        this.userId = userId;
//        this.providerStartId = providerStartId;
//        this.providerEndId=providerEndId;
//        this.creatTime = creatTime;
//        this.putTime = putTime;
//        this.getTime = getTime;
//        this.orderBigNum = orderBigNum;
//        this.orderSmallNum = orderSmallNum;
//        this.orderPrice = orderPrice;
//        this.orderStartLoc = orderStartLoc;
//        this.orderEndLoc = orderEndLoc;
//        this.orderTask = orderTask;
//        this.orderMessage = orderMessage;
//        this.orderPayState = orderPayState;
//        this.orderAcceptState = orderAcceptState;
//        this.orderFinishState = orderFinishState;
//        this.orderType=orderType;
//        this.startPost=startPost;
//        this.endPost=endPost;
//    }


    public BmobFile getPackageImage() {
        return PackageImage;
    }

    public void setPackageImage(BmobFile packageImage) {
        PackageImage = packageImage;
    }

    public static String getGuide() {
        return Guide;
    }
    public Guide getguide()
    {
        return guide;
    }
    public void setGuide(Guide guide) {
        this.guide = guide;
    }
    public static void setGuide(String guide) {
        Guide = guide;
    }

//    public int getGuideId() {
//        return guideId;
//    }
//
//    public void setGuideId(int guideId) {
//        this.guideId = guideId;
//    }

    public static String getPost() {
        return Post;
    }

    public static void setPost(String post) {
        Post = post;
    }

    public static String getPhotoGrapher() {
        return PhotoGrapher;
    }

    public static void setPhotoGrapher(String photoGrapher) {
        PhotoGrapher = photoGrapher;
    }

    public static String getGuider() {
        return Guide;
    }

    public static void setGuider(String guider) {
        Guide = guider;
    }

    public static String getPhotographerOnly() {
        return PhotographerOnly;
    }

    public static void setPhotographerOnly(String photographerOnly) {
        PhotographerOnly = photographerOnly;
    }

    public static String getPhotographerAndCloth() {
        return PhotographerAndCloth;
    }

    public static void setPhotographerAndCloth(String photographerAndCloth) {
        PhotographerAndCloth = photographerAndCloth;
    }

//    public int getPhotographerId() {
//        return photographerId;
//    }
//
//    public void setPhotographerId(int photographerId) {
//        this.photographerId = photographerId;
//    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Photographer getPhotographer() {
        return photographer;
    }

    public void setPhotographer(Photographer photographer) {
        this.photographer = photographer;
    }

    public String getPhotographerType() {
        return photographerType;
    }

    public void setPhotographerType(String photographerType) {
        this.photographerType = photographerType;
    }

    public static String getPay() {
        return Pay;
    }

    public static void setPay(String pay) {
        Pay = pay;
    }

    public static String getNotPat() {
        return notPat;
    }

    public static void setNotPat(String notPat) {
        Order.notPat = notPat;
    }

    public static String getAccept() {
        return Accept;
    }

    public static void setAccept(String accept) {
        Accept = accept;
    }

    public static String getNotAccept() {
        return notAccept;
    }

    public static void setNotAccept(String notAccept) {
        Order.notAccept = notAccept;
    }

    public static String getFinish() {
        return Finish;
    }

    public static void setFinish(String finish) {
        Finish = finish;
    }

    public static String getNotFinish() {
        return notFinish;
    }

    public static void setNotFinish(String notFinish) {
        Order.notFinish = notFinish;
    }

//    public int getOrderId() {
//        return orderId;
//    }
//
//    public void setOrderId(int orderId) {
//        this.orderId = orderId;
//    }
//
//    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }
//
//    public int getProviderStartId() {
//        return providerStartId;
//    }
//
//    public void setProviderStartId(int providerStartId) {
//        this.providerStartId = providerStartId;
//    }
//
//    public int getProviderEndId() {
//        return providerEndId;
//    }
//
//    public void setProviderEndId(int providerEndId) {
//        this.providerEndId = providerEndId;
//    }

    public BmobDate getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(BmobDate creatTime) {
        this.creatTime = creatTime;
    }

    public BmobDate getDoneTime() {
        return doneTime;
    }

    public void setDoneTime(BmobDate doneTime) {
        this.doneTime = doneTime;
    }

    public BmobDate getPutTime() {
        return putTime;
    }

    public void setPutTime(BmobDate putTime) {
        this.putTime = putTime;
    }

    public BmobDate getGetTime() {
        return getTime;
    }

    public void setGetTime(BmobDate getTime) {
        this.getTime = getTime;
    }

    public int getOrderBigNum() {
        return orderBigNum;
    }

    public void setOrderBigNum(int orderBigNum) {
        this.orderBigNum = orderBigNum;
    }

    public int getOrderSmallNum() {
        return orderSmallNum;
    }

    public void setOrderSmallNum(int orderSmallNum) {
        this.orderSmallNum = orderSmallNum;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderStartLoc() {
        return orderStartLoc;
    }

    public void setOrderStartLoc(String orderStartLoc) {
        this.orderStartLoc = orderStartLoc;
    }


    public int getOrderMessage() {
        return orderMessage;
    }

    public void setOrderMessage(int orderMessage) {
        this.orderMessage = orderMessage;
    }

    public String getOrderPayState() {
        return orderPayState;
    }

    public void setOrderPayState(String orderPayState) {
        this.orderPayState = orderPayState;
    }

    public String getOrderAcceptState() {
        return orderAcceptState;
    }

    public void setOrderAcceptState(String orderAcceptState) {
        this.orderAcceptState = orderAcceptState;
    }

    public String getOrderFinishState() {
        return orderFinishState;
    }

    public void setOrderFinishState(String orderFinishState) {
        this.orderFinishState = orderFinishState;
    }

    public Post getStartPost() {
        return startPost;
    }

    public void setStartPost(Post startPost) {
        this.startPost = startPost;
    }

    public Post getEndPost() {
        return endPost;
    }

    public void setEndPost(Post endPost) {
        this.endPost = endPost;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
