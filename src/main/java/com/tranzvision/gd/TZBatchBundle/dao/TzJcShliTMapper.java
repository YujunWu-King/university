package com.tranzvision.gd.TZBatchBundle.dao;

import com.tranzvision.gd.TZBatchBundle.model.TzJcShliT;
import com.tranzvision.gd.TZBatchBundle.model.TzJcShliTKey;

public interface TzJcShliTMapper {
    int deleteByPrimaryKey(TzJcShliTKey key);

    int insert(TzJcShliT record);

    int insertSelective(TzJcShliT record);

    TzJcShliT selectByPrimaryKey(TzJcShliTKey key);

    int updateByPrimaryKeySelective(TzJcShliT record);

    int updateByPrimaryKey(TzJcShliT record);
}