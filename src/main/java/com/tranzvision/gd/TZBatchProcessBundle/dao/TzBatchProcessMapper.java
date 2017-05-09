package com.tranzvision.gd.TZBatchProcessBundle.dao;

import com.tranzvision.gd.TZBatchProcessBundle.model.TzBatchProcess;
import com.tranzvision.gd.TZBatchProcessBundle.model.TzBatchProcessKey;

public interface TzBatchProcessMapper {
    int deleteByPrimaryKey(TzBatchProcessKey key);

    int insert(TzBatchProcess record);

    int insertSelective(TzBatchProcess record);

    TzBatchProcess selectByPrimaryKey(TzBatchProcessKey key);

    int updateByPrimaryKeySelective(TzBatchProcess record);

    int updateByPrimaryKey(TzBatchProcess record);
}