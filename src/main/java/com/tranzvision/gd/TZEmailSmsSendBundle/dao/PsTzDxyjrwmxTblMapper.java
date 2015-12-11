package com.tranzvision.gd.TZEmailSmsSendBundle.dao;

import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzDxyjrwmxTblKey;

public interface PsTzDxyjrwmxTblMapper {
    int deleteByPrimaryKey(PsTzDxyjrwmxTblKey key);

    int insert(PsTzDxyjrwmxTblKey record);

    int insertSelective(PsTzDxyjrwmxTblKey record);
}