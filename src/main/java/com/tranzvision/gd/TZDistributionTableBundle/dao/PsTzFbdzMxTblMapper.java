package com.tranzvision.gd.TZDistributionTableBundle.dao;

import com.tranzvision.gd.TZDistributionTableBundle.model.PsTzFbdzMxTbl;
import com.tranzvision.gd.TZDistributionTableBundle.model.PsTzFbdzMxTblKey;

public interface PsTzFbdzMxTblMapper {
    int deleteByPrimaryKey(PsTzFbdzMxTblKey key);

    int insert(PsTzFbdzMxTbl record);

    int insertSelective(PsTzFbdzMxTbl record);

    PsTzFbdzMxTbl selectByPrimaryKey(PsTzFbdzMxTblKey key);

    int updateByPrimaryKeySelective(PsTzFbdzMxTbl record);

    int updateByPrimaryKey(PsTzFbdzMxTbl record);
}