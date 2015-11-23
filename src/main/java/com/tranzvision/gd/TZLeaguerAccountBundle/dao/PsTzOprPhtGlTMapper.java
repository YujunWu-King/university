package com.tranzvision.gd.TZLeaguerAccountBundle.dao;

import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzOprPhtGlT;

public interface PsTzOprPhtGlTMapper {
    int deleteByPrimaryKey(String oprid);

    int insert(PsTzOprPhtGlT record);

    int insertSelective(PsTzOprPhtGlT record);

    PsTzOprPhtGlT selectByPrimaryKey(String oprid);

    int updateByPrimaryKeySelective(PsTzOprPhtGlT record);

    int updateByPrimaryKey(PsTzOprPhtGlT record);
}