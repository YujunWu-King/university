package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcwjBgkxzT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcwjBgkxzTKey;

public interface PsTzDcwjBgkxzTMapper {
    int deleteByPrimaryKey(PsTzDcwjBgkxzTKey key);

    int insert(PsTzDcwjBgkxzT record);

    int insertSelective(PsTzDcwjBgkxzT record);

    PsTzDcwjBgkxzT selectByPrimaryKey(PsTzDcwjBgkxzTKey key);

    int updateByPrimaryKeySelective(PsTzDcwjBgkxzT record);

    int updateByPrimaryKey(PsTzDcwjBgkxzT record);
}