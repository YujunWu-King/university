package com.tranzvision.gd.TZClassSetBundle.dao;

import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBmlchfT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsBmlchfTKey;

public interface PsTzClsBmlchfTMapper {
    int deleteByPrimaryKey(PsTzClsBmlchfTKey key);

    int insert(PsTzClsBmlchfT record);

    int insertSelective(PsTzClsBmlchfT record);

    PsTzClsBmlchfT selectByPrimaryKey(PsTzClsBmlchfTKey key);

    int updateByPrimaryKeySelective(PsTzClsBmlchfT record);

    int updateByPrimaryKeyWithBLOBs(PsTzClsBmlchfT record);

    int updateByPrimaryKey(PsTzClsBmlchfT record);
}