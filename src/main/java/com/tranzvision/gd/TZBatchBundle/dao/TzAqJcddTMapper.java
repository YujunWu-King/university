package com.tranzvision.gd.TZBatchBundle.dao;

import com.tranzvision.gd.TZBatchBundle.model.TzAqJcddT;
import com.tranzvision.gd.TZBatchBundle.model.TzAqJcddTKey;

public interface TzAqJcddTMapper {
    int deleteByPrimaryKey(TzAqJcddTKey key);

    int insert(TzAqJcddT record);

    int insertSelective(TzAqJcddT record);

    TzAqJcddT selectByPrimaryKey(TzAqJcddTKey key);

    int updateByPrimaryKeySelective(TzAqJcddT record);

    int updateByPrimaryKey(TzAqJcddT record);
}