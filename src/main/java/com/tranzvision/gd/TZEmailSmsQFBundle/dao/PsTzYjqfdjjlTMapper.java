package com.tranzvision.gd.TZEmailSmsQFBundle.dao;

import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqfdjjlT;
import com.tranzvision.gd.TZEmailSmsQFBundle.model.PsTzYjqfdjjlTKey;

public interface PsTzYjqfdjjlTMapper {
    int deleteByPrimaryKey(PsTzYjqfdjjlTKey key);

    int insert(PsTzYjqfdjjlT record);

    int insertSelective(PsTzYjqfdjjlT record);

    PsTzYjqfdjjlT selectByPrimaryKey(PsTzYjqfdjjlTKey key);

    int updateByPrimaryKeySelective(PsTzYjqfdjjlT record);

    int updateByPrimaryKey(PsTzYjqfdjjlT record);
}