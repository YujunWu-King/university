package com.tranzvision.gd.TZApplicationProcessBundle.dao;

import com.tranzvision.gd.TZApplicationProcessBundle.model.PsTzAppProTmpT;

public interface PsTzAppProTmpTMapper {
    int deleteByPrimaryKey(String tzAppproTmpId);

    int insert(PsTzAppProTmpT record);

    int insertSelective(PsTzAppProTmpT record);

    PsTzAppProTmpT selectByPrimaryKey(String tzAppproTmpId);

    int updateByPrimaryKeySelective(PsTzAppProTmpT record);

    int updateByPrimaryKey(PsTzAppProTmpT record);
}