package com.tranzvision.gd.TZEmailSmsQFBundle.dao;

import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqfdkyjjlT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqfdkyjjlTKey;

public interface PsTzYjqfdkyjjlTMapper {
    int deleteByPrimaryKey(PsTzYjqfdkyjjlTKey key);

    int insert(PsTzYjqfdkyjjlT record);

    int insertSelective(PsTzYjqfdkyjjlT record);

    PsTzYjqfdkyjjlT selectByPrimaryKey(PsTzYjqfdkyjjlTKey key);

    int updateByPrimaryKeySelective(PsTzYjqfdkyjjlT record);

    int updateByPrimaryKey(PsTzYjqfdkyjjlT record);
}