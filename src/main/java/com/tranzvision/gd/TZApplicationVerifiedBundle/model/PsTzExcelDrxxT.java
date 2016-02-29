package com.tranzvision.gd.TZApplicationVerifiedBundle.model;

import java.util.Date;

public class PsTzExcelDrxxT {
    private Integer processinstance;

    private String tzComId;

    private String tzPageId;

    private String tzDrLxbh;

    private String tzDrTaskDesc;

    private Date tzStartDtt;

    private Date tzEndDtt;

    private Integer tzDrTotalNum;

    private Integer tzDrSuccessNum;

    private Integer tzDrFaildNum;

    private String oprid;

    private String tzIsViewAtt;

    public Integer getProcessinstance() {
        return processinstance;
    }

    public void setProcessinstance(Integer processinstance) {
        this.processinstance = processinstance;
    }

    public String getTzComId() {
        return tzComId;
    }

    public void setTzComId(String tzComId) {
        this.tzComId = tzComId == null ? null : tzComId.trim();
    }

    public String getTzPageId() {
        return tzPageId;
    }

    public void setTzPageId(String tzPageId) {
        this.tzPageId = tzPageId == null ? null : tzPageId.trim();
    }

    public String getTzDrLxbh() {
        return tzDrLxbh;
    }

    public void setTzDrLxbh(String tzDrLxbh) {
        this.tzDrLxbh = tzDrLxbh == null ? null : tzDrLxbh.trim();
    }

    public String getTzDrTaskDesc() {
        return tzDrTaskDesc;
    }

    public void setTzDrTaskDesc(String tzDrTaskDesc) {
        this.tzDrTaskDesc = tzDrTaskDesc == null ? null : tzDrTaskDesc.trim();
    }

    public Date getTzStartDtt() {
        return tzStartDtt;
    }

    public void setTzStartDtt(Date tzStartDtt) {
        this.tzStartDtt = tzStartDtt;
    }

    public Date getTzEndDtt() {
        return tzEndDtt;
    }

    public void setTzEndDtt(Date tzEndDtt) {
        this.tzEndDtt = tzEndDtt;
    }

    public Integer getTzDrTotalNum() {
        return tzDrTotalNum;
    }

    public void setTzDrTotalNum(Integer tzDrTotalNum) {
        this.tzDrTotalNum = tzDrTotalNum;
    }

    public Integer getTzDrSuccessNum() {
        return tzDrSuccessNum;
    }

    public void setTzDrSuccessNum(Integer tzDrSuccessNum) {
        this.tzDrSuccessNum = tzDrSuccessNum;
    }

    public Integer getTzDrFaildNum() {
        return tzDrFaildNum;
    }

    public void setTzDrFaildNum(Integer tzDrFaildNum) {
        this.tzDrFaildNum = tzDrFaildNum;
    }

    public String getOprid() {
        return oprid;
    }

    public void setOprid(String oprid) {
        this.oprid = oprid == null ? null : oprid.trim();
    }

    public String getTzIsViewAtt() {
        return tzIsViewAtt;
    }

    public void setTzIsViewAtt(String tzIsViewAtt) {
        this.tzIsViewAtt = tzIsViewAtt == null ? null : tzIsViewAtt.trim();
    }
}