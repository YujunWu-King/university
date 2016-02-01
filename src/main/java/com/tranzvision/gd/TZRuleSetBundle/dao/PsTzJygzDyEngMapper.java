package com.tranzvision.gd.TZRuleSetBundle.dao;

import com.tranzvision.gd.TZRuleSetBundle.model.PsTzJygzDyEng;
import com.tranzvision.gd.TZRuleSetBundle.model.PsTzJygzDyEngKey;

public interface PsTzJygzDyEngMapper {
    int deleteByPrimaryKey(PsTzJygzDyEngKey key);

    int insert(PsTzJygzDyEng record);

    int insertSelective(PsTzJygzDyEng record);

    PsTzJygzDyEng selectByPrimaryKey(PsTzJygzDyEngKey key);

    int updateByPrimaryKeySelective(PsTzJygzDyEng record);

    int updateByPrimaryKey(PsTzJygzDyEng record);
}