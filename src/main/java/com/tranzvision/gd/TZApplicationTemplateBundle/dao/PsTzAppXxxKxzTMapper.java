package com.tranzvision.gd.TZApplicationTemplateBundle.dao;

import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxxKxzT;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxxKxzTKey;

public interface PsTzAppXxxKxzTMapper {
    int deleteByPrimaryKey(PsTzAppXxxKxzTKey key);

    int insert(PsTzAppXxxKxzT record);

    int insertSelective(PsTzAppXxxKxzT record);

    PsTzAppXxxKxzT selectByPrimaryKey(PsTzAppXxxKxzTKey key);

    int updateByPrimaryKeySelective(PsTzAppXxxKxzT record);

    int updateByPrimaryKey(PsTzAppXxxKxzT record);
}