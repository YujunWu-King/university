package com.tranzvision.gd.TZApplicationSurveyBundle.model;

import java.math.BigDecimal;

public class PsTzDcXxxKxzT extends PsTzDcXxxKxzTKey {
    private Integer tzOrder;

    private String tzXxxkxzMs;

    private String tzKxzMrzBz;

    private String tzKxzQtBz;

    private BigDecimal tzXxxkxzQz;

    public Integer getTzOrder() {
        return tzOrder;
    }

    public void setTzOrder(Integer tzOrder) {
        this.tzOrder = tzOrder;
    }

    public String getTzXxxkxzMs() {
        return tzXxxkxzMs;
    }

    public void setTzXxxkxzMs(String tzXxxkxzMs) {
        this.tzXxxkxzMs = tzXxxkxzMs == null ? null : tzXxxkxzMs.trim();
    }

    public String getTzKxzMrzBz() {
        return tzKxzMrzBz;
    }

    public void setTzKxzMrzBz(String tzKxzMrzBz) {
        this.tzKxzMrzBz = tzKxzMrzBz == null ? null : tzKxzMrzBz.trim();
    }

    public String getTzKxzQtBz() {
        return tzKxzQtBz;
    }

    public void setTzKxzQtBz(String tzKxzQtBz) {
        this.tzKxzQtBz = tzKxzQtBz == null ? null : tzKxzQtBz.trim();
    }

    public BigDecimal getTzXxxkxzQz() {
        return tzXxxkxzQz;
    }

    public void setTzXxxkxzQz(BigDecimal tzXxxkxzQz) {
        this.tzXxxkxzQz = tzXxxkxzQz;
    }
}