package com.tranzvision.gd.TZPermissionDefnBundle.dao;

import com.tranzvision.gd.TZPermissionDefnBundle.model.PsClassDefn;

public interface PsClassDefnMapper {
    int deleteByPrimaryKey(String classid);

    int insert(PsClassDefn record);

    int insertSelective(PsClassDefn record);

    PsClassDefn selectByPrimaryKey(String classid);

    int updateByPrimaryKeySelective(PsClassDefn record);

    int updateByPrimaryKey(PsClassDefn record);
}