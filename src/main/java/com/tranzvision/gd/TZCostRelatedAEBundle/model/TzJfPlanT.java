package com.tranzvision.gd.TZCostRelatedAEBundle.model;

import java.math.BigDecimal;
import java.util.Date;

public class TzJfPlanT {
    private String tzJfplId;

    private String tzObjId;

    private String tzObjType;

    private String tzObjPrjId;

    private String tzObjName;

    private String tzJfType;

    private Date tzJfDate;

    private BigDecimal tzJfBzJe;

    private BigDecimal tzJfTzJe;

    private BigDecimal tzJfJmJe;

    private BigDecimal tzJfBqys;

    private BigDecimal tzJfBqyt;

    private BigDecimal tzJfBqss;

    private BigDecimal tzJfBqyj;

    private String tzJfStat;

    private String rowAddedOprid;

    private Date rowAddedDttm;

    private String rowLastmantOprid;

    private Date rowLastmantDttm;

    private String tzRemarks;

    public String getTzJfplId() {
        return tzJfplId;
    }

    public void setTzJfplId(String tzJfplId) {
        this.tzJfplId = tzJfplId == null ? null : tzJfplId.trim();
    }

    public String getTzObjId() {
        return tzObjId;
    }

    public void setTzObjId(String tzObjId) {
        this.tzObjId = tzObjId == null ? null : tzObjId.trim();
    }

    public String getTzObjType() {
        return tzObjType;
    }

    public void setTzObjType(String tzObjType) {
        this.tzObjType = tzObjType == null ? null : tzObjType.trim();
    }

    public String getTzObjPrjId() {
        return tzObjPrjId;
    }

    public void setTzObjPrjId(String tzObjPrjId) {
        this.tzObjPrjId = tzObjPrjId == null ? null : tzObjPrjId.trim();
    }

    public String getTzObjName() {
        return tzObjName;
    }

    public void setTzObjName(String tzObjName) {
        this.tzObjName = tzObjName == null ? null : tzObjName.trim();
    }

    public String getTzJfType() {
        return tzJfType;
    }

    public void setTzJfType(String tzJfType) {
        this.tzJfType = tzJfType == null ? null : tzJfType.trim();
    }

    public Date getTzJfDate() {
        return tzJfDate;
    }

    public void setTzJfDate(Date tzJfDate) {
        this.tzJfDate = tzJfDate;
    }

    public BigDecimal getTzJfBzJe() {
        return tzJfBzJe;
    }

    public void setTzJfBzJe(BigDecimal tzJfBzJe) {
        this.tzJfBzJe = tzJfBzJe;
    }

    public BigDecimal getTzJfTzJe() {
        return tzJfTzJe;
    }

    public void setTzJfTzJe(BigDecimal tzJfTzJe) {
        this.tzJfTzJe = tzJfTzJe;
    }

    public BigDecimal getTzJfJmJe() {
        return tzJfJmJe;
    }

    public void setTzJfJmJe(BigDecimal tzJfJmJe) {
        this.tzJfJmJe = tzJfJmJe;
    }

    public BigDecimal getTzJfBqys() {
        return tzJfBqys;
    }

    public void setTzJfBqys(BigDecimal tzJfBqys) {
        this.tzJfBqys = tzJfBqys;
    }

    public BigDecimal getTzJfBqyt() {
        return tzJfBqyt;
    }

    public void setTzJfBqyt(BigDecimal tzJfBqyt) {
        this.tzJfBqyt = tzJfBqyt;
    }

    public BigDecimal getTzJfBqss() {
        return tzJfBqss;
    }

    public void setTzJfBqss(BigDecimal tzJfBqss) {
        this.tzJfBqss = tzJfBqss;
    }

    public BigDecimal getTzJfBqyj() {
        return tzJfBqyj;
    }

    public void setTzJfBqyj(BigDecimal tzJfBqyj) {
        this.tzJfBqyj = tzJfBqyj;
    }

    public String getTzJfStat() {
        return tzJfStat;
    }

    public void setTzJfStat(String tzJfStat) {
        this.tzJfStat = tzJfStat == null ? null : tzJfStat.trim();
    }

    public String getRowAddedOprid() {
        return rowAddedOprid;
    }

    public void setRowAddedOprid(String rowAddedOprid) {
        this.rowAddedOprid = rowAddedOprid == null ? null : rowAddedOprid.trim();
    }

    public Date getRowAddedDttm() {
        return rowAddedDttm;
    }

    public void setRowAddedDttm(Date rowAddedDttm) {
        this.rowAddedDttm = rowAddedDttm;
    }

    public String getRowLastmantOprid() {
        return rowLastmantOprid;
    }

    public void setRowLastmantOprid(String rowLastmantOprid) {
        this.rowLastmantOprid = rowLastmantOprid == null ? null : rowLastmantOprid.trim();
    }

    public Date getRowLastmantDttm() {
        return rowLastmantDttm;
    }

    public void setRowLastmantDttm(Date rowLastmantDttm) {
        this.rowLastmantDttm = rowLastmantDttm;
    }

    public String getTzRemarks() {
        return tzRemarks;
    }

    public void setTzRemarks(String tzRemarks) {
        this.tzRemarks = tzRemarks == null ? null : tzRemarks.trim();
    }
}