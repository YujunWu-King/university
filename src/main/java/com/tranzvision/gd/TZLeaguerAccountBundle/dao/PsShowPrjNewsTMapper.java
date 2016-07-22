package com.tranzvision.gd.TZLeaguerAccountBundle.dao;

import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsShowPrjNewsTKey;

public interface PsShowPrjNewsTMapper {
    int deleteByPrimaryKey(PsShowPrjNewsTKey key);

    int insert(PsShowPrjNewsTKey record);

    int insertSelective(PsShowPrjNewsTKey record);
}