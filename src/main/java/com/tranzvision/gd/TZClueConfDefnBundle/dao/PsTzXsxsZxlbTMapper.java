package com.tranzvision.gd.TZClueConfDefnBundle.dao;

import com.tranzvision.gd.TZClueConfDefnBundle.model.PsTzXsxsZxlbT;

public interface PsTzXsxsZxlbTMapper {
    int deleteByPrimaryKey(String tzZxlbId);

    int insert(PsTzXsxsZxlbT record);

    int insertSelective(PsTzXsxsZxlbT record);

    PsTzXsxsZxlbT selectByPrimaryKey(String tzZxlbId);

    int updateByPrimaryKeySelective(PsTzXsxsZxlbT record);

    int updateByPrimaryKey(PsTzXsxsZxlbT record);
}