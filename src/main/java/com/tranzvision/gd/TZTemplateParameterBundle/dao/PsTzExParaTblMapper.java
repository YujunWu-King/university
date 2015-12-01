package com.tranzvision.gd.TZTemplateParameterBundle.dao;

import com.tranzvision.gd.TZTemplateParameterBundle.model.PsTzExParaTbl;

public interface PsTzExParaTblMapper {
    int deleteByPrimaryKey(String tzParaId);

    int insert(PsTzExParaTbl record);

    int insertSelective(PsTzExParaTbl record);

    PsTzExParaTbl selectByPrimaryKey(String tzParaId);

    int updateByPrimaryKeySelective(PsTzExParaTbl record);

    int updateByPrimaryKey(PsTzExParaTbl record);
}