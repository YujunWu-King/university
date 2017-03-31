package com.tranzvision.gd.TZAutoScoringRulerBundle.dao;

import com.tranzvision.gd.TZAutoScoringRulerBundle.model.PsTzZdCsDfGzTbl;

public interface PsTzZdCsDfGzTblMapper {
    int deleteByPrimaryKey(String tzZdcsgzId);

    int insert(PsTzZdCsDfGzTbl record);

    int insertSelective(PsTzZdCsDfGzTbl record);

    PsTzZdCsDfGzTbl selectByPrimaryKey(String tzZdcsgzId);

    int updateByPrimaryKeySelective(PsTzZdCsDfGzTbl record);

    int updateByPrimaryKey(PsTzZdCsDfGzTbl record);
}