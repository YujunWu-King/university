package com.tranzvision.gd.TZEmailSmsSendBundle.dao;

import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzYjfslshiTbl;

public interface PsTzYjfslshiTblMapper {
    int deleteByPrimaryKey(String tzRwslId);

    int insert(PsTzYjfslshiTbl record);

    int insertSelective(PsTzYjfslshiTbl record);

    PsTzYjfslshiTbl selectByPrimaryKey(String tzRwslId);

    int updateByPrimaryKeySelective(PsTzYjfslshiTbl record);

    int updateByPrimaryKey(PsTzYjfslshiTbl record);
}