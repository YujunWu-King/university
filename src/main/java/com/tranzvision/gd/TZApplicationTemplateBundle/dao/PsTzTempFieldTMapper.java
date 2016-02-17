package com.tranzvision.gd.TZApplicationTemplateBundle.dao;

import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzTempFieldT;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzTempFieldTKey;

public interface PsTzTempFieldTMapper {
    int deleteByPrimaryKey(PsTzTempFieldTKey key);

    int insert(PsTzTempFieldT record);

    int insertSelective(PsTzTempFieldT record);

    PsTzTempFieldT selectByPrimaryKey(PsTzTempFieldTKey key);

    int updateByPrimaryKeySelective(PsTzTempFieldT record);

    int updateByPrimaryKey(PsTzTempFieldT record);
}