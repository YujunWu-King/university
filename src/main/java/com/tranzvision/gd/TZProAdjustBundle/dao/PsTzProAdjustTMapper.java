package com.tranzvision.gd.TZProAdjustBundle.dao;

import com.tranzvision.gd.TZProAdjustBundle.model.PsTzProAdjustT;

public interface PsTzProAdjustTMapper {
    int deleteByPrimaryKey(Integer tzProadjustId);

    int insert(PsTzProAdjustT record);

    int insertSelective(PsTzProAdjustT record);

    PsTzProAdjustT selectByPrimaryKey(Integer tzProadjustId);

    int updateByPrimaryKeySelective(PsTzProAdjustT record);

    int updateByPrimaryKey(PsTzProAdjustT record);
}