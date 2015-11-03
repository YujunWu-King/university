package com.tranzvision.gd.TZResourceCollectionMgBundle.dao;

import com.tranzvision.gd.TZResourceCollectionMgBundle.model.PsTzPtZyjhTbl;

public interface PsTzPtZyjhTblMapper {
    int deleteByPrimaryKey(String tzZyjhId);

    int insert(PsTzPtZyjhTbl record);

    int insertSelective(PsTzPtZyjhTbl record);

    PsTzPtZyjhTbl selectByPrimaryKey(String tzZyjhId);

    int updateByPrimaryKeySelective(PsTzPtZyjhTbl record);

    int updateByPrimaryKey(PsTzPtZyjhTbl record);
}