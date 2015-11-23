package com.tranzvision.gd.TZOrganizationSiteMgBundle.dao;

import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiDefnT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiDefnTWithBLOBs;

public interface PsTzSiteiDefnTMapper {
    int deleteByPrimaryKey(String tzSiteiId);

    int insert(PsTzSiteiDefnTWithBLOBs record);

    int insertSelective(PsTzSiteiDefnTWithBLOBs record);

    PsTzSiteiDefnTWithBLOBs selectByPrimaryKey(String tzSiteiId);

    int updateByPrimaryKeySelective(PsTzSiteiDefnTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzSiteiDefnTWithBLOBs record);

    int updateByPrimaryKey(PsTzSiteiDefnT record);
}