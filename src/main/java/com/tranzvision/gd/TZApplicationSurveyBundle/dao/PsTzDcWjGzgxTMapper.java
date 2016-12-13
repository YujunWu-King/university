package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjGzgxT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcWjGzgxTKey;

public interface PsTzDcWjGzgxTMapper {
    int deleteByPrimaryKey(PsTzDcWjGzgxTKey key);

    int insert(PsTzDcWjGzgxT record);

    int insertSelective(PsTzDcWjGzgxT record);

    PsTzDcWjGzgxT selectByPrimaryKey(PsTzDcWjGzgxTKey key);

    int updateByPrimaryKeySelective(PsTzDcWjGzgxT record);

    int updateByPrimaryKey(PsTzDcWjGzgxT record);
}