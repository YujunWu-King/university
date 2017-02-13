package com.tranzvision.gd.TZAudMgBundle.dao;

import com.tranzvision.gd.TZAudMgBundle.model.PsTzAudListT;
import com.tranzvision.gd.TZAudMgBundle.model.PsTzAudListTKey;

public interface PsTzAudListTMapper {
    int deleteByPrimaryKey(PsTzAudListTKey key);
    
    int deleteByPrimaryKey(String strAudID);

    int insert(PsTzAudListT record);

    int insertSelective(PsTzAudListT record);

    PsTzAudListT selectByPrimaryKey(PsTzAudListTKey key);

    int updateByPrimaryKeySelective(PsTzAudListT record);

    int updateByPrimaryKey(PsTzAudListT record);
}