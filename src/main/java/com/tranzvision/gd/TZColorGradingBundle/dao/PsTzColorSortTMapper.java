package com.tranzvision.gd.TZColorGradingBundle.dao;

import com.tranzvision.gd.TZColorGradingBundle.model.PsTzColorSortT;

public interface PsTzColorSortTMapper {
    int deleteByPrimaryKey(String tzColorSortId);

    int insert(PsTzColorSortT record);

    int insertSelective(PsTzColorSortT record);

    PsTzColorSortT selectByPrimaryKey(String tzColorSortId);

    int updateByPrimaryKeySelective(PsTzColorSortT record);

    int updateByPrimaryKey(PsTzColorSortT record);
}