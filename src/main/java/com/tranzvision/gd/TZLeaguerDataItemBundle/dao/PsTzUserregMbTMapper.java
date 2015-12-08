package com.tranzvision.gd.TZLeaguerDataItemBundle.dao;

import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzUserregMbT;

public interface PsTzUserregMbTMapper {
    int deleteByPrimaryKey(String tzJgId);

    int insert(PsTzUserregMbT record);

    int insertSelective(PsTzUserregMbT record);

    PsTzUserregMbT selectByPrimaryKey(String tzJgId);

    int updateByPrimaryKeySelective(PsTzUserregMbT record);

    int updateByPrimaryKeyWithBLOBs(PsTzUserregMbT record);

    int updateByPrimaryKey(PsTzUserregMbT record);
}