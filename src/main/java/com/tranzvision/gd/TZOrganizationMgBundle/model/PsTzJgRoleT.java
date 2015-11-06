package com.tranzvision.gd.TZOrganizationMgBundle.model;

public class PsTzJgRoleT extends PsTzJgRoleTKey {
    private String tzRoleType;

    public String getTzRoleType() {
        return tzRoleType;
    }

    public void setTzRoleType(String tzRoleType) {
        this.tzRoleType = tzRoleType == null ? null : tzRoleType.trim();
    }
}