package com.tranzvision.gd.TZLeaguerAccountBundle.dao;

import com.tranzvision.gd.TZLeaguerAccountBundle.model.PsTzKshLqlcTbl;

public interface PsTzKshLqlcTblMapper {
    int deleteByPrimaryKey(String oprid);

    int insert(PsTzKshLqlcTbl record);

    int insertSelective(PsTzKshLqlcTbl record);

    PsTzKshLqlcTbl selectByPrimaryKey(String oprid);

    int updateByPrimaryKeySelective(PsTzKshLqlcTbl record);

    int updateByPrimaryKey(PsTzKshLqlcTbl record);
}