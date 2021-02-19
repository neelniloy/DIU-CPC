package com.sarker.cpc;

import java.io.Serializable;

public class EventInfo implements Serializable {


    String key,postTime,eTitle,eTime,eType,wingType,eRegister,eImage;

    EventInfo(){

    }

    public EventInfo(String key, String postTime, String eTitle, String eTime, String eType, String wingType, String eRegister, String eImage) {
        this.key = key;
        this.postTime = postTime;
        this.eTitle = eTitle;
        this.eTime = eTime;
        this.eType = eType;
        this.wingType = wingType;
        this.eRegister = eRegister;
        this.eImage = eImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String geteTitle() {
        return eTitle;
    }

    public void seteTitle(String eTitle) {
        this.eTitle = eTitle;
    }

    public String geteTime() {
        return eTime;
    }

    public void seteTime(String eTime) {
        this.eTime = eTime;
    }

    public String geteType() {
        return eType;
    }

    public void seteType(String eType) {
        this.eType = eType;
    }

    public String getWingType() {
        return wingType;
    }

    public void setWingType(String wingType) {
        this.wingType = wingType;
    }

    public String geteRegister() {
        return eRegister;
    }

    public void seteRegister(String eRegister) {
        this.eRegister = eRegister;
    }

    public String geteImage() {
        return eImage;
    }

    public void seteImage(String eImage) {
        this.eImage = eImage;
    }
}
