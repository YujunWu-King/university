package com.tranzvision.gd.TZAccountMgBundle.model;

public class Psroleuser extends PsroleuserKey {
    private String dynamicSw;

    public String getDynamicSw() {
        return dynamicSw;
    }

    public void setDynamicSw(String dynamicSw) {
        this.dynamicSw = dynamicSw == null ? null : dynamicSw.trim();
    }
}