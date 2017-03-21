package com.tranzvision.gd.TZAutomaticScreenBundle.dao;

import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsbqT;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsKsbqTKey;

public interface PsTzCsKsbqTMapper {
    int deleteByPrimaryKey(PsTzCsKsbqTKey key);

    int insert(PsTzCsKsbqT record);

    int insertSelective(PsTzCsKsbqT record);

    PsTzCsKsbqT selectByPrimaryKey(PsTzCsKsbqTKey key);

    int updateByPrimaryKeySelective(PsTzCsKsbqT record);

    int updateByPrimaryKey(PsTzCsKsbqT record);
}