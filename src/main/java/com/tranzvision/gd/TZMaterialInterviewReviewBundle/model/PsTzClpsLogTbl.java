package com.tranzvision.gd.TZMaterialInterviewReviewBundle.model;

import java.util.Date;

public class PsTzClpsLogTbl {
    private String tzRzlsNum;

    private String tzClassId;

    private String tzBatchId;

    private Short tzClpsLunc;

    private String tzOprType;

    private String tzOprid;

    private Date tzOprDttm;

    public String getTzRzlsNum() {
        return tzRzlsNum;
    }

    public void setTzRzlsNum(String tzRzlsNum) {
        this.tzRzlsNum = tzRzlsNum == null ? null : tzRzlsNum.trim();
    }

    public String getTzClassId() {
        return tzClassId;
    }

    public void setTzClassId(String tzClassId) {
        this.tzClassId = tzClassId == null ? null : tzClassId.trim();
    }

    public String getTzBatchId() {
        return tzBatchId;
    }

    public void setTzBatchId(String tzBatchId) {
        this.tzBatchId = tzBatchId == null ? null : tzBatchId.trim();
    }

    public Short getTzClpsLunc() {
        return tzClpsLunc;
    }

    public void setTzClpsLunc(Short tzClpsLunc) {
        this.tzClpsLunc = tzClpsLunc;
    }

    public String getTzOprType() {
        return tzOprType;
    }

    public void setTzOprType(String tzOprType) {
        this.tzOprType = tzOprType == null ? null : tzOprType.trim();
    }

    public String getTzOprid() {
        return tzOprid;
    }

    public void setTzOprid(String tzOprid) {
        this.tzOprid = tzOprid == null ? null : tzOprid.trim();
    }

    public Date getTzOprDttm() {
        return tzOprDttm;
    }

    public void setTzOprDttm(Date tzOprDttm) {
        this.tzOprDttm = tzOprDttm;
    }
}