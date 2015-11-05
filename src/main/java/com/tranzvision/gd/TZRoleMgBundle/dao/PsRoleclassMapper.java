package com.tranzvision.gd.TZRoleMgBundle.dao;

import com.tranzvision.gd.TZRoleMgBundle.model.PsRoleclassKey;

public interface PsRoleclassMapper {
    int deleteByPrimaryKey(PsRoleclassKey key);

    int insert(PsRoleclassKey record);

    int insertSelective(PsRoleclassKey record);
}