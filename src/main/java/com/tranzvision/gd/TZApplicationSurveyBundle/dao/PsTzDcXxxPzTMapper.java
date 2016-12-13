package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcXxxPzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcXxxPzTKey;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcXxxPzTWithBLOBs;

public interface PsTzDcXxxPzTMapper {
    int deleteByPrimaryKey(PsTzDcXxxPzTKey key);

    int insert(PsTzDcXxxPzTWithBLOBs record);

    int insertSelective(PsTzDcXxxPzTWithBLOBs record);

    PsTzDcXxxPzTWithBLOBs selectByPrimaryKey(PsTzDcXxxPzTKey key);

    int updateByPrimaryKeySelective(PsTzDcXxxPzTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzDcXxxPzTWithBLOBs record);

    int updateByPrimaryKey(PsTzDcXxxPzT record);
}