package com.tranzvision.gd.TZOrganizationSiteMgBundle.dao;

import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMtypT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMtypTKey;

public interface PsTzSiteiMtypTMapper {
    int deleteByPrimaryKey(PsTzSiteiMtypTKey key);

    int insert(PsTzSiteiMtypT record);

    int insertSelective(PsTzSiteiMtypT record);

    PsTzSiteiMtypT selectByPrimaryKey(PsTzSiteiMtypTKey key);

    int updateByPrimaryKeySelective(PsTzSiteiMtypT record);

    int updateByPrimaryKeyWithBLOBs(PsTzSiteiMtypT record);

    int updateByPrimaryKey(PsTzSiteiMtypT record);
}