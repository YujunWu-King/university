package com.tranzvision.gd.TZSiteTemplateBundle.dao;

import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemCdpfT;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemCdpfTKey;

public interface PsTzSitemCdpfTMapper {
    int deleteByPrimaryKey(PsTzSitemCdpfTKey key);

    int insert(PsTzSitemCdpfT record);

    int insertSelective(PsTzSitemCdpfT record);

    PsTzSitemCdpfT selectByPrimaryKey(PsTzSitemCdpfTKey key);

    int updateByPrimaryKeySelective(PsTzSitemCdpfT record);

    int updateByPrimaryKey(PsTzSitemCdpfT record);
}