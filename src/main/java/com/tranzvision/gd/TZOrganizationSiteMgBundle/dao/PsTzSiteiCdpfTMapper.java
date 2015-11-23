package com.tranzvision.gd.TZOrganizationSiteMgBundle.dao;

import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiCdpfT;
import com.tranzvision.gd.TZOrganizationSiteMgBundle.model.PsTzSiteiCdpfTKey;

public interface PsTzSiteiCdpfTMapper {
    int deleteByPrimaryKey(PsTzSiteiCdpfTKey key);

    int insert(PsTzSiteiCdpfT record);

    int insertSelective(PsTzSiteiCdpfT record);

    PsTzSiteiCdpfT selectByPrimaryKey(PsTzSiteiCdpfTKey key);

    int updateByPrimaryKeySelective(PsTzSiteiCdpfT record);

    int updateByPrimaryKey(PsTzSiteiCdpfT record);
}