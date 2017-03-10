package com.tranzvision.gd.TZWeChatBundle.model;

import java.util.Date;

public class PsTzWxGzhcsT extends PsTzWxGzhcsTKey {
    private String tzAccessToken;

    private Date tzUpdateDttm;

    private Integer tzTokenYxq;

    private String tzJsapiTicket;

    private Date tzUpdateDttm2;

    private Integer tzTicketYxq;

    public String getTzAccessToken() {
        return tzAccessToken;
    }

    public void setTzAccessToken(String tzAccessToken) {
        this.tzAccessToken = tzAccessToken == null ? null : tzAccessToken.trim();
    }

    public Date getTzUpdateDttm() {
        return tzUpdateDttm;
    }

    public void setTzUpdateDttm(Date tzUpdateDttm) {
        this.tzUpdateDttm = tzUpdateDttm;
    }

    public Integer getTzTokenYxq() {
        return tzTokenYxq;
    }

    public void setTzTokenYxq(Integer tzTokenYxq) {
        this.tzTokenYxq = tzTokenYxq;
    }

    public String getTzJsapiTicket() {
        return tzJsapiTicket;
    }

    public void setTzJsapiTicket(String tzJsapiTicket) {
        this.tzJsapiTicket = tzJsapiTicket == null ? null : tzJsapiTicket.trim();
    }

    public Date getTzUpdateDttm2() {
        return tzUpdateDttm2;
    }

    public void setTzUpdateDttm2(Date tzUpdateDttm2) {
        this.tzUpdateDttm2 = tzUpdateDttm2;
    }

    public Integer getTzTicketYxq() {
        return tzTicketYxq;
    }

    public void setTzTicketYxq(Integer tzTicketYxq) {
        this.tzTicketYxq = tzTicketYxq;
    }
}