package com.tranzvision.gd.TZCostRelatedAEBundle.dao;

import com.tranzvision.gd.TZCostRelatedAEBundle.model.TzJxjDefnT;

import java.util.List;

public interface TzJxjDefnTMapper {
    int deleteByPrimaryKey(String tzJxjId);

    int insert(TzJxjDefnT record);

    int insertSelective(TzJxjDefnT record);

    TzJxjDefnT selectByPrimaryKey(String tzJxjId);

    int updateByPrimaryKeySelective(TzJxjDefnT record);

    int updateByPrimaryKey(TzJxjDefnT record);

    int insertForeach(List<TzJxjDefnT> list);
}