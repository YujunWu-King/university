package com.tranzvision.gd.TZMbaPwClpsBundle.model;

import java.math.BigDecimal;
import java.util.Date;

public class PsTzClpsKshTbl extends PsTzClpsKshTblKey {
    private String tzMshiZgflg;

    private BigDecimal tzClpsPwjPc;

    private Date rowAddedDttm;

    private String rowAddedOprid;

    private Date rowLastmantDttm;

    private String rowLastmantOprid;

    private Integer syncid;

    private Date syncdttm;

    public String getTzMshiZgflg() {
        return tzMshiZgflg;
    }

    public void setTzMshiZgflg(String tzMshiZgflg) {
        this.tzMshiZgflg = tzMshiZgflg == null ? null : tzMshiZgflg.trim();
    }

    public BigDecimal getTzClpsPwjPc() {
        return tzClpsPwjPc;
    }

    public void setTzClpsPwjPc(BigDecimal tzClpsPwjPc) {
        this.tzClpsPwjPc = tzClpsPwjPc;
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
}