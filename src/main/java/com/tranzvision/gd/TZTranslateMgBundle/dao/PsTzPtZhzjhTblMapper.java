package com.tranzvision.gd.TZTranslateMgBundle.dao;

import com.tranzvision.gd.TZTranslateMgBundle.model.PsTzPtZhzjhTbl;

public interface PsTzPtZhzjhTblMapper {
    int deleteByPrimaryKey(String tzZhzjhId);

    int insert(PsTzPtZhzjhTbl record);

    int insertSelective(PsTzPtZhzjhTbl record);

    PsTzPtZhzjhTbl selectByPrimaryKey(String tzZhzjhId);

    int updateByPrimaryKeySelective(PsTzPtZhzjhTbl record);

    int updateByPrimaryKey(PsTzPtZhzjhTbl record);
}