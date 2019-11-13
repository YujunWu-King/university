package com.tranzvision.gd.TZCostRelatedAEBundle.dao;

import com.tranzvision.gd.TZCostRelatedAEBundle.model.TzXyxfftTbl;
import com.tranzvision.gd.TZCostRelatedAEBundle.model.TzXyxfftTblKey;

import java.util.List;

public interface TzXyxfftTblMapper {
    int deleteByPrimaryKey(TzXyxfftTblKey key);

    int insert(TzXyxfftTbl record);

    int insertSelective(TzXyxfftTbl record);

    TzXyxfftTbl selectByPrimaryKey(TzXyxfftTblKey key);

    int updateByPrimaryKeySelective(TzXyxfftTbl record);

    int updateByPrimaryKey(TzXyxfftTbl record);

    int insertForeach(List<TzXyxfftTbl> list);
}