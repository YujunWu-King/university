package com.tranzvision.gd.TZDispatchLoopBundle.dao;

import com.tranzvision.gd.TZDispatchLoopBundle.model.TzDispatchLoop;
import com.tranzvision.gd.TZDispatchLoopBundle.model.TzDispatchLoopKey;

public interface TzDispatchLoopMapper {
    int deleteByPrimaryKey(TzDispatchLoopKey key);

    int insert(TzDispatchLoop record);

    int insertSelective(TzDispatchLoop record);

    TzDispatchLoop selectByPrimaryKey(TzDispatchLoopKey key);

    int updateByPrimaryKeySelective(TzDispatchLoop record);

    int updateByPrimaryKey(TzDispatchLoop record);
}