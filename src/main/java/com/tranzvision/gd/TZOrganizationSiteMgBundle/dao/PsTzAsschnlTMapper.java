package com.tranzvision.gd.TZOrganizationSiteMgBundle.dao;

import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzAsschnlTKey;

public interface PsTzAsschnlTMapper {
    int deleteByPrimaryKey(PsTzAsschnlTKey key);

    int insert(PsTzAsschnlTKey record);

    int insertSelective(PsTzAsschnlTKey record);
}