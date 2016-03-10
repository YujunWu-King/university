package com.tranzvision.gd.TZEmailSmsSendBundle.dao;

import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzDxfslshiTbl;

public interface PsTzDxfslshiTblMapper {
    int deleteByPrimaryKey(String tzRwslId);

    int insert(PsTzDxfslshiTbl record);

    int insertSelective(PsTzDxfslshiTbl record);

    PsTzDxfslshiTbl selectByPrimaryKey(String tzRwslId);

    int updateByPrimaryKeySelective(PsTzDxfslshiTbl record);

    int updateByPrimaryKey(PsTzDxfslshiTbl record);
}