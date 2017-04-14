package com.tranzvision.gd.TZMbaPwClpsBundle.dao;

import com.tranzvision.gd.TZMbaPwClpsBundle.model.PsTzPwExtTbl;

public interface PsTzPwExtTblMapper {
    int deleteByPrimaryKey(String oprid);

    int insert(PsTzPwExtTbl record);

    int insertSelective(PsTzPwExtTbl record);

    PsTzPwExtTbl selectByPrimaryKey(String oprid);

    int updateByPrimaryKeySelective(PsTzPwExtTbl record);

    int updateByPrimaryKey(PsTzPwExtTbl record);
}