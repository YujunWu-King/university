package com.tranzvision.gd.TZOrganizationOutSiteMgBundle.dao;

import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.model.PsTzContFldefT;
import com.tranzvision.gd.TZOrganizationOutSiteMgBundle.model.PsTzContFldefTKey;

public interface PsTzContFldefTMapper {
    int deleteByPrimaryKey(PsTzContFldefTKey key);

    int insert(PsTzContFldefT record);

    int insertSelective(PsTzContFldefT record);

    PsTzContFldefT selectByPrimaryKey(PsTzContFldefTKey key);

    int updateByPrimaryKeySelective(PsTzContFldefT record);

    int updateByPrimaryKey(PsTzContFldefT record);
}