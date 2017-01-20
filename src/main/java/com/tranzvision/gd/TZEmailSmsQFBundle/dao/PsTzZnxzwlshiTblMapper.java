package com.tranzvision.gd.TZEmailSmsQFBundle.dao;

import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxzwlshiTbl;

public interface PsTzZnxzwlshiTblMapper {
    int deleteByPrimaryKey(String tzRwslId);

    int insert(PsTzZnxzwlshiTbl record);

    int insertSelective(PsTzZnxzwlshiTbl record);

    PsTzZnxzwlshiTbl selectByPrimaryKey(String tzRwslId);

    int updateByPrimaryKeySelective(PsTzZnxzwlshiTbl record);

    int updateByPrimaryKeyWithBLOBs(PsTzZnxzwlshiTbl record);

    int updateByPrimaryKey(PsTzZnxzwlshiTbl record);
}