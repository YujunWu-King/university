package com.tranzvision.gd.TZClassSetBundle.dao;

import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBmlcT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBmlcTKey;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBmlcTWithBLOBs;

public interface PsTzClsBmlcTMapper {
    int deleteByPrimaryKey(PsTzClsBmlcTKey key);

    int insert(PsTzClsBmlcTWithBLOBs record);

    int insertSelective(PsTzClsBmlcTWithBLOBs record);

    PsTzClsBmlcTWithBLOBs selectByPrimaryKey(PsTzClsBmlcTKey key);

    int updateByPrimaryKeySelective(PsTzClsBmlcTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzClsBmlcTWithBLOBs record);

    int updateByPrimaryKey(PsTzClsBmlcT record);
}