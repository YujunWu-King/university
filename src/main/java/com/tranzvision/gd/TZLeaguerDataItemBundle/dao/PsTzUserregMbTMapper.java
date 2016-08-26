package com.tranzvision.gd.TZLeaguerDataItemBundle.dao;

import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzUserregMbT;

public interface PsTzUserregMbTMapper {
    int deleteByPrimaryKey(String tzSiteiId);

    int insert(PsTzUserregMbT record);

    int insertSelective(PsTzUserregMbT record);

    PsTzUserregMbT selectByPrimaryKey(String tzSiteiId);

    int updateByPrimaryKeySelective(PsTzUserregMbT record);

    int updateByPrimaryKeyWithBLOBs(PsTzUserregMbT record);

    int updateByPrimaryKey(PsTzUserregMbT record);
}