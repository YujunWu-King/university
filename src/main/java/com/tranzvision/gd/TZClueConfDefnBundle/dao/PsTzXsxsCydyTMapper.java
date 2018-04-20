package com.tranzvision.gd.TZClueConfDefnBundle.dao;

import com.tranzvision.gd.TZClueConfDefnBundle.model.PsTzXsxsCydyT;

public interface PsTzXsxsCydyTMapper {
    int deleteByPrimaryKey(String tzCydyId);

    int insert(PsTzXsxsCydyT record);

    int insertSelective(PsTzXsxsCydyT record);

    PsTzXsxsCydyT selectByPrimaryKey(String tzCydyId);

    int updateByPrimaryKeySelective(PsTzXsxsCydyT record);

    int updateByPrimaryKey(PsTzXsxsCydyT record);
}