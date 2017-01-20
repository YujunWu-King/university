package com.tranzvision.gd.TZEmailSmsQFBundle.dao;

import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzDxyjqfTbl;

public interface PsTzDxyjqfTblMapper {
    int deleteByPrimaryKey(String runCntlId);

    int insert(PsTzDxyjqfTbl record);

    int insertSelective(PsTzDxyjqfTbl record);

    PsTzDxyjqfTbl selectByPrimaryKey(String runCntlId);

    int updateByPrimaryKeySelective(PsTzDxyjqfTbl record);

    int updateByPrimaryKey(PsTzDxyjqfTbl record);
}