package com.tranzvision.gd.TZAudMgBundle.dao;

import com.tranzvision.gd.TZAudMgBundle.model.PsTzAudDefnT;

public interface PsTzAudDefnTMapper {
    int deleteByPrimaryKey(String tzAudId);

    int insert(PsTzAudDefnT record);

    int insertSelective(PsTzAudDefnT record);

    PsTzAudDefnT selectByPrimaryKey(String tzAudId);

    int updateByPrimaryKeySelective(PsTzAudDefnT record);

    int updateByPrimaryKey(PsTzAudDefnT record);
}