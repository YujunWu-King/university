package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjattT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjattTKey;

public interface PsTzDcWjattTMapper {
    int deleteByPrimaryKey(PsTzDcWjattTKey key);

    int insert(PsTzDcWjattT record);

    int insertSelective(PsTzDcWjattT record);

    PsTzDcWjattT selectByPrimaryKey(PsTzDcWjattTKey key);

    int updateByPrimaryKeySelective(PsTzDcWjattT record);

    int updateByPrimaryKey(PsTzDcWjattT record);
}