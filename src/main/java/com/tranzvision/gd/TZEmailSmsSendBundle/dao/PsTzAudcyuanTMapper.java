package com.tranzvision.gd.TZEmailSmsSendBundle.dao;

import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzAudcyuanT;
import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzAudcyuanTKey;

public interface PsTzAudcyuanTMapper {
    int deleteByPrimaryKey(PsTzAudcyuanTKey key);

    int insert(PsTzAudcyuanT record);

    int insertSelective(PsTzAudcyuanT record);

    PsTzAudcyuanT selectByPrimaryKey(PsTzAudcyuanTKey key);

    int updateByPrimaryKeySelective(PsTzAudcyuanT record);

    int updateByPrimaryKey(PsTzAudcyuanT record);
}