package com.tranzvision.gd.TZEmailSmsQFBundle.dao;

import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqftdTbl;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqftdTblKey;

public interface PsTzYjqftdTblMapper {
    int deleteByPrimaryKey(PsTzYjqftdTblKey key);

    int insert(PsTzYjqftdTbl record);

    int insertSelective(PsTzYjqftdTbl record);

    PsTzYjqftdTbl selectByPrimaryKey(PsTzYjqftdTblKey key);

    int updateByPrimaryKeySelective(PsTzYjqftdTbl record);

    int updateByPrimaryKey(PsTzYjqftdTbl record);
}