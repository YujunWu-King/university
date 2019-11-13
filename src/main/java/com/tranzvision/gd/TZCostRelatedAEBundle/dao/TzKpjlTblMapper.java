package com.tranzvision.gd.TZCostRelatedAEBundle.dao;

import com.tranzvision.gd.TZCostRelatedAEBundle.model.TzKpjlTbl;

import java.util.List;

public interface TzKpjlTblMapper {
    int deleteByPrimaryKey(String tzKpNbid);

    int insert(TzKpjlTbl record);

    int insertSelective(TzKpjlTbl record);

    TzKpjlTbl selectByPrimaryKey(String tzKpNbid);

    int updateByPrimaryKeySelective(TzKpjlTbl record);

    int updateByPrimaryKey(TzKpjlTbl record);

    int insertForeach(List<TzKpjlTbl> list);
}