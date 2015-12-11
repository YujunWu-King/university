package com.tranzvision.gd.TZEmailSmsSendBundle.dao;

import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzYjzwlshiTbl;

public interface PsTzYjzwlshiTblMapper {
    int deleteByPrimaryKey(String tzRwslId);

    int insert(PsTzYjzwlshiTbl record);

    int insertSelective(PsTzYjzwlshiTbl record);

    PsTzYjzwlshiTbl selectByPrimaryKey(String tzRwslId);

    int updateByPrimaryKeySelective(PsTzYjzwlshiTbl record);

    int updateByPrimaryKeyWithBLOBs(PsTzYjzwlshiTbl record);

    int updateByPrimaryKey(PsTzYjzwlshiTbl record);
}