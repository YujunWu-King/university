package com.tranzvision.gd.TZAccountMgBundle.model;

public class PsroleuserKey {
    private String roleuser;

    private String rolename;

    public String getRoleuser() {
        return roleuser;
    }

    public void setRoleuser(String roleuser) {
        this.roleuser = roleuser == null ? null : roleuser.trim();
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename == null ? null : rolename.trim();
    }
}