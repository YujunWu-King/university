package com.tranzvision.gd.TZCostRelatedAEBundle.dao;

import com.tranzvision.gd.TZCostRelatedAEBundle.model.TzDzjlTbl;

public interface TzDzjlTblMapper {
    int deleteByPrimaryKey(String tzDzId);

    int insert(TzDzjlTbl record);

    int insertSelective(TzDzjlTbl record);

    TzDzjlTbl selectByPrimaryKey(String tzDzId);

    int updateByPrimaryKeySelective(TzDzjlTbl record);

    int updateByPrimaryKey(TzDzjlTbl record);
}