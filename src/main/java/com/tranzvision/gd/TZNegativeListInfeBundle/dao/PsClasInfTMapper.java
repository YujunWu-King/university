package com.tranzvision.gd.TZNegativeListInfeBundle.dao;

import com.tranzvision.gd.TZNegativeListInfeBundle.model.PsClasInfT;

public interface PsClasInfTMapper {
    int deleteByPrimaryKey(String tzClassId);

    int insert(PsClasInfT record);

    int insertSelective(PsClasInfT record);

    PsClasInfT selectByPrimaryKey(String tzClassId);

    int updateByPrimaryKeySelective(PsClasInfT record);

    int updateByPrimaryKeyWithBLOBs(PsClasInfT record);

    int updateByPrimaryKey(PsClasInfT record);
}