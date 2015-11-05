package com.tranzvision.gd.TZComRegMgBundle.dao;

import com.tranzvision.gd.TZComRegMgBundle.model.PsTzAqComzcTbl;

public interface PsTzAqComzcTblMapper {
    int deleteByPrimaryKey(String tzComId);

    int insert(PsTzAqComzcTbl record);

    int insertSelective(PsTzAqComzcTbl record);

    PsTzAqComzcTbl selectByPrimaryKey(String tzComId);

    int updateByPrimaryKeySelective(PsTzAqComzcTbl record);

    int updateByPrimaryKey(PsTzAqComzcTbl record);
}