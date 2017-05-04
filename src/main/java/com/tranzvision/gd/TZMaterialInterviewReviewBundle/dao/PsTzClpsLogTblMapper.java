package com.tranzvision.gd.TZMaterialInterviewReviewBundle.dao;

import com.tranzvision.gd.TZMaterialInterviewReviewBundle.model.PsTzClpsLogTbl;

public interface PsTzClpsLogTblMapper {
    int deleteByPrimaryKey(String tzRzlsNum);

    int insert(PsTzClpsLogTbl record);

    int insertSelective(PsTzClpsLogTbl record);

    PsTzClpsLogTbl selectByPrimaryKey(String tzRzlsNum);

    int updateByPrimaryKeySelective(PsTzClpsLogTbl record);

    int updateByPrimaryKey(PsTzClpsLogTbl record);
}