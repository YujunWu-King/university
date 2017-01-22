package com.tranzvision.gd.TZScoreModeManagementBundle.dao;

import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzTreeDefn;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzTreeDefnKey;

public interface PsTzTreeDefnMapper {
    int deleteByPrimaryKey(PsTzTreeDefnKey key);

    int insert(PsTzTreeDefn record);

    int insertSelective(PsTzTreeDefn record);

    PsTzTreeDefn selectByPrimaryKey(PsTzTreeDefnKey key);

    int updateByPrimaryKeySelective(PsTzTreeDefn record);

    int updateByPrimaryKey(PsTzTreeDefn record);
}