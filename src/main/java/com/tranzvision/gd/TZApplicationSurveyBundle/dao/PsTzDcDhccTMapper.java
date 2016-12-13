package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcDhccT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcDhccTKey;

public interface PsTzDcDhccTMapper {
    int deleteByPrimaryKey(PsTzDcDhccTKey key);

    int insert(PsTzDcDhccT record);

    int insertSelective(PsTzDcDhccT record);

    PsTzDcDhccT selectByPrimaryKey(PsTzDcDhccTKey key);

    int updateByPrimaryKeySelective(PsTzDcDhccT record);

    int updateByPrimaryKey(PsTzDcDhccT record);
}