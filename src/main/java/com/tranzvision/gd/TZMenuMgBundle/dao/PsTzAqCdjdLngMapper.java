package com.tranzvision.gd.TZMenuMgBundle.dao;

import com.tranzvision.gd.TZMenuMgBundle.model.PsTzAqCdjdLng;
import com.tranzvision.gd.TZMenuMgBundle.model.PsTzAqCdjdLngKey;

public interface PsTzAqCdjdLngMapper {
    int deleteByPrimaryKey(PsTzAqCdjdLngKey key);

    int insert(PsTzAqCdjdLng record);

    int insertSelective(PsTzAqCdjdLng record);

    PsTzAqCdjdLng selectByPrimaryKey(PsTzAqCdjdLngKey key);

    int updateByPrimaryKeySelective(PsTzAqCdjdLng record);

    int updateByPrimaryKey(PsTzAqCdjdLng record);
}