package com.tranzvision.gd.TZJudgesTypeBundle.dao;

import com.tranzvision.gd.TZJudgesTypeBundle.model.PsTzMspsGrTbl;

public interface PsTzMspsGrTblMapper {
    int deleteByPrimaryKey(String tzClpsGrId);

    int insert(PsTzMspsGrTbl record);

    int insertSelective(PsTzMspsGrTbl record);

    PsTzMspsGrTbl selectByPrimaryKey(String tzClpsGrId);

    int updateByPrimaryKeySelective(PsTzMspsGrTbl record);

    int updateByPrimaryKey(PsTzMspsGrTbl record);
}