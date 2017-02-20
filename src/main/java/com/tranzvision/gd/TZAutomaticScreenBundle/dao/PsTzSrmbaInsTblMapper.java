package com.tranzvision.gd.TZAutomaticScreenBundle.dao;

import com.tranzvision.gd.TZAutomaticScreenBundle.model.PsTzSrmbaInsTbl;

public interface PsTzSrmbaInsTblMapper {
    int deleteByPrimaryKey(Long tzScoreInsId);

    int insert(PsTzSrmbaInsTbl record);

    int insertSelective(PsTzSrmbaInsTbl record);

    PsTzSrmbaInsTbl selectByPrimaryKey(Long tzScoreInsId);

    int updateByPrimaryKeySelective(PsTzSrmbaInsTbl record);

    int updateByPrimaryKey(PsTzSrmbaInsTbl record);
}