package com.tranzvision.gd.TZAdditionalInformationBundle.model;

public class PsTzSbmInfRepT extends PsTzSbmInfRepTKey {
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