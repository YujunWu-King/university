package com.tranzvision.gd.TZEventsBundle.dao;

import com.tranzvision.gd.TZEventsBundle.model.PsTzXxxTransT;
import com.tranzvision.gd.TZEventsBundle.model.PsTzXxxTransTKey;

public interface PsTzXxxTransTMapper {
    int deleteByPrimaryKey(PsTzXxxTransTKey key);

    int insert(PsTzXxxTransT record);

    int insertSelective(PsTzXxxTransT record);

    PsTzXxxTransT selectByPrimaryKey(PsTzXxxTransTKey key);

    int updateByPrimaryKeySelective(PsTzXxxTransT record);

    int updateByPrimaryKey(PsTzXxxTransT record);
}