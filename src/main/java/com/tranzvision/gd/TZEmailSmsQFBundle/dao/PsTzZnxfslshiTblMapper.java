package com.tranzvision.gd.TZEmailSmsQFBundle.dao;

import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzZnxfslshiTbl;

public interface PsTzZnxfslshiTblMapper {
    int deleteByPrimaryKey(String tzRwslId);

    int insert(PsTzZnxfslshiTbl record);

    int insertSelective(PsTzZnxfslshiTbl record);

    PsTzZnxfslshiTbl selectByPrimaryKey(String tzRwslId);

    int updateByPrimaryKeySelective(PsTzZnxfslshiTbl record);

    int updateByPrimaryKey(PsTzZnxfslshiTbl record);
}