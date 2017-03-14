package com.tranzvision.gd.TZAutomaticTagBundle.dao;

import com.tranzvision.gd.TZAutomaticTagBundle.model.PsQynDbblKsTKey;

public interface PsQynDbblKsTMapper {
    int deleteByPrimaryKey(PsQynDbblKsTKey key);

    int insert(PsQynDbblKsTKey record);

    int insertSelective(PsQynDbblKsTKey record);
}