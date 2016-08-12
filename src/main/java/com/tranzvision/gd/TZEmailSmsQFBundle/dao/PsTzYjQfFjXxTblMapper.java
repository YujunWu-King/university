package com.tranzvision.gd.TZEmailSmsQFBundle.dao;

import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjQfFjXxTbl;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjQfFjXxTblKey;

public interface PsTzYjQfFjXxTblMapper {
    int deleteByPrimaryKey(PsTzYjQfFjXxTblKey key);

    int insert(PsTzYjQfFjXxTbl record);

    int insertSelective(PsTzYjQfFjXxTbl record);

    PsTzYjQfFjXxTbl selectByPrimaryKey(PsTzYjQfFjXxTblKey key);

    int updateByPrimaryKeySelective(PsTzYjQfFjXxTbl record);

    int updateByPrimaryKey(PsTzYjQfFjXxTbl record);
}