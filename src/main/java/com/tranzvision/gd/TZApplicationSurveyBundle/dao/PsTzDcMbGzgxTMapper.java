package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcMbGzgxT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcMbGzgxTKey;

public interface PsTzDcMbGzgxTMapper {
    int deleteByPrimaryKey(PsTzDcMbGzgxTKey key);

    int insert(PsTzDcMbGzgxT record);

    int insertSelective(PsTzDcMbGzgxT record);

    PsTzDcMbGzgxT selectByPrimaryKey(PsTzDcMbGzgxTKey key);

    int updateByPrimaryKeySelective(PsTzDcMbGzgxT record);

    int updateByPrimaryKey(PsTzDcMbGzgxT record);
}