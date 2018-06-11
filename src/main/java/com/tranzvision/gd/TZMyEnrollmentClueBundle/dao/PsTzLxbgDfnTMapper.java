package com.tranzvision.gd.TZMyEnrollmentClueBundle.dao;

import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzLxbgDfnT;

public interface PsTzLxbgDfnTMapper {
    int deleteByPrimaryKey(Long tzCallreportId);

    int insert(PsTzLxbgDfnT record);

    int insertSelective(PsTzLxbgDfnT record);

    PsTzLxbgDfnT selectByPrimaryKey(Long tzCallreportId);

    int updateByPrimaryKeySelective(PsTzLxbgDfnT record);

    int updateByPrimaryKeyWithBLOBs(PsTzLxbgDfnT record);

    int updateByPrimaryKey(PsTzLxbgDfnT record);
}