package com.tranzvision.gd.TZThemeMgBundle.dao;

import com.tranzvision.gd.TZThemeMgBundle.model.PsTzPtZtxxTbl;

public interface PsTzPtZtxxTblMapper {
    int deleteByPrimaryKey(String tzZtId);

    int insert(PsTzPtZtxxTbl record);

    int insertSelective(PsTzPtZtxxTbl record);

    PsTzPtZtxxTbl selectByPrimaryKey(String tzZtId);

    int updateByPrimaryKeySelective(PsTzPtZtxxTbl record);

    int updateByPrimaryKey(PsTzPtZtxxTbl record);
}