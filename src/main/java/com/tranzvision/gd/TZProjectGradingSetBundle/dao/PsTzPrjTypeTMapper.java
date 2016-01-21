package com.tranzvision.gd.TZProjectGradingSetBundle.dao;

import com.tranzvision.gd.TZProjectGradingSetBundle.model.PsTzPrjTypeT;

public interface PsTzPrjTypeTMapper {
    int deleteByPrimaryKey(String tzPrjTypeId);

    int insert(PsTzPrjTypeT record);

    int insertSelective(PsTzPrjTypeT record);

    PsTzPrjTypeT selectByPrimaryKey(String tzPrjTypeId);

    int updateByPrimaryKeySelective(PsTzPrjTypeT record);

    int updateByPrimaryKey(PsTzPrjTypeT record);
}