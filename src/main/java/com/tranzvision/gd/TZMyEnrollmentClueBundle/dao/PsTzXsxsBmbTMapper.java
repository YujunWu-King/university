package com.tranzvision.gd.TZMyEnrollmentClueBundle.dao;

import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsBmbT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsBmbTKey;

public interface PsTzXsxsBmbTMapper {
    int deleteByPrimaryKey(PsTzXsxsBmbTKey key);

    int insert(PsTzXsxsBmbT record);

    int insertSelective(PsTzXsxsBmbT record);

    PsTzXsxsBmbT selectByPrimaryKey(PsTzXsxsBmbTKey key);

    int updateByPrimaryKeySelective(PsTzXsxsBmbT record);

    int updateByPrimaryKey(PsTzXsxsBmbT record);
}