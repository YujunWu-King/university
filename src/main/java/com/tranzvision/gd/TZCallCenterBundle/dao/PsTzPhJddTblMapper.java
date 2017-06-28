package com.tranzvision.gd.TZCallCenterBundle.dao;

import com.tranzvision.gd.TZCallCenterBundle.model.PsTzPhJddTbl;

public interface PsTzPhJddTblMapper {
    int deleteByPrimaryKey(String tzXh);

    int insert(PsTzPhJddTbl record);

    int insertSelective(PsTzPhJddTbl record);

    PsTzPhJddTbl selectByPrimaryKey(String tzXh);

    int updateByPrimaryKeySelective(PsTzPhJddTbl record);

    int updateByPrimaryKey(PsTzPhJddTbl record);
}