package com.tranzvision.gd.TZOrganizationSiteMgBundle.dao;

import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMnpfT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiMnpfTKey;

public interface PsTzSiteiMnpfTMapper {
    int deleteByPrimaryKey(PsTzSiteiMnpfTKey key);

    int insert(PsTzSiteiMnpfT record);

    int insertSelective(PsTzSiteiMnpfT record);

    PsTzSiteiMnpfT selectByPrimaryKey(PsTzSiteiMnpfTKey key);

    int updateByPrimaryKeySelective(PsTzSiteiMnpfT record);

    int updateByPrimaryKey(PsTzSiteiMnpfT record);
}