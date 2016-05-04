package com.tranzvision.gd.TZBatchBundle.dao;

import com.tranzvision.gd.TZBatchBundle.model.TzJcFwqdxT;
import com.tranzvision.gd.TZBatchBundle.model.TzJcFwqdxTKey;

public interface TzJcFwqdxTMapper {
    int deleteByPrimaryKey(TzJcFwqdxTKey key);

    int insert(TzJcFwqdxT record);

    int insertSelective(TzJcFwqdxT record);

    TzJcFwqdxT selectByPrimaryKey(TzJcFwqdxTKey key);

    int updateByPrimaryKeySelective(TzJcFwqdxT record);

    int updateByPrimaryKey(TzJcFwqdxT record);
}