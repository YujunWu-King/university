package com.tranzvision.gd.TZOrganizationSiteMgBundle.dao;

import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMenuT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMenuTKey;

public interface PsTzSiteiMenuTMapper {
    int deleteByPrimaryKey(PsTzSiteiMenuTKey key);

    int insert(PsTzSiteiMenuT record);

    int insertSelective(PsTzSiteiMenuT record);

    PsTzSiteiMenuT selectByPrimaryKey(PsTzSiteiMenuTKey key);

    int updateByPrimaryKeySelective(PsTzSiteiMenuT record);

    int updateByPrimaryKeyWithBLOBs(PsTzSiteiMenuT record);

    int updateByPrimaryKey(PsTzSiteiMenuT record);
}