package com.tranzvision.gd.TZTemplateBundle.dao;

import com.tranzvision.gd.TZTemplateBundle.model.PsTzSmsservTbl;

public interface PsTzSmsservTblMapper {
    int deleteByPrimaryKey(String tzSmsServId);

    int insert(PsTzSmsservTbl record);

    int insertSelective(PsTzSmsservTbl record);

    PsTzSmsservTbl selectByPrimaryKey(String tzSmsServId);

    int updateByPrimaryKeySelective(PsTzSmsservTbl record);

    int updateByPrimaryKey(PsTzSmsservTbl record);
}