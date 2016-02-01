package com.tranzvision.gd.TZApplicationTemplateBundle.dao;

import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzRqXxxPzT;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzRqXxxPzTKey;

public interface PsTzRqXxxPzTMapper {
    int deleteByPrimaryKey(PsTzRqXxxPzTKey key);

    int insert(PsTzRqXxxPzT record);

    int insertSelective(PsTzRqXxxPzT record);

    PsTzRqXxxPzT selectByPrimaryKey(PsTzRqXxxPzTKey key);

    int updateByPrimaryKeySelective(PsTzRqXxxPzT record);

    int updateByPrimaryKey(PsTzRqXxxPzT record);
}