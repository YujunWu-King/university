package com.tranzvision.gd.TZOrganizationSiteMgBundle.dao;

import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzMsiteiDefnT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzMsiteiDefnTWithBLOBs;

public interface PsTzMsiteiDefnTMapper {
    int deleteByPrimaryKey(String tzSiteiId);

    int insert(PsTzMsiteiDefnTWithBLOBs record);

    int insertSelective(PsTzMsiteiDefnTWithBLOBs record);

    PsTzMsiteiDefnTWithBLOBs selectByPrimaryKey(String tzSiteiId);

    int updateByPrimaryKeySelective(PsTzMsiteiDefnTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzMsiteiDefnTWithBLOBs record);

    int updateByPrimaryKey(PsTzMsiteiDefnT record);
}