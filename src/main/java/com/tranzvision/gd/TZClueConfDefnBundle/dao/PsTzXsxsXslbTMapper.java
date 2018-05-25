package com.tranzvision.gd.TZClueConfDefnBundle.dao;

import com.tranzvision.gd.TZClueConfDefnBundle.model.PsTzXsxsXslbT;

public interface PsTzXsxsXslbTMapper {
    int deleteByPrimaryKey(String tzColourSortId);

    int insert(PsTzXsxsXslbT record);

    int insertSelective(PsTzXsxsXslbT record);

    PsTzXsxsXslbT selectByPrimaryKey(String tzColourSortId);

    int updateByPrimaryKeySelective(PsTzXsxsXslbT record);

    int updateByPrimaryKey(PsTzXsxsXslbT record);
}