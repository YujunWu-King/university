package com.tranzvision.gd.TZEmailTxTypeBundle.dao;

import com.tranzvision.gd.TZEmailTxTypeBundle.model.PsTzTxLbtjgxTKey;

public interface PsTzTxLbtjgxTMapper {
    int deleteByPrimaryKey(PsTzTxLbtjgxTKey key);

    int insert(PsTzTxLbtjgxTKey record);

    int insertSelective(PsTzTxLbtjgxTKey record);
}