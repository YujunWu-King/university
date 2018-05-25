package com.tranzvision.gd.TZMyEnrollmentClueBundle.dao;

import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsLogT;

public interface PsTzXsxsLogTMapper {
    int deleteByPrimaryKey(Integer tzOperateId);

    int insert(PsTzXsxsLogT record);

    int insertSelective(PsTzXsxsLogT record);

    PsTzXsxsLogT selectByPrimaryKey(Integer tzOperateId);

    int updateByPrimaryKeySelective(PsTzXsxsLogT record);

    int updateByPrimaryKey(PsTzXsxsLogT record);
}