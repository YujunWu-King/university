package com.tranzvision.gd.TZEmailSmsQFBundle.model;

import java.util.Date;

public class PsTzYjqftxjlT extends PsTzYjqftxjlTKey {
    private String tzTxType;

    private Date tzTxDttm;

    private String tzTxDesc;

    private String tzMalContent;

    public String getTzTxType() {
        return tzTxType;
    }

    public void setTzTxType(String tzTxType) {
        this.tzTxType = tzTxType == null ? null : tzTxType.trim();
    }

    public Date getTzTxDttm() {
        return tzTxDttm;
    }

    public void setTzTxDttm(Date tzTxDttm) {
        this.tzTxDttm = tzTxDttm;
    }

    public String getTzTxDesc() {
        return tzTxDesc;
    }

    public void setTzTxDesc(String tzTxDesc) {
        this.tzTxDesc = tzTxDesc == null ? null : tzTxDesc.trim();
    }

    public String getTzMalContent() {
        return tzMalContent;
    }

    public void setTzMalContent(String tzMalContent) {
        this.tzMalContent = tzMalContent == null ? null : tzMalContent.trim();
    }
}