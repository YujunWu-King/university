package com.tranzvision.gd.TZMyEnrollmentClueBundle.dao;

import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsDcAet;

public interface PsTzXsxsDcAetMapper {
    int deleteByPrimaryKey(String runCntlId);

    int insert(PsTzXsxsDcAet record);

    int insertSelective(PsTzXsxsDcAet record);

    PsTzXsxsDcAet selectByPrimaryKey(String runCntlId);

    int updateByPrimaryKeySelective(PsTzXsxsDcAet record);

    int updateByPrimaryKeyWithBLOBs(PsTzXsxsDcAet record);

    int updateByPrimaryKey(PsTzXsxsDcAet record);
}