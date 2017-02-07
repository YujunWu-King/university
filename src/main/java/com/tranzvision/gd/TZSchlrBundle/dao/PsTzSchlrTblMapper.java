package com.tranzvision.gd.TZSchlrBundle.dao;

import com.tranzvision.gd.TZSchlrBundle.model.PsTzSchlrTbl;

public interface PsTzSchlrTblMapper {
    int deleteByPrimaryKey(String tzSchlrId);

    int insert(PsTzSchlrTbl record);

    int insertSelective(PsTzSchlrTbl record);

    PsTzSchlrTbl selectByPrimaryKey(String tzSchlrId);

    int updateByPrimaryKeySelective(PsTzSchlrTbl record);

    int updateByPrimaryKey(PsTzSchlrTbl record);
}