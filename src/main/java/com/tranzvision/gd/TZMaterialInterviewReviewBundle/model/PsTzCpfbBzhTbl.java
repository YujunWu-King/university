package com.tranzvision.gd.TZMaterialInterviewReviewBundle.model;

import java.math.BigDecimal;

public class PsTzCpfbBzhTbl extends PsTzCpfbBzhTblKey {
    private BigDecimal tzBzfbBl;

    private BigDecimal tzBzfbNum;

    private BigDecimal tzYxwcNum;

    private String tzFbbzZt;

    public BigDecimal getTzBzfbBl() {
        return tzBzfbBl;
    }

    public void setTzBzfbBl(BigDecimal tzBzfbBl) {
        this.tzBzfbBl = tzBzfbBl;
    }

    public BigDecimal getTzBzfbNum() {
        return tzBzfbNum;
    }

    public void setTzBzfbNum(BigDecimal tzBzfbNum) {
        this.tzBzfbNum = tzBzfbNum;
    }

    public BigDecimal getTzYxwcNum() {
        return tzYxwcNum;
    }

    public void setTzYxwcNum(BigDecimal tzYxwcNum) {
        this.tzYxwcNum = tzYxwcNum;
    }

    public String getTzFbbzZt() {
        return tzFbbzZt;
    }

    public void setTzFbbzZt(String tzFbbzZt) {
        this.tzFbbzZt = tzFbbzZt == null ? null : tzFbbzZt.trim();
    }
}