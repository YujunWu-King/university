package com.tranzvision.gd.TZBatchBundle.dao;

import com.tranzvision.gd.TZBatchBundle.model.TzJincDyT;
import com.tranzvision.gd.TZBatchBundle.model.TzJincDyTKey;

public interface TzJincDyTMapper {
    int deleteByPrimaryKey(TzJincDyTKey key);

    int insert(TzJincDyT record);

    int insertSelective(TzJincDyT record);

    TzJincDyT selectByPrimaryKey(TzJincDyTKey key);

    int updateByPrimaryKeySelective(TzJincDyT record);

    int updateByPrimaryKey(TzJincDyT record);
}