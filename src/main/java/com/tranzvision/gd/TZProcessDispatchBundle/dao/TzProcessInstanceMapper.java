package com.tranzvision.gd.TZProcessDispatchBundle.dao;

import com.tranzvision.gd.TZProcessDispatchBundle.model.TzProcessInstance;
import com.tranzvision.gd.TZProcessDispatchBundle.model.TzProcessInstanceKey;

public interface TzProcessInstanceMapper {
    int deleteByPrimaryKey(TzProcessInstanceKey key);

    int insert(TzProcessInstance record);

    int insertSelective(TzProcessInstance record);

    TzProcessInstance selectByPrimaryKey(TzProcessInstanceKey key);

    int updateByPrimaryKeySelective(TzProcessInstance record);

    int updateByPrimaryKey(TzProcessInstance record);
}