package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjXxxPzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjXxxPzTKey;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjXxxPzTWithBLOBs;

public interface PsTzDcWjXxxPzTMapper {
    int deleteByPrimaryKey(PsTzDcWjXxxPzTKey key);

    int insert(PsTzDcWjXxxPzTWithBLOBs record);

    int insertSelective(PsTzDcWjXxxPzTWithBLOBs record);

    PsTzDcWjXxxPzTWithBLOBs selectByPrimaryKey(PsTzDcWjXxxPzTKey key);

    int updateByPrimaryKeySelective(PsTzDcWjXxxPzTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzDcWjXxxPzTWithBLOBs record);

    int updateByPrimaryKey(PsTzDcWjXxxPzT record);
}