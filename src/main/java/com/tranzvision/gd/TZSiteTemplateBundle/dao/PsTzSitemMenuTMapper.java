package com.tranzvision.gd.TZSiteTemplateBundle.dao;

import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemMenuT;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemMenuTKey;

public interface PsTzSitemMenuTMapper {
    int deleteByPrimaryKey(PsTzSitemMenuTKey key);

    int insert(PsTzSitemMenuT record);

    int insertSelective(PsTzSitemMenuT record);

    PsTzSitemMenuT selectByPrimaryKey(PsTzSitemMenuTKey key);

    int updateByPrimaryKeySelective(PsTzSitemMenuT record);

    int updateByPrimaryKey(PsTzSitemMenuT record);
}