package com.tranzvision.gd.TZTemplateBundle.dao;

import com.tranzvision.gd.TZTemplateBundle.model.PsTzSmsServTbl;

public interface PsTzSmsServTblMapper {
    int deleteByPrimaryKey(String tzSmsServId);

    int insert(PsTzSmsServTbl record);

    int insertSelective(PsTzSmsServTbl record);

    PsTzSmsServTbl selectByPrimaryKey(String tzSmsServId);

    int updateByPrimaryKeySelective(PsTzSmsServTbl record);

    int updateByPrimaryKey(PsTzSmsServTbl record);
}