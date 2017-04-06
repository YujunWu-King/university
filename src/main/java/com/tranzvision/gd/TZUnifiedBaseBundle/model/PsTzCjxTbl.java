package com.tranzvision.gd.TZUnifiedBaseBundle.model;

import java.math.BigDecimal;

public class PsTzCjxTbl extends PsTzCjxTblKey {
    private BigDecimal tzScoreNum;

    private String tzCjxXlkXxbh;

    private String tzScoreBz;

    public BigDecimal getTzScoreNum() {
        return tzScoreNum;
    }

    public void setTzScoreNum(BigDecimal tzScoreNum) {
        this.tzScoreNum = tzScoreNum;
    }

    public String getTzCjxXlkXxbh() {
        return tzCjxXlkXxbh;
    }

    public void setTzCjxXlkXxbh(String tzCjxXlkXxbh) {
        this.tzCjxXlkXxbh = tzCjxXlkXxbh == null ? null : tzCjxXlkXxbh.trim();
    }

    public String getTzScoreBz() {
        return tzScoreBz;
    }

    public void setTzScoreBz(String tzScoreBz) {
        this.tzScoreBz = tzScoreBz == null ? null : tzScoreBz.trim();
    }
}