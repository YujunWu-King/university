package com.tranzvision.gd.TZApplicationTemplateBundle.dao;

import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxxPzT;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxxPzTKey;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxxPzTWithBLOBs;

public interface PsTzAppXxxPzTMapper {
    int deleteByPrimaryKey(PsTzAppXxxPzTKey key);

    int insert(PsTzAppXxxPzTWithBLOBs record);

    int insertSelective(PsTzAppXxxPzTWithBLOBs record);

    PsTzAppXxxPzTWithBLOBs selectByPrimaryKey(PsTzAppXxxPzTKey key);

    int updateByPrimaryKeySelective(PsTzAppXxxPzTWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(PsTzAppXxxPzTWithBLOBs record);

    int updateByPrimaryKey(PsTzAppXxxPzT record);
}