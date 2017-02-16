package com.tranzvision.gd.TZClmsCsCkzlManagementBundle.dao;

import com.tranzvision.gd.TZClmsCsCkzlManagementBundle.model.PsTzCkzlT;
import com.tranzvision.gd.TZClmsCsCkzlManagementBundle.model.PsTzCkzlTKey;

public interface PsTzCkzlTMapper {
    int deleteByPrimaryKey(String strCkzlID);

    int insert(PsTzCkzlT record);

    int insertSelective(PsTzCkzlT record);

    PsTzCkzlT selectByPrimaryKey(String strCkzlID);

    int updateByPrimaryKeySelective(PsTzCkzlT record);

    int updateByPrimaryKey(PsTzCkzlT record);
}