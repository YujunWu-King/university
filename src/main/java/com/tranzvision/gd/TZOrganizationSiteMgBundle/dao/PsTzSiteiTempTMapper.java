package com.tranzvision.gd.TZOrganizationSiteMgBundle.dao;

import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiTempT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiTempTKey;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiTempTWithBLOBs;

public interface PsTzSiteiTempTMapper {
    int deleteByPrimaryKey(PsTzSiteiTempTKey key);

    int insert(PsTzSiteiTempTWithBLOBs record);

    int insertSelective(PsTzSiteiTempTWithBLOBs record);

    PsTzSiteiTempTWithBLOBs selectByPrimaryKey(PsTzSiteiTempTKey key);

    int updateByPrimaryKeySelective(PsTzSiteiTempTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzSiteiTempTWithBLOBs record);

    int updateByPrimaryKey(PsTzSiteiTempT record);
}