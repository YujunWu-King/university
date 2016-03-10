package com.tranzvision.gd.TZEmailSmsSendBundle.dao;

import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzDxzwlshiTbl;

public interface PsTzDxzwlshiTblMapper {
    int deleteByPrimaryKey(String tzRwslId);

    int insert(PsTzDxzwlshiTbl record);

    int insertSelective(PsTzDxzwlshiTbl record);

    PsTzDxzwlshiTbl selectByPrimaryKey(String tzRwslId);

    int updateByPrimaryKeySelective(PsTzDxzwlshiTbl record);

    int updateByPrimaryKeyWithBLOBs(PsTzDxzwlshiTbl record);

    int updateByPrimaryKey(PsTzDxzwlshiTbl record);
}