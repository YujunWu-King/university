package com.tranzvision.gd.TZOrganizationSiteMgBundle.dao;

import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiColuT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiColuTKey;

public interface PsTzSiteiColuTMapper {
    int deleteByPrimaryKey(PsTzSiteiColuTKey key);

    int insert(PsTzSiteiColuT record);

    int insertSelective(PsTzSiteiColuT record);

    PsTzSiteiColuT selectByPrimaryKey(PsTzSiteiColuTKey key);

    int updateByPrimaryKeySelective(PsTzSiteiColuT record);

    int updateByPrimaryKey(PsTzSiteiColuT record);
}