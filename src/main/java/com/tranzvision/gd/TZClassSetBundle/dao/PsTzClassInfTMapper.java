package com.tranzvision.gd.TZClassSetBundle.dao;

import com.tranzvision.gd.TZClassSetBundle.model.PsTzClassInfT;

public interface PsTzClassInfTMapper {
    int deleteByPrimaryKey(String tzClassId);

    int insert(PsTzClassInfT record);

    int insertSelective(PsTzClassInfT record);

    PsTzClassInfT selectByPrimaryKey(String tzClassId);

    int updateByPrimaryKeySelective(PsTzClassInfT record);

    int updateByPrimaryKeyWithBLOBs(PsTzClassInfT record);

    int updateByPrimaryKey(PsTzClassInfT record);
}