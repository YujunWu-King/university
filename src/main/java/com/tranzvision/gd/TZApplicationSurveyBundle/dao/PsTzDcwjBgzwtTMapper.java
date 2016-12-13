package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcwjBgzwtT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcwjBgzwtTKey;

public interface PsTzDcwjBgzwtTMapper {
    int deleteByPrimaryKey(PsTzDcwjBgzwtTKey key);

    int insert(PsTzDcwjBgzwtT record);

    int insertSelective(PsTzDcwjBgzwtT record);

    PsTzDcwjBgzwtT selectByPrimaryKey(PsTzDcwjBgzwtTKey key);

    int updateByPrimaryKeySelective(PsTzDcwjBgzwtT record);

    int updateByPrimaryKey(PsTzDcwjBgzwtT record);
}