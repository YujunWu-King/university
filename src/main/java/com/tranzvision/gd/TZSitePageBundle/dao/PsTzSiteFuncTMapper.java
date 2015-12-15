package com.tranzvision.gd.TZSitePageBundle.dao;

import com.tranzvision.gd.TZSitePageBundle.model.PsTzSiteFuncT;

public interface PsTzSiteFuncTMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PsTzSiteFuncT record);

    int insertSelective(PsTzSiteFuncT record);

    PsTzSiteFuncT selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PsTzSiteFuncT record);

    int updateByPrimaryKey(PsTzSiteFuncT record);
}