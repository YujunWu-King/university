package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcInsT;

public interface PsTzDcInsTMapper {
    int deleteByPrimaryKey(Long tzAppInsId);

    int insert(PsTzDcInsT record);

    int insertSelective(PsTzDcInsT record);

    PsTzDcInsT selectByPrimaryKey(Long tzAppInsId);

    int updateByPrimaryKeySelective(PsTzDcInsT record);

    int updateByPrimaryKeyWithBLOBs(PsTzDcInsT record);

    int updateByPrimaryKey(PsTzDcInsT record);
}