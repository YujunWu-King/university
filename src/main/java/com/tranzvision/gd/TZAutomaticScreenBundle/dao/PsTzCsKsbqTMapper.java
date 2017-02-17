package com.tranzvision.gd.TZAutomaticScreenBundle.dao;

import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsbqTKey;

public interface PsTzCsKsbqTMapper {
    int deleteByPrimaryKey(PsTzCsKsbqTKey key);

    int insert(PsTzCsKsbqTKey record);

    int insertSelective(PsTzCsKsbqTKey record);
}