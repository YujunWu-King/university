package com.tranzvision.gd.TZClassSetBundle.dao;

import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsMorinfT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsMorinfTKey;

public interface PsTzClsMorinfTMapper {
    int deleteByPrimaryKey(PsTzClsMorinfTKey key);

    int insert(PsTzClsMorinfT record);

    int insertSelective(PsTzClsMorinfT record);

    PsTzClsMorinfT selectByPrimaryKey(PsTzClsMorinfTKey key);

    int updateByPrimaryKeySelective(PsTzClsMorinfT record);

    int updateByPrimaryKey(PsTzClsMorinfT record);
}