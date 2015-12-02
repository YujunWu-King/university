package com.tranzvision.gd.TZTemplateBundle.dao;

import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpParaTbl;
import com.tranzvision.gd.TZTemplateBundle.model.PsTzTmpParaTblKey;

public interface PsTzTmpParaTblMapper {
    int deleteByPrimaryKey(PsTzTmpParaTblKey key);

    int insert(PsTzTmpParaTbl record);

    int insertSelective(PsTzTmpParaTbl record);

    PsTzTmpParaTbl selectByPrimaryKey(PsTzTmpParaTblKey key);

    int updateByPrimaryKeySelective(PsTzTmpParaTbl record);

    int updateByPrimaryKey(PsTzTmpParaTbl record);
}