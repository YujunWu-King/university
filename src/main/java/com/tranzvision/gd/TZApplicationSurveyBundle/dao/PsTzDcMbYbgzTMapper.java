package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcMbYbgzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcMbYbgzTKey;

public interface PsTzDcMbYbgzTMapper {
    int deleteByPrimaryKey(PsTzDcMbYbgzTKey key);

    int insert(PsTzDcMbYbgzT record);

    int insertSelective(PsTzDcMbYbgzT record);

    PsTzDcMbYbgzT selectByPrimaryKey(PsTzDcMbYbgzTKey key);

    int updateByPrimaryKeySelective(PsTzDcMbYbgzT record);

    int updateByPrimaryKey(PsTzDcMbYbgzT record);
}