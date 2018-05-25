package com.tranzvision.gd.TZMbaPwClpsBundle.model;

import java.math.BigDecimal;
import java.util.Date;

public class PsTzMsPskshTbl extends PsTzMsPskshTblKey {
    private BigDecimal tzMspsPwjPc;

    private String tzLuquZt;

    private String tzBeizhu;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    private Integer tzGroupId;

    private Date tzGroupDate;

    private Integer tzOrder;

    private Long tzScoreInsId;

    public BigDecimal getTzMspsPwjPc() {
        return tzMspsPwjPc;
    }

    public void setTzMspsPwjPc(BigDecimal tzMspsPwjPc) {
        this.tzMspsPwjPc = tzMspsPwjPc;
    }

    public String getTzLuquZt() {
        return tzLuquZt;
    }

    public void setTzLuquZt(String tzLuquZt) {
        this.tzLuquZt = tzLuquZt == null ? null : tzLuquZt.trim();
    }

    public String getTzBeizhu() {
        return tzBeizhu;
    }

    public void setTzBeizhu(String tzBeizhu) {
        this.tzBeizhu = tzBeizhu == null ? null : tzBeizhu.trim();
    }

    public Date getRowAddedDttm() {
        return rowAddedDttm;
    }

    public void setRowAddedDttm(Date rowAddedDttm) {
        this.rowAddedDttm = rowAddedDttm;
    }

    public String getRowAddedOprid() {
        return rowAddedOprid;
    }

    public void setRowAddedOprid(String rowAddedOprid) {
        this.rowAddedOprid = rowAddedOprid == null ? null : rowAddedOprid.trim();
    }

    public Date getRowLastmantDttm() {
        return rowLastmantDttm;
    }

    public void setRowLastmantDttm(Date rowLastmantDttm) {
        this.rowLastmantDttm = rowLastmantDttm;
    }

    public String getRowLastmantOprid() {
        return rowLastmantOprid;
    }

    public void setRowLastmantOprid(String rowLastmantOprid) {
        this.rowLastmantOprid = rowLastmantOprid == null ? null : rowLastmantOprid.trim();
    }

    public Integer getSyncid() {
        return syncid;
    }

    public void setSyncid(Integer syncid) {
        this.syncid = syncid;
    }

    public Date getSyncdttm() {
        return syncdttm;
    }

    public void setSyncdttm(Date syncdttm) {
        this.syncdttm = syncdttm;
    }

    public Integer getTzGroupId() {
        return tzGroupId;
    }

    public void setTzGroupId(Integer tzGroupId) {
        this.tzGroupId = tzGroupId;
    }

    public Date getTzGroupDate() {
        return tzGroupDate;
    }

    public void setTzGroupDate(Date tzGroupDate) {
        this.tzGroupDate = tzGroupDate;
    }

    public Integer getTzOrder() {
        return tzOrder;
    }

    public void setTzOrder(Integer tzOrder) {
        this.tzOrder = tzOrder;
    }

    public Long getTzScoreInsId() {
        return tzScoreInsId;
    }

    public void setTzScoreInsId(Long tzScoreInsId) {
        this.tzScoreInsId = tzScoreInsId;
    }
}