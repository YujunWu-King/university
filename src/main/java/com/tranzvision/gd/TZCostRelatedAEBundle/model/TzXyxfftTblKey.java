package com.tranzvision.gd.TZCostRelatedAEBundle.model;

public class TzXyxfftTblKey {
    private String tzmsProNbid;

    private Long tzAppInsId;

    private String tzTxYear;

    private String tzTxMon;

    public String getTzmsProNbid() {
        return tzmsProNbid;
    }

    public void setTzmsProNbid(String tzmsProNbid) {
        this.tzmsProNbid = tzmsProNbid == null ? null : tzmsProNbid.trim();
    }

    public Long getTzAppInsId() {
        return tzAppInsId;
    }

    public void setTzAppInsId(Long tzAppInsId) {
        this.tzAppInsId = tzAppInsId;
    }

    public String getTzTxYear() {
        return tzTxYear;
    }

    public void setTzTxYear(String tzTxYear) {
        this.tzTxYear = tzTxYear == null ? null : tzTxYear.trim();
    }

    public String getTzTxMon() {
        return tzTxMon;
    }

    public void setTzTxMon(String tzTxMon) {
        this.tzTxMon = tzTxMon == null ? null : tzTxMon.trim();
    }
}