package com.tranzvision.gd.TZThemeMgBundle.dao;

import com.tranzvision.gd.TZThemeMgBundle.model.PsTzPtZtzyTblKey;

public interface PsTzPtZtzyTblMapper {
    int deleteByPrimaryKey(PsTzPtZtzyTblKey key);

    int insert(PsTzPtZtzyTblKey record);

    int insertSelective(PsTzPtZtzyTblKey record);
}