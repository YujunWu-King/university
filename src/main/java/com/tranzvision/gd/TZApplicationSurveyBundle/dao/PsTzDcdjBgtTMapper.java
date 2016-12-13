package com.tranzvision.gd.TZApplicationSurveyBundle.dao;

import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcdjBgtT;
import com.tranzvision.gd.TZApplicationSurveyBundle.model.PsTzDcdjBgtTKey;

public interface PsTzDcdjBgtTMapper {
    int deleteByPrimaryKey(PsTzDcdjBgtTKey key);

    int insert(PsTzDcdjBgtT record);

    int insertSelective(PsTzDcdjBgtT record);

    PsTzDcdjBgtT selectByPrimaryKey(PsTzDcdjBgtTKey key);

    int updateByPrimaryKeySelective(PsTzDcdjBgtT record);

    int updateByPrimaryKey(PsTzDcdjBgtT record);
}