package com.tranzvision.gd.TZWeChatAppBundle.model;

import java.util.Date;

public class SignInfoT {
    private Integer id;

    private String openid;

    private String ibeaconName;

    private Date signTime;

    private String signAccuracy;

    private String nickName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    public String getIbeaconName() {
        return ibeaconName;
    }

    public void setIbeaconName(String ibeaconName) {
        this.ibeaconName = ibeaconName == null ? null : ibeaconName.trim();
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public String getSignAccuracy() {
        return signAccuracy;
    }

    public void setSignAccuracy(String signAccuracy) {
        this.signAccuracy = signAccuracy == null ? null : signAccuracy.trim();
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }
}