package com.tranzvision.gd.TZEmailTxTypeBundle.dao;

import com.tranzvision.gd.TZEmailTxTypeBundle.model.PsTzTxRuleTbl;

public interface PsTzTxRuleTblMapper {
    int deleteByPrimaryKey(String tzTxRuleId);

    int insert(PsTzTxRuleTbl record);

    int insertSelective(PsTzTxRuleTbl record);

    PsTzTxRuleTbl selectByPrimaryKey(String tzTxRuleId);

    int updateByPrimaryKeySelective(PsTzTxRuleTbl record);

    int updateByPrimaryKey(PsTzTxRuleTbl record);
}