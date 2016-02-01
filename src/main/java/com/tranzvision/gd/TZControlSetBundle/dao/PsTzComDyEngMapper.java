package com.tranzvision.gd.TZControlSetBundle.dao;

import com.tranzvision.gd.TZControlSetBundle.model.PsTzComDyEng;
import com.tranzvision.gd.TZControlSetBundle.model.PsTzComDyEngKey;

public interface PsTzComDyEngMapper {
    int deleteByPrimaryKey(PsTzComDyEngKey key);

    int insert(PsTzComDyEng record);

    int insertSelective(PsTzComDyEng record);

    PsTzComDyEng selectByPrimaryKey(PsTzComDyEngKey key);

    int updateByPrimaryKeySelective(PsTzComDyEng record);

    int updateByPrimaryKey(PsTzComDyEng record);
}