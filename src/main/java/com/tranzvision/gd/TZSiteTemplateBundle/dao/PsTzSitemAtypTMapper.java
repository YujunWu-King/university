package com.tranzvision.gd.TZSiteTemplateBundle.dao;

import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemAtypT;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemAtypTKey;

public interface PsTzSitemAtypTMapper {
    int deleteByPrimaryKey(PsTzSitemAtypTKey key);

    int insert(PsTzSitemAtypT record);

    int insertSelective(PsTzSitemAtypT record);

    PsTzSitemAtypT selectByPrimaryKey(PsTzSitemAtypTKey key);

    int updateByPrimaryKeySelective(PsTzSitemAtypT record);

    int updateByPrimaryKey(PsTzSitemAtypT record);
}