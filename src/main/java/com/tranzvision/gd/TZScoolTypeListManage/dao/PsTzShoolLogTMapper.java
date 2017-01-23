package com.tranzvision.gd.TZScoolTypeListManage.dao;

import com.tranzvision.gd.TZScoolTypeListManage.model.PsTzShoolLogT;

public interface PsTzShoolLogTMapper {
    int deleteByPrimaryKey(String tzSchoolTypeid);

    int insert(PsTzShoolLogT record);

    int insertSelective(PsTzShoolLogT record);

    PsTzShoolLogT selectByPrimaryKey(String tzSchoolTypeid);

    int updateByPrimaryKeySelective(PsTzShoolLogT record);

    int updateByPrimaryKey(PsTzShoolLogT record);
}