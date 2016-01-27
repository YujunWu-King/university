package com.tranzvision.gd.TZAdditionalInformationBundle.dao;

import com.tranzvision.gd.TZAdditionalInformationBundle.model.PsTzSbmInfTmpT;

public interface PsTzSbmInfTmpTMapper {
    int deleteByPrimaryKey(String tzSbminfTmpId);

    int insert(PsTzSbmInfTmpT record);

    int insertSelective(PsTzSbmInfTmpT record);

    PsTzSbmInfTmpT selectByPrimaryKey(String tzSbminfTmpId);

    int updateByPrimaryKeySelective(PsTzSbmInfTmpT record);

    int updateByPrimaryKey(PsTzSbmInfTmpT record);
}