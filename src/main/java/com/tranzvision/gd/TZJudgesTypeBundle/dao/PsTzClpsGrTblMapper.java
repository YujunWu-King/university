package com.tranzvision.gd.TZJudgesTypeBundle.dao;

import java.util.List;

import com.tranzvision.gd.TZJudgesTypeBundle.model.PsTzClpsGrTbl;

public interface PsTzClpsGrTblMapper {
    int deleteByPrimaryKey(String tzClpsGrId);

    int insert(PsTzClpsGrTbl record);

    int insertSelective(PsTzClpsGrTbl record);

    PsTzClpsGrTbl selectByPrimaryKey(String tzClpsGrId);

    int updateByPrimaryKeySelective(PsTzClpsGrTbl record);

    int updateByPrimaryKey(PsTzClpsGrTbl record);

	List<PsTzClpsGrTbl> findAll();
}