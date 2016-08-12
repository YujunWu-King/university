package com.tranzvision.gd.TZEmailSmsSendBundle.dao;

import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzDxyjfsrwTbl;

public interface PsTzDxyjfsrwTblMapper {
    int deleteByPrimaryKey(String tzEmlSmsTaskId);

    int insert(PsTzDxyjfsrwTbl record);

    int insertSelective(PsTzDxyjfsrwTbl record);

    PsTzDxyjfsrwTbl selectByPrimaryKey(String tzEmlSmsTaskId);

    int updateByPrimaryKeySelective(PsTzDxyjfsrwTbl record);

    int updateByPrimaryKeyWithBLOBs(PsTzDxyjfsrwTbl record);

    int updateByPrimaryKey(PsTzDxyjfsrwTbl record);
}