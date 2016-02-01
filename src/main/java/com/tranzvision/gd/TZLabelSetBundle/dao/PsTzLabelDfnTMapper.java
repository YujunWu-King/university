package com.tranzvision.gd.TZLabelSetBundle.dao;

import com.tranzvision.gd.TZLabelSetBundle.model.PsTzLabelDfnT;

public interface PsTzLabelDfnTMapper {
    int deleteByPrimaryKey(String tzLabelId);

    int insert(PsTzLabelDfnT record);

    int insertSelective(PsTzLabelDfnT record);

    PsTzLabelDfnT selectByPrimaryKey(String tzLabelId);

    int updateByPrimaryKeySelective(PsTzLabelDfnT record);

    int updateByPrimaryKey(PsTzLabelDfnT record);
}