package com.tranzvision.gd.TZClassSetBundle.dao;

import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsHfdyT;
import com.tranzvision.gd.TZClassSetBundle.model.PsTzClsHfdyTKey;

public interface PsTzClsHfdyTMapper {
    int deleteByPrimaryKey(PsTzClsHfdyTKey key);

    int insert(PsTzClsHfdyT record);

    int insertSelective(PsTzClsHfdyT record);

    PsTzClsHfdyT selectByPrimaryKey(PsTzClsHfdyTKey key);

    int updateByPrimaryKeySelective(PsTzClsHfdyT record);

    int updateByPrimaryKey(PsTzClsHfdyT record);
}