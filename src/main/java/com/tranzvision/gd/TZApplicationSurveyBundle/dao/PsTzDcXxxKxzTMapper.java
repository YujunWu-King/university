package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcXxxKxzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcXxxKxzTKey;

public interface PsTzDcXxxKxzTMapper {
    int deleteByPrimaryKey(PsTzDcXxxKxzTKey key);

    int insert(PsTzDcXxxKxzT record);

    int insertSelective(PsTzDcXxxKxzT record);

    PsTzDcXxxKxzT selectByPrimaryKey(PsTzDcXxxKxzTKey key);

    int updateByPrimaryKeySelective(PsTzDcXxxKxzT record);

    int updateByPrimaryKey(PsTzDcXxxKxzT record);
}