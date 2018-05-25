package com.tranzvision.gd.TZClueConfDefnBundle.dao;

import com.tranzvision.gd.TZClueConfDefnBundle.model.PsTzThyyXsglT;

public interface PsTzThyyXsglTMapper {
    int deleteByPrimaryKey(String tzThyyId);

    int insert(PsTzThyyXsglT record);

    int insertSelective(PsTzThyyXsglT record);

    PsTzThyyXsglT selectByPrimaryKey(String tzThyyId);

    int updateByPrimaryKeySelective(PsTzThyyXsglT record);

    int updateByPrimaryKey(PsTzThyyXsglT record);
}