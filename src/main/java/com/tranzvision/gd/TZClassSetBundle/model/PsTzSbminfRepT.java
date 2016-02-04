package com.tranzvision.gd.TZClassSetBundle.model;

public class PsTzSbminfRepT extends PsTzSbminfRepTKey {
    private String tzSbminfRep;

    private Integer tzSortNum;

    public String getTzSbminfRep() {
        return tzSbminfRep;
    }

    public void setTzSbminfRep(String tzSbminfRep) {
        this.tzSbminfRep = tzSbminfRep == null ? null : tzSbminfRep.trim();
    }

    public Integer getTzSortNum() {
        return tzSortNum;
    }

    public void setTzSortNum(Integer tzSortNum) {
        this.tzSortNum = tzSortNum;
    }
}