package com.tranzvision.gd.TZAdditionalInformationBundle.dao;

import com.tranzvision.gd.TZAdditionalInformationBundle.model.PsTzSbmInfRepT;
import com.tranzvision.gd.TZAdditionalInformationBundle.model.PsTzSbmInfRepTKey;

public interface PsTzSbmInfRepTMapper {
    int deleteByPrimaryKey(PsTzSbmInfRepTKey key);

    int insert(PsTzSbmInfRepT record);

    int insertSelective(PsTzSbmInfRepT record);

    PsTzSbmInfRepT selectByPrimaryKey(PsTzSbmInfRepTKey key);

    int updateByPrimaryKeySelective(PsTzSbmInfRepT record);

    int updateByPrimaryKey(PsTzSbmInfRepT record);
}