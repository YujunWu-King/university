package com.tranzvision.gd.TZEventsBundle.dao;

import com.tranzvision.gd.TZEventsBundle.model.PsTzXxxTrEnT;
import com.tranzvision.gd.TZEventsBundle.model.PsTzXxxTrEnTKey;

public interface PsTzXxxTrEnTMapper {
    int deleteByPrimaryKey(PsTzXxxTrEnTKey key);

    int insert(PsTzXxxTrEnT record);

    int insertSelective(PsTzXxxTrEnT record);

    PsTzXxxTrEnT selectByPrimaryKey(PsTzXxxTrEnTKey key);

    int updateByPrimaryKeySelective(PsTzXxxTrEnT record);

    int updateByPrimaryKey(PsTzXxxTrEnT record);
}