package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcDyT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcDyTWithBLOBs;

public interface PsTzDcDyTMapper {
    int deleteByPrimaryKey(String tzAppTplId);

    int insert(PsTzDcDyTWithBLOBs record);

    int insertSelective(PsTzDcDyTWithBLOBs record);

    PsTzDcDyTWithBLOBs selectByPrimaryKey(String tzAppTplId);

    int updateByPrimaryKeySelective(PsTzDcDyTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzDcDyTWithBLOBs record);

    int updateByPrimaryKey(PsTzDcDyT record);
}