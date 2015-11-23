package com.tranzvision.gd.TZOrganizationSiteMgBundle.dao;

import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAtypT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiAtypTKey;

public interface PsTzSiteiAtypTMapper {
    int deleteByPrimaryKey(PsTzSiteiAtypTKey key);

    int insert(PsTzSiteiAtypT record);

    int insertSelective(PsTzSiteiAtypT record);

    PsTzSiteiAtypT selectByPrimaryKey(PsTzSiteiAtypTKey key);

    int updateByPrimaryKeySelective(PsTzSiteiAtypT record);

    int updateByPrimaryKey(PsTzSiteiAtypT record);
}