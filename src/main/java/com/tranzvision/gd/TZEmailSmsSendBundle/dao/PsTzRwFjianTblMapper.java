package com.tranzvision.gd.TZEmailSmsSendBundle.dao;

import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzRwFjianTbl;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzRwFjianTblKey;

public interface PsTzRwFjianTblMapper {
    int deleteByPrimaryKey(PsTzRwFjianTblKey key);

    int insert(PsTzRwFjianTbl record);

    int insertSelective(PsTzRwFjianTbl record);

    PsTzRwFjianTbl selectByPrimaryKey(PsTzRwFjianTblKey key);

    int updateByPrimaryKeySelective(PsTzRwFjianTbl record);

    int updateByPrimaryKey(PsTzRwFjianTbl record);
}