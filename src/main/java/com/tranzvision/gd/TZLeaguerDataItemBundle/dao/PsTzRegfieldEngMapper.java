package com.tranzvision.gd.TZLeaguerDataItemBundle.dao;

import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzRegfieldEng;
import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzRegfieldEngKey;

public interface PsTzRegfieldEngMapper {
    int deleteByPrimaryKey(PsTzRegfieldEngKey key);

    int insert(PsTzRegfieldEng record);

    int insertSelective(PsTzRegfieldEng record);

    PsTzRegfieldEng selectByPrimaryKey(PsTzRegfieldEngKey key);

    int updateByPrimaryKeySelective(PsTzRegfieldEng record);

    int updateByPrimaryKey(PsTzRegfieldEng record);
}