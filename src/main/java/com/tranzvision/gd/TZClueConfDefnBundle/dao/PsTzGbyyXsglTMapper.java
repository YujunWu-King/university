package com.tranzvision.gd.TZClueConfDefnBundle.dao;

import com.tranzvision.gd.TZClueConfDefnBundle.model.PsTzGbyyXsglT;

public interface PsTzGbyyXsglTMapper {
    int deleteByPrimaryKey(String tzGbyyId);

    int insert(PsTzGbyyXsglT record);

    int insertSelective(PsTzGbyyXsglT record);

    PsTzGbyyXsglT selectByPrimaryKey(String tzGbyyId);

    int updateByPrimaryKeySelective(PsTzGbyyXsglT record);

    int updateByPrimaryKey(PsTzGbyyXsglT record);
}