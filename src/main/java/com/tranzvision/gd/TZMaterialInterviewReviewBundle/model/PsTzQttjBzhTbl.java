package com.tranzvision.gd.TZMaterialInterviewReviewBundle.model;

import java.math.BigDecimal;

public class PsTzQttjBzhTbl extends PsTzQttjBzhTblKey {
    private String tzTjbzZt;

    private BigDecimal tzTjlBzh;

    private BigDecimal tzTjlWcz;

    public String getTzTjbzZt() {
        return tzTjbzZt;
    }

    public void setTzTjbzZt(String tzTjbzZt) {
        this.tzTjbzZt = tzTjbzZt == null ? null : tzTjbzZt.trim();
    }

    public BigDecimal getTzTjlBzh() {
        return tzTjlBzh;
    }

    public void setTzTjlBzh(BigDecimal tzTjlBzh) {
        this.tzTjlBzh = tzTjlBzh;
    }

    public BigDecimal getTzTjlWcz() {
        return tzTjlWcz;
    }

    public void setTzTjlWcz(BigDecimal tzTjlWcz) {
        this.tzTjlWcz = tzTjlWcz;
    }
}