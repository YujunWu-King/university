package com.tranzvision.gd.TZCostRelatedAEBundle.dao;

import com.tranzvision.gd.TZCostRelatedAEBundle.model.TzJfPlanT;

import java.util.List;

public interface TzJfPlanTMapper {
    int deleteByPrimaryKey(String tzJfplId);

    int insert(TzJfPlanT record);

    int insertSelective(TzJfPlanT record);

    TzJfPlanT selectByPrimaryKey(String tzJfplId);

    int updateByPrimaryKeySelective(TzJfPlanT record);

    int updateByPrimaryKey(TzJfPlanT record);

    int insertForeach(List<TzJfPlanT> list);
}