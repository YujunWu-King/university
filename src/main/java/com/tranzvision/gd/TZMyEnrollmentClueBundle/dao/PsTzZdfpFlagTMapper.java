package com.tranzvision.gd.TZMyEnrollmentClueBundle.dao;

import com.tranzvision.gd.TZMyEnrollmentClueBundle.model.PsTzZdfpFlagT;

public interface PsTzZdfpFlagTMapper {
    int deleteByPrimaryKey(String tzJgId);

    int insert(PsTzZdfpFlagT record);

    int insertSelective(PsTzZdfpFlagT record);

    PsTzZdfpFlagT selectByPrimaryKey(String tzJgId);

    int updateByPrimaryKeySelective(PsTzZdfpFlagT record);

    int updateByPrimaryKey(PsTzZdfpFlagT record);
}