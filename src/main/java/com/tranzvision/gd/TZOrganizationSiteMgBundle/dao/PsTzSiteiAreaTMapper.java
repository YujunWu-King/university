package com.tranzvision.gd.TZOrganizationSiteMgBundle.dao;

import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAreaT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAreaTKey;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAreaTWithBLOBs;

public interface PsTzSiteiAreaTMapper {
    int deleteByPrimaryKey(PsTzSiteiAreaTKey key);

    int insert(PsTzSiteiAreaTWithBLOBs record);

    int insertSelective(PsTzSiteiAreaTWithBLOBs record);

    PsTzSiteiAreaTWithBLOBs selectByPrimaryKey(PsTzSiteiAreaTKey key);

    int updateByPrimaryKeySelective(PsTzSiteiAreaTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzSiteiAreaTWithBLOBs record);

    int updateByPrimaryKey(PsTzSiteiAreaT record);
}