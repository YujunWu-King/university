package com.tranzvision.gd.TZClassRuleBundle.dao;

import com.tranzvision.gd.TZClassRuleBundle.model.PsTzClsHcgzT;

public interface PsTzClsHcgzTMapper {
    int deleteByPrimaryKey(String tzClsHcgzId);

    int insert(PsTzClsHcgzT record);

    int insertSelective(PsTzClsHcgzT record);

    PsTzClsHcgzT selectByPrimaryKey(String tzClsHcgzId);

    int updateByPrimaryKeySelective(PsTzClsHcgzT record);

    int updateByPrimaryKey(PsTzClsHcgzT record);
}