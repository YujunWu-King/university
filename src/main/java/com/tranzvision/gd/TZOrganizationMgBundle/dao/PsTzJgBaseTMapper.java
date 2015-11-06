package com.tranzvision.gd.TZOrganizationMgBundle.dao;

import com.tranzvision.gd.TZOrganizationMgBundle.model.PsTzJgBaseT;

public interface PsTzJgBaseTMapper {
    int deleteByPrimaryKey(String tzJgId);

    int insert(PsTzJgBaseT record);

    int insertSelective(PsTzJgBaseT record);

    PsTzJgBaseT selectByPrimaryKey(String tzJgId);

    int updateByPrimaryKeySelective(PsTzJgBaseT record);

    int updateByPrimaryKeyWithBLOBs(PsTzJgBaseT record);

    int updateByPrimaryKey(PsTzJgBaseT record);
}