package com.tranzvision.gd.TZClassRuleBundle.dao;

import com.tranzvision.gd.TZClassRuleBundle.model.PsTzClsHcgzcsTKey;

public interface PsTzClsHcgzcsTMapper {
    int deleteByPrimaryKey(PsTzClsHcgzcsTKey key);

    int insert(PsTzClsHcgzcsTKey record);

    int insertSelective(PsTzClsHcgzcsTKey record);
}