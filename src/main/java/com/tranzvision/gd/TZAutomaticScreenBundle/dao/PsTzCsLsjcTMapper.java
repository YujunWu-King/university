package com.tranzvision.gd.TZAutomaticScreenBundle.dao;

import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsLsjcTKey;

public interface PsTzCsLsjcTMapper {
    int deleteByPrimaryKey(PsTzCsLsjcTKey key);

    int insert(PsTzCsLsjcTKey record);

    int insertSelective(PsTzCsLsjcTKey record);
}