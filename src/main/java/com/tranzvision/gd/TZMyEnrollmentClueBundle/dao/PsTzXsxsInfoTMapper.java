package com.tranzvision.gd.TZMyEnrollmentClueBundle.dao;

import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsInfoT;
import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzXsxsInfoTWithBLOBs;

public interface PsTzXsxsInfoTMapper {
    int deleteByPrimaryKey(String tzLeadId);

    int insert(PsTzXsxsInfoTWithBLOBs record);

    int insertSelective(PsTzXsxsInfoTWithBLOBs record);

    PsTzXsxsInfoTWithBLOBs selectByPrimaryKey(String tzLeadId);

    int updateByPrimaryKeySelective(PsTzXsxsInfoTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzXsxsInfoTWithBLOBs record);

    int updateByPrimaryKey(PsTzXsxsInfoT record);
}