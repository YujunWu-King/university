package com.tranzvision.gd.TZCostRelatedAEBundle.dao;

import com.tranzvision.gd.TZCostRelatedAEBundle.model.TzKsJxjT;
import com.tranzvision.gd.TZCostRelatedAEBundle.model.TzKsJxjTKey;

import java.util.List;

public interface TzKsJxjTMapper {
    int deleteByPrimaryKey(TzKsJxjTKey key);

    int insert(TzKsJxjT record);

    int insertSelective(TzKsJxjT record);

    TzKsJxjT selectByPrimaryKey(TzKsJxjTKey key);

    int updateByPrimaryKeySelective(TzKsJxjT record);

    int updateByPrimaryKey(TzKsJxjT record);

    int insertForeach(List<TzKsJxjT> list);
}