package com.tranzvision.gd.TZAdditionalInformationBundle.dao;

import com.tranzvision.gd.TZAdditionalInformationBundle.model.PsTzSbmInfStpT;
import com.tranzvision.gd.TZAdditionalInformationBundle.model.PsTzSbmInfStpTKey;

public interface PsTzSbmInfStpTMapper {
    int deleteByPrimaryKey(PsTzSbmInfStpTKey key);

    int insert(PsTzSbmInfStpT record);

    int insertSelective(PsTzSbmInfStpT record);

    PsTzSbmInfStpT selectByPrimaryKey(PsTzSbmInfStpTKey key);

    int updateByPrimaryKeySelective(PsTzSbmInfStpT record);

    int updateByPrimaryKeyWithBLOBs(PsTzSbmInfStpT record);

    int updateByPrimaryKey(PsTzSbmInfStpT record);
}