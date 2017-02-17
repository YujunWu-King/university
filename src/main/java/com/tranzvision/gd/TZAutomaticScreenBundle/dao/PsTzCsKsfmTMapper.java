package com.tranzvision.gd.TZAutomaticScreenBundle.dao;

import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsfmTKey;

public interface PsTzCsKsfmTMapper {
    int deleteByPrimaryKey(PsTzCsKsfmTKey key);

    int insert(PsTzCsKsfmTKey record);

    int insertSelective(PsTzCsKsfmTKey record);
}