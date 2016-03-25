package com.tranzvision.gd.TZControlSetBundle.dao;

import com.tranzvision.gd.TZControlSetBundle.model.PsTzComDyT;
import com.tranzvision.gd.TZControlSetBundle.model.PsTzComDyTWithBLOBs;

public interface PsTzComDyTMapper {
    int deleteByPrimaryKey(String tzComId);

    int insert(PsTzComDyTWithBLOBs record);

    int insertSelective(PsTzComDyTWithBLOBs record);

    PsTzComDyTWithBLOBs selectByPrimaryKey(String tzComId);

    int updateByPrimaryKeySelective(PsTzComDyTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzComDyTWithBLOBs record);

    int updateByPrimaryKey(PsTzComDyT record);
}