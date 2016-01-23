package com.tranzvision.gd.TZProjectSetBundle.dao;

import com.tranzvision.gd.TZProjectSetBundle.model.PsTzPrjInfT;

public interface PsTzPrjInfTMapper {
    int deleteByPrimaryKey(String tzPrjId);

    int insert(PsTzPrjInfT record);

    int insertSelective(PsTzPrjInfT record);

    PsTzPrjInfT selectByPrimaryKey(String tzPrjId);

    int updateByPrimaryKeySelective(PsTzPrjInfT record);

    int updateByPrimaryKeyWithBLOBs(PsTzPrjInfT record);

    int updateByPrimaryKey(PsTzPrjInfT record);
}