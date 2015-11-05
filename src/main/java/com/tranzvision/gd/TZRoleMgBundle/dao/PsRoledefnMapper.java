package com.tranzvision.gd.TZRoleMgBundle.dao;

import com.tranzvision.gd.TZRoleMgBundle.model.PsRoledefn;

public interface PsRoledefnMapper {
    int deleteByPrimaryKey(String rolename);

    int insert(PsRoledefn record);

    int insertSelective(PsRoledefn record);

    PsRoledefn selectByPrimaryKey(String rolename);

    int updateByPrimaryKeySelective(PsRoledefn record);

    int updateByPrimaryKeyWithBLOBs(PsRoledefn record);

    int updateByPrimaryKey(PsRoledefn record);
}