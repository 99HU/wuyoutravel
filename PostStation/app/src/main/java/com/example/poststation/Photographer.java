package com.example.poststation;

import java.io.Serializable;

public class Photographer implements Serializable {
    String PhotographerName;
    int PhotographerId;
    int PhotographerImageId;
    String PhotographerTel;
    String PhotographerGender;
    int PhotographerAge;
    String PhotographerLoc;
    String PhotographerProfile;
    int PhotographerMouthNum;
    int PhtotgrapherImageShowId1;
    String photographerImageshowText1;
    int PhtotgrapherImageShowId2;
    String photographerImageshowText2;
    int PhtotgrapherImageShowId3;
    String photographerImageshowText3;
    double PhotographerPrice;
    double PhotographerClothPrice;
    String photographerSay;
    String notice1;
    String notice2;
    String notice3;

    public Photographer(String photographerName, int photographerId, int photographerImageId, String photographerTel, String photographerGender, int photographerAge, String photographerLoc, String photographerProfile, int photographerMouthNum, int phtotgrapherImageShowId1, String photographerImageshowText1, int phtotgrapherImageShowId2, String photographerImageshowText2, int phtotgrapherImageShowId3, String photographerImageshowText3, double photographerPrice, double photographerClothPrice, String photographerSay, String notice1, String notice2, String notice3) {
        PhotographerName = photographerName;
        PhotographerId = photographerId;
        PhotographerImageId = photographerImageId;
        PhotographerTel = photographerTel;
        PhotographerGender = photographerGender;
        PhotographerAge = photographerAge;
        PhotographerLoc = photographerLoc;
        PhotographerProfile = photographerProfile;
        PhotographerMouthNum = photographerMouthNum;
        PhtotgrapherImageShowId1 = phtotgrapherImageShowId1;
        this.photographerImageshowText1 = photographerImageshowText1;
        PhtotgrapherImageShowId2 = phtotgrapherImageShowId2;
        this.photographerImageshowText2 = photographerImageshowText2;
        PhtotgrapherImageShowId3 = phtotgrapherImageShowId3;
        this.photographerImageshowText3 = photographerImageshowText3;
        PhotographerPrice = photographerPrice;
        PhotographerClothPrice = photographerClothPrice;
        this.photographerSay = photographerSay;
        this.notice1 = notice1;
        this.notice2 = notice2;
        this.notice3 = notice3;
    }

    public String getPhotographerName() {
        return PhotographerName;
    }

    public void setPhotographerName(String photographerName) {
        PhotographerName = photographerName;
    }

    public int getPhotographerId() {
        return PhotographerId;
    }

    public void setPhotographerId(int photographerId) {
        PhotographerId = photographerId;
    }

    public int getPhotographerImageId() {
        return PhotographerImageId;
    }

    public void setPhotographerImageId(int photographerImageId) {
        PhotographerImageId = photographerImageId;
    }

    public String getPhotographerTel() {
        return PhotographerTel;
    }

    public void setPhotographerTel(String photographerTel) {
        PhotographerTel = photographerTel;
    }

    public String getPhotographerGender() {
        return PhotographerGender;
    }

    public void setPhotographerGender(String photographerGender) {
        PhotographerGender = photographerGender;
    }

    public int getPhotographerAge() {
        return PhotographerAge;
    }

    public void setPhotographerAge(int photographerAge) {
        PhotographerAge = photographerAge;
    }

    public String getPhotographerLoc() {
        return PhotographerLoc;
    }

    public void setPhotographerLoc(String photographerLoc) {
        PhotographerLoc = photographerLoc;
    }

    public String getPhotographerProfile() {
        return PhotographerProfile;
    }

    public void setPhotographerProfile(String photographerProfile) {
        PhotographerProfile = photographerProfile;
    }

    public int getPhotographerMouthNum() {
        return PhotographerMouthNum;
    }

    public void setPhotographerMouthNum(int photographerMouthNum) {
        PhotographerMouthNum = photographerMouthNum;
    }

    public int getPhtotgrapherImageShowId1() {
        return PhtotgrapherImageShowId1;
    }

    public void setPhtotgrapherImageShowId1(int phtotgrapherImageShowId1) {
        PhtotgrapherImageShowId1 = phtotgrapherImageShowId1;
    }

    public String getPhotographerImageshowText1() {
        return photographerImageshowText1;
    }

    public void setPhotographerImageshowText1(String photographerImageshowText1) {
        this.photographerImageshowText1 = photographerImageshowText1;
    }

    public int getPhtotgrapherImageShowId2() {
        return PhtotgrapherImageShowId2;
    }

    public void setPhtotgrapherImageShowId2(int phtotgrapherImageShowId2) {
        PhtotgrapherImageShowId2 = phtotgrapherImageShowId2;
    }

    public String getPhotographerImageshowText2() {
        return photographerImageshowText2;
    }

    public void setPhotographerImageshowText2(String photographerImageshowText2) {
        this.photographerImageshowText2 = photographerImageshowText2;
    }

    public int getPhtotgrapherImageShowId3() {
        return PhtotgrapherImageShowId3;
    }

    public void setPhtotgrapherImageShowId3(int phtotgrapherImageShowId3) {
        PhtotgrapherImageShowId3 = phtotgrapherImageShowId3;
    }

    public String getPhotographerImageshowText3() {
        return photographerImageshowText3;
    }

    public void setPhotographerImageshowText3(String photographerImageshowText3) {
        this.photographerImageshowText3 = photographerImageshowText3;
    }

    public double getPhotographerPrice() {
        return PhotographerPrice;
    }

    public void setPhotographerPrice(double photographerPrice) {
        PhotographerPrice = photographerPrice;
    }

    public double getPhotographerClothPrice() {
        return PhotographerClothPrice;
    }

    public void setPhotographerClothPrice(double photographerClothPrice) {
        PhotographerClothPrice = photographerClothPrice;
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
