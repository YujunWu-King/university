package com.tranzvision.gd.TZControlSetBundle.dao;

import com.tranzvision.gd.TZControlSetBundle.model.PsTzComDyT;

public interface PsTzComDyTMapper {
    int deleteByPrimaryKey(String tzComId);

    int insert(PsTzComDyT record);

    int insertSelective(PsTzComDyT record);

    PsTzComDyT selectByPrimaryKey(String tzComId);

    int updateByPrimaryKeySelective(PsTzComDyT record);

    int updateByPrimaryKeyWithBLOBs(PsTzComDyT record);

    int updateByPrimaryKey(PsTzComDyT record);
}