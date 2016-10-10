package com.tranzvision.gd.TZDataRequestBundle.dao;

import com.tranzvision.gd.TZDataRequestBundle.model.PsTzDataRequestT;

public interface PsTzDataRequestTMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PsTzDataRequestT record);

    int insertSelective(PsTzDataRequestT record);

    PsTzDataRequestT selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PsTzDataRequestT record);

    int updateByPrimaryKey(PsTzDataRequestT record);
}