package com.example.photographer;

import java.io.Serializable;

public class Guide implements Serializable {
    String GuideName;
    int GuideId;
    int GuideImageId;
    String GuideTel;
    String GuideGender;
    int GuideAge;
    String GuideLoc;
    String GuideProfile;
    int GuideMouthNum;
    int GuideImageShowId1;
    String GuideImageshowText1;
    int GuideImageShowId2;
    String GuideImageshowText2;
    int GuideImageShowId3;
    String GuideImageshowText3;
    double GuidePrice;
    String GuideSay;
    String notice1;
    String notice2;
    String notice3;

    public Guide(String guideName, int guideId, int guideImageId, String guideTel, String guideGender, int guideAge, String guideLoc, String guideProfile, int guideMouthNum, int guideImageShowId1, String guideImageshowText1, int guideImageShowId2, String guideImageshowText2, int guideImageShowId3, String guideImageshowText3, double guidePrice, String guideSay, String notice1, String notice2, String notice3) {
        GuideName = guideName;
        GuideId = guideId;
        GuideImageId = guideImageId;
        GuideTel = guideTel;
        GuideGender = guideGender;
        GuideAge = guideAge;
        GuideLoc = guideLoc;
        GuideProfile = guideProfile;
        GuideMouthNum = guideMouthNum;
        GuideImageShowId1 = guideImageShowId1;
        GuideImageshowText1 = guideImageshowText1;
        GuideImageShowId2 = guideImageShowId2;
        GuideImageshowText2 = guideImageshowText2;
        GuideImageShowId3 = guideImageShowId3;
        GuideImageshowText3 = guideImageshowText3;
        GuidePrice = guidePrice;
        GuideSay = guideSay;
        this.notice1 = notice1;
        this.notice2 = notice2;
        this.notice3 = notice3;
    }

    public String getGuideName() {
        return GuideName;
    }

    public void setGuideName(String guideName) {
        GuideName = guideName;
    }

    public int getGuideId() {
        return GuideId;
    }

    public void setGuideId(int guideId) {
        GuideId = guideId;
    }

    public int getGuideImageId() {
        return GuideImageId;
    }

    public void setGuideImageId(int guideImageId) {
        GuideImageId = guideImageId;
    }

    public String getGuideTel() {
        return GuideTel;
    }

    public void setGuideTel(String guideTel) {
        GuideTel = guideTel;
    }

    public String getGuideGender() {
        return GuideGender;
    }

    public void setGuideGender(String guideGender) {
        GuideGender = guideGender;
    }

    public int getGuideAge() {
        return GuideAge;
    }

    public void setGuideAge(int guideAge) {
        GuideAge = guideAge;
    }

    public String getGuideLoc() {
        return GuideLoc;
    }

    public void setGuideLoc(String guideLoc) {
        GuideLoc = guideLoc;
    }

    public String getGuideProfile() {
        return GuideProfile;
    }

    public void setGuideProfile(String guideProfile) {
        GuideProfile = guideProfile;
    }

    public int getGuideMouthNum() {
        return GuideMouthNum;
    }

    public void setGuideMouthNum(int guideMouthNum) {
        GuideMouthNum = guideMouthNum;
    }

    public int getGuideImageShowId1() {
        return GuideImageShowId1;
    }

    public void setGuideImageShowId1(int guideImageShowId1) {
        GuideImageShowId1 = guideImageShowId1;
    }

    public String getGuideImageshowText1() {
        return GuideImageshowText1;
    }

    public void setGuideImageshowText1(String guideImageshowText1) {
        GuideImageshowText1 = guideImageshowText1;
    }

    public int getGuideImageShowId2() {
        return GuideImageShowId2;
    }

    public void setGuideImageShowId2(int guideImageShowId2) {
        GuideImageShowId2 = guideImageShowId2;
    }

    public String getGuideImageshowText2() {
        return GuideImageshowText2;
    }

    public void setGuideImageshowText2(String guideImageshowText2) {
        GuideImageshowText2 = guideImageshowText2;
    }

    public int getGuideImageShowId3() {
        return GuideImageShowId3;
    }

    public void setGuideImageShowId3(int guideImageShowId3) {
        GuideImageShowId3 = guideImageShowId3;
    }

    public String getGuideImageshowText3() {
        return GuideImageshowText3;
    }

    public void setGuideImageshowText3(String guideImageshowText3) {
        GuideImageshowText3 = guideImageshowText3;
    }

    public double getGuidePrice() {
        return GuidePrice;
    }

    public void setGuidePrice(double guidePrice) {
        GuidePrice = guidePrice;
    }

    public String getGuideSay() {
        return GuideSay;
    }

    public void setGuideSay(String guideSay) {
        GuideSay = guideSay;
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
