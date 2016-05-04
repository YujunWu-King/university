package com.tranzvision.gd.TZBatchBundle.dao;

import com.tranzvision.gd.TZBatchBundle.model.TzXunhDefnT;
import com.tranzvision.gd.TZBatchBundle.model.TzXunhDefnTKey;

public interface TzXunhDefnTMapper {
    int deleteByPrimaryKey(TzXunhDefnTKey key);

    int insert(TzXunhDefnT record);

    int insertSelective(TzXunhDefnT record);

    TzXunhDefnT selectByPrimaryKey(TzXunhDefnTKey key);

    int updateByPrimaryKeySelective(TzXunhDefnT record);

    int updateByPrimaryKey(TzXunhDefnT record);
}