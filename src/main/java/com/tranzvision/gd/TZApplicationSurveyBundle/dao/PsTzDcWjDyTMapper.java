package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjDyT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjDyTWithBLOBs;

public interface PsTzDcWjDyTMapper {
    int deleteByPrimaryKey(String tzDcWjId);

    int insert(PsTzDcWjDyTWithBLOBs record);

    int insertSelective(PsTzDcWjDyTWithBLOBs record);

    PsTzDcWjDyTWithBLOBs selectByPrimaryKey(String tzDcWjId);

    int updateByPrimaryKeySelective(PsTzDcWjDyTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzDcWjDyTWithBLOBs record);

    int updateByPrimaryKey(PsTzDcWjDyT record);
}