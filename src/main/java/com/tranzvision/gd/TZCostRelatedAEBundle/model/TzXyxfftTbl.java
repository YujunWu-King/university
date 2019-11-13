package com.tranzvision.gd.TZCostRelatedAEBundle.model;

import java.math.BigDecimal;
import java.util.Date;

public class TzXyxfftTbl extends TzXyxfftTblKey {
    private BigDecimal tzTxJine;

    private BigDecimal tzJxjJine;

    private BigDecimal tzXfJine;

    private String tzOpeOprid;

    private Date tzOpeDttm;

    public BigDecimal getTzTxJine() {
        return tzTxJine;
    }

    public void setTzTxJine(BigDecimal tzTxJine) {
        this.tzTxJine = tzTxJine;
    }

    public BigDecimal getTzJxjJine() {
        return tzJxjJine;
    }

    public void setTzJxjJine(BigDecimal tzJxjJine) {
        this.tzJxjJine = tzJxjJine;
    }

    public BigDecimal getTzXfJine() {
        return tzXfJine;
    }

    public void setTzXfJine(BigDecimal tzXfJine) {
        this.tzXfJine = tzXfJine;
    }

    public String getTzOpeOprid() {
        return tzOpeOprid;
    }

    public void setTzOpeOprid(String tzOpeOprid) {
        this.tzOpeOprid = tzOpeOprid == null ? null : tzOpeOprid.trim();
    }

    public Date getTzOpeDttm() {
        return tzOpeDttm;
    }

    public void setTzOpeDttm(Date tzOpeDttm) {
        this.tzOpeDttm = tzOpeDttm;
    }
}