package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcBgtZwtT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcBgtZwtTKey;

public interface PsTzDcBgtZwtTMapper {
    int deleteByPrimaryKey(PsTzDcBgtZwtTKey key);

    int insert(PsTzDcBgtZwtT record);

    int insertSelective(PsTzDcBgtZwtT record);

    PsTzDcBgtZwtT selectByPrimaryKey(PsTzDcBgtZwtTKey key);

    int updateByPrimaryKeySelective(PsTzDcBgtZwtT record);

    int updateByPrimaryKey(PsTzDcBgtZwtT record);
}