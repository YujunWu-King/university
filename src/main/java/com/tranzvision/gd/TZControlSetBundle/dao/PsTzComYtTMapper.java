package com.tranzvision.gd.TZControlSetBundle.dao;

import com.tranzvision.gd.TZControlSetBundle.model.PsTzComYtT;
import com.tranzvision.gd.TZControlSetBundle.model.PsTzComYtTKey;

public interface PsTzComYtTMapper {
    int deleteByPrimaryKey(PsTzComYtTKey key);

    int insert(PsTzComYtT record);

    int insertSelective(PsTzComYtT record);

    PsTzComYtT selectByPrimaryKey(PsTzComYtTKey key);

    int updateByPrimaryKeySelective(PsTzComYtT record);

    int updateByPrimaryKey(PsTzComYtT record);
}