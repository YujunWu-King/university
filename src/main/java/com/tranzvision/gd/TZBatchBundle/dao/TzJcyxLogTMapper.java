package com.tranzvision.gd.TZBatchBundle.dao;

import com.tranzvision.gd.TZBatchBundle.model.TzJcyxLogT;
import com.tranzvision.gd.TZBatchBundle.model.TzJcyxLogTKey;

public interface TzJcyxLogTMapper {
    int deleteByPrimaryKey(TzJcyxLogTKey key);

    int insert(TzJcyxLogT record);

    int insertSelective(TzJcyxLogT record);

    TzJcyxLogT selectByPrimaryKey(TzJcyxLogTKey key);

    int updateByPrimaryKeySelective(TzJcyxLogT record);

    int updateByPrimaryKey(TzJcyxLogT record);
}