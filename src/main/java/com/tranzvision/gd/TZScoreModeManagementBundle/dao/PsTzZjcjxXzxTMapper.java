package com.tranzvision.gd.TZScoreModeManagementBundle.dao;

import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzZjcjxXzxT;
import com.tranzvision.gd.TZScoreModeManagementBundle.model.PsTzZjcjxXzxTKey;

public interface PsTzZjcjxXzxTMapper {
    int deleteByPrimaryKey(PsTzZjcjxXzxTKey key);

    int insert(PsTzZjcjxXzxT record);

    int insertSelective(PsTzZjcjxXzxT record);

    PsTzZjcjxXzxT selectByPrimaryKey(PsTzZjcjxXzxTKey key);

    int updateByPrimaryKeySelective(PsTzZjcjxXzxT record);

    int updateByPrimaryKey(PsTzZjcjxXzxT record);
}