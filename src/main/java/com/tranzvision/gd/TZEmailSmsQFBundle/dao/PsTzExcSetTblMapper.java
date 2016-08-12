package com.tranzvision.gd.TZEmailSmsQFBundle.dao;

import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzExcSetTbl;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzExcSetTblKey;

public interface PsTzExcSetTblMapper {
    int deleteByPrimaryKey(PsTzExcSetTblKey key);

    int insert(PsTzExcSetTbl record);

    int insertSelective(PsTzExcSetTbl record);

    PsTzExcSetTbl selectByPrimaryKey(PsTzExcSetTblKey key);

    int updateByPrimaryKeySelective(PsTzExcSetTbl record);

    int updateByPrimaryKey(PsTzExcSetTbl record);
}