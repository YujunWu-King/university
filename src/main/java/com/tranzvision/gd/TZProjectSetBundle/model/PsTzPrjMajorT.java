package com.tranzvision.gd.TZProjectSetBundle.model;

public class PsTzPrjMajorT extends PsTzPrjMajorTKey {
    private String tzMajorName;

    private Integer tzSortNum;

    public String getTzMajorName() {
        return tzMajorName;
    }

    public void setTzMajorName(String tzMajorName) {
        this.tzMajorName = tzMajorName == null ? null : tzMajorName.trim();
    }

    public Integer getTzSortNum() {
        return tzSortNum;
    }

    public void setTzSortNum(Integer tzSortNum) {
        this.tzSortNum = tzSortNum;
    }
}