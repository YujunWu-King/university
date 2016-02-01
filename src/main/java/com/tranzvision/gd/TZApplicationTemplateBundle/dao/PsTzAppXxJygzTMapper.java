package com.tranzvision.gd.TZApplicationTemplateBundle.dao;

import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxJygzT;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxJygzTKey;

public interface PsTzAppXxJygzTMapper {
    int deleteByPrimaryKey(PsTzAppXxJygzTKey key);

    int insert(PsTzAppXxJygzT record);

    int insertSelective(PsTzAppXxJygzT record);

    PsTzAppXxJygzT selectByPrimaryKey(PsTzAppXxJygzTKey key);

    int updateByPrimaryKeySelective(PsTzAppXxJygzT record);

    int updateByPrimaryKey(PsTzAppXxJygzT record);
}