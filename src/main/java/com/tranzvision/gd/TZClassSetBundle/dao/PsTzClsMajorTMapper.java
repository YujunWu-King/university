package com.tranzvision.gd.TZClassSetBundle.dao;

import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsMajorT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsMajorTKey;

public interface PsTzClsMajorTMapper {
    int deleteByPrimaryKey(PsTzClsMajorTKey key);

    int insert(PsTzClsMajorT record);

    int insertSelective(PsTzClsMajorT record);

    PsTzClsMajorT selectByPrimaryKey(PsTzClsMajorTKey key);

    int updateByPrimaryKeySelective(PsTzClsMajorT record);

    int updateByPrimaryKey(PsTzClsMajorT record);
}