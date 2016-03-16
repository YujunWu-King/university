package com.tranzvision.gd.TZSmsTemplateBundle.dao;

import com.tranzvision.gd.TZSmsTemplateBundle.model.PsTzSmsServTbl;

public interface PsTzSmsServTblMapper {
    int deleteByPrimaryKey(String tzSmsServId);

    int insert(PsTzSmsServTbl record);

    int insertSelective(PsTzSmsServTbl record);

    PsTzSmsServTbl selectByPrimaryKey(String tzSmsServId);

    int updateByPrimaryKeySelective(PsTzSmsServTbl record);

    int updateByPrimaryKey(PsTzSmsServTbl record);
}