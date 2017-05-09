package com.tranzvision.gd.TZBatchProcessBundle.dao;

import com.tranzvision.gd.TZBatchProcessBundle.model.TzProcessServer;
import com.tranzvision.gd.TZBatchProcessBundle.model.TzProcessServerKey;

public interface TzProcessServerMapper {
    int deleteByPrimaryKey(TzProcessServerKey key);

    int insert(TzProcessServer record);

    int insertSelective(TzProcessServer record);

    TzProcessServer selectByPrimaryKey(TzProcessServerKey key);

    int updateByPrimaryKeySelective(TzProcessServer record);

    int updateByPrimaryKey(TzProcessServer record);
}