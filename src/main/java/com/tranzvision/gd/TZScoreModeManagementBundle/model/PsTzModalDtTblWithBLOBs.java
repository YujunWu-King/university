package com.tranzvision.gd.TZScoreModeManagementBundle.model;

public class PsTzModalDtTblWithBLOBs extends PsTzModalDtTbl {
    private String tzScoreItemDfsm;

    private String tzScoreItemCkwt;

    private String tzScoreItemMsff;

    public String getTzScoreItemDfsm() {
        return tzScoreItemDfsm;
    }

    public void setTzScoreItemDfsm(String tzScoreItemDfsm) {
        this.tzScoreItemDfsm = tzScoreItemDfsm == null ? null : tzScoreItemDfsm.trim();
    }

    public String getTzScoreItemCkwt() {
        return tzScoreItemCkwt;
    }

    public void setTzScoreItemCkwt(String tzScoreItemCkwt) {
        this.tzScoreItemCkwt = tzScoreItemCkwt == null ? null : tzScoreItemCkwt.trim();
    }

    public String getTzScoreItemMsff() {
        return tzScoreItemMsff;
    }

    public void setTzScoreItemMsff(String tzScoreItemMsff) {
        this.tzScoreItemMsff = tzScoreItemMsff == null ? null : tzScoreItemMsff.trim();
    }
}