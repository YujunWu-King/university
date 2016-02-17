package com.tranzvision.gd.TZApplicationTemplateBundle.dao;

import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxSyncT;
import com.tranzvision.gd.TZApplicationTemplateBundle.model.PsTzAppXxSyncTKey;

public interface PsTzAppXxSyncTMapper {
    int deleteByPrimaryKey(PsTzAppXxSyncTKey key);

    int insert(PsTzAppXxSyncT record);

    int insertSelective(PsTzAppXxSyncT record);

    PsTzAppXxSyncT selectByPrimaryKey(PsTzAppXxSyncTKey key);

    int updateByPrimaryKeySelective(PsTzAppXxSyncT record);

    int updateByPrimaryKey(PsTzAppXxSyncT record);
}