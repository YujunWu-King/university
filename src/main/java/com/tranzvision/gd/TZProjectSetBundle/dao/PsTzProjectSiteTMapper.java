package com.tranzvision.gd.TZProjectSetBundle.dao;

import com.tranzvision.gd.TZProjectSetBundle.model.PsTzProjectSiteTKey;

public interface PsTzProjectSiteTMapper {
    int deleteByPrimaryKey(PsTzProjectSiteTKey key);

    int insert(PsTzProjectSiteTKey record);

    int insertSelective(PsTzProjectSiteTKey record);
}