package com.tranzvision.gd.TZClassSetBundle.dao;

import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsDjzlT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsDjzlTKey;

public interface PsTzClsDjzlTMapper {
    int deleteByPrimaryKey(PsTzClsDjzlTKey key);

    int insert(PsTzClsDjzlT record);

    int insertSelective(PsTzClsDjzlT record);

    PsTzClsDjzlT selectByPrimaryKey(PsTzClsDjzlTKey key);

    int updateByPrimaryKeySelective(PsTzClsDjzlT record);

    int updateByPrimaryKeyWithBLOBs(PsTzClsDjzlT record);

    int updateByPrimaryKey(PsTzClsDjzlT record);
}