package com.tranzvision.gd.TZAdditionalInformationBundle.model;

public class PsTzSbmInfStpT extends PsTzSbmInfStpTKey {
    private Integer tzSortNum;

    private String tzContIntro;

    private String tzRemark;

    public Integer getTzSortNum() {
        return tzSortNum;
    }

    public void setTzSortNum(Integer tzSortNum) {
        this.tzSortNum = tzSortNum;
    }

    public String getTzContIntro() {
        return tzContIntro;
    }

    public void setTzContIntro(String tzContIntro) {
        this.tzContIntro = tzContIntro == null ? null : tzContIntro.trim();
    }

    public String getTzRemark() {
        return tzRemark;
    }

    public void setTzRemark(String tzRemark) {
        this.tzRemark = tzRemark == null ? null : tzRemark.trim();
    }
}