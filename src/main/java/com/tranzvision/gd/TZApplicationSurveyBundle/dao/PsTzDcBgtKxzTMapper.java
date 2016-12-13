package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcBgtKxzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcBgtKxzTKey;

public interface PsTzDcBgtKxzTMapper {
    int deleteByPrimaryKey(PsTzDcBgtKxzTKey key);

    int insert(PsTzDcBgtKxzT record);

    int insertSelective(PsTzDcBgtKxzT record);

    PsTzDcBgtKxzT selectByPrimaryKey(PsTzDcBgtKxzTKey key);

    int updateByPrimaryKeySelective(PsTzDcBgtKxzT record);

    int updateByPrimaryKey(PsTzDcBgtKxzT record);
}