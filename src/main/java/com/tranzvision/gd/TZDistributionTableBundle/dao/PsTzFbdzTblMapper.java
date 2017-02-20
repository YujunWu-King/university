package com.tranzvision.gd.TZDistributionTableBundle.dao;

import com.tranzvision.gd.TZDistributionTableBundle.model.PsTzFbdzTbl;

public interface PsTzFbdzTblMapper {
    int deleteByPrimaryKey(String tzMFbdzId);

    int insert(PsTzFbdzTbl record);

    int insertSelective(PsTzFbdzTbl record);

    PsTzFbdzTbl selectByPrimaryKey(String tzMFbdzId);

    int updateByPrimaryKeySelective(PsTzFbdzTbl record);

    int updateByPrimaryKey(PsTzFbdzTbl record);
}