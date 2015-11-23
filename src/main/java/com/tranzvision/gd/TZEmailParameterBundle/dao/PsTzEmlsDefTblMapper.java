package com.tranzvision.gd.TZEmailParameterBundle.dao;

import com.tranzvision.gd.TZEmailParameterBundle.model.PsTzEmlsDefTbl;

public interface PsTzEmlsDefTblMapper {
    int deleteByPrimaryKey(String tzEmlservId);

    int insert(PsTzEmlsDefTbl record);

    int insertSelective(PsTzEmlsDefTbl record);

    PsTzEmlsDefTbl selectByPrimaryKey(String tzEmlservId);

    int updateByPrimaryKeySelective(PsTzEmlsDefTbl record);

    int updateByPrimaryKey(PsTzEmlsDefTbl record);
}