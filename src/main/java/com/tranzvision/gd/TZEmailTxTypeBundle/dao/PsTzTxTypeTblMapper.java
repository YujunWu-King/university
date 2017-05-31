package com.tranzvision.gd.TZEmailTxTypeBundle.dao;

import com.tranzvision.gd.TZEmailTxTypeBundle.model.PsTzTxTypeTbl;

public interface PsTzTxTypeTblMapper {
    int deleteByPrimaryKey(String tzTxTypeId);

    int insert(PsTzTxTypeTbl record);

    int insertSelective(PsTzTxTypeTbl record);

    PsTzTxTypeTbl selectByPrimaryKey(String tzTxTypeId);

    int updateByPrimaryKeySelective(PsTzTxTypeTbl record);

    int updateByPrimaryKey(PsTzTxTypeTbl record);
}