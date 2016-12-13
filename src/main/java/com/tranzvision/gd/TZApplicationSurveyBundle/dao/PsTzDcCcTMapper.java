package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcCcT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcCcTKey;

public interface PsTzDcCcTMapper {
    int deleteByPrimaryKey(PsTzDcCcTKey key);

    int insert(PsTzDcCcT record);

    int insertSelective(PsTzDcCcT record);

    PsTzDcCcT selectByPrimaryKey(PsTzDcCcTKey key);

    int updateByPrimaryKeySelective(PsTzDcCcT record);

    int updateByPrimaryKeyWithBLOBs(PsTzDcCcT record);

    int updateByPrimaryKey(PsTzDcCcT record);
}