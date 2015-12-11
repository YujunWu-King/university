package com.tranzvision.gd.TZEmailSmsSendBundle.dao;

import com.tranzvision.gd.TZEmailSmsSendBundle.model.PsTzAudienceT;

public interface PsTzAudienceTMapper {
    int deleteByPrimaryKey(String tzAudienceId);

    int insert(PsTzAudienceT record);

    int insertSelective(PsTzAudienceT record);

    PsTzAudienceT selectByPrimaryKey(String tzAudienceId);

    int updateByPrimaryKeySelective(PsTzAudienceT record);

    int updateByPrimaryKey(PsTzAudienceT record);
}