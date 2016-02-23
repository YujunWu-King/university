package com.tranzvision.gd.TZApplicationVerifiedBundle.dao;

import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFormZlshT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFormZlshTKey;

public interface PsTzFormZlshTMapper {
    int deleteByPrimaryKey(PsTzFormZlshTKey key);

    int insert(PsTzFormZlshT record);

    int insertSelective(PsTzFormZlshT record);

    PsTzFormZlshT selectByPrimaryKey(PsTzFormZlshTKey key);

    int updateByPrimaryKeySelective(PsTzFormZlshT record);

    int updateByPrimaryKey(PsTzFormZlshT record);
}