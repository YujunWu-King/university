package com.tranzvision.gd.TZClassSetBundle.dao;

import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBatchT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBatchTKey;

public interface PsTzClsBatchTMapper {
    int deleteByPrimaryKey(PsTzClsBatchTKey key);

    int insert(PsTzClsBatchT record);

    int insertSelective(PsTzClsBatchT record);

    PsTzClsBatchT selectByPrimaryKey(PsTzClsBatchTKey key);

    int updateByPrimaryKeySelective(PsTzClsBatchT record);

    int updateByPrimaryKey(PsTzClsBatchT record);
}