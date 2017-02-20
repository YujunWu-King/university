package com.tranzvision.gd.TZAutomaticScreenBundle.dao;

import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsJcT;
import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzCsJcTKey;

public interface PsTzCsJcTMapper {
    int deleteByPrimaryKey(PsTzCsJcTKey key);

    int insert(PsTzCsJcT record);

    int insertSelective(PsTzCsJcT record);

    PsTzCsJcT selectByPrimaryKey(PsTzCsJcTKey key);

    int updateByPrimaryKeySelective(PsTzCsJcT record);

    int updateByPrimaryKey(PsTzCsJcT record);
}