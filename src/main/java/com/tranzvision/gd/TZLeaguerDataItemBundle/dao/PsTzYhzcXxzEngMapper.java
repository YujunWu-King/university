package com.tranzvision.gd.TZLeaguerDataItemBundle.dao;

import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzYhzcXxzEng;
import com.tranzvision.gd.TZLeaguerDataItemBundle.model.PsTzYhzcXxzEngKey;

public interface PsTzYhzcXxzEngMapper {
    int deleteByPrimaryKey(PsTzYhzcXxzEngKey key);

    int insert(PsTzYhzcXxzEng record);

    int insertSelective(PsTzYhzcXxzEng record);

    PsTzYhzcXxzEng selectByPrimaryKey(PsTzYhzcXxzEngKey key);

    int updateByPrimaryKeySelective(PsTzYhzcXxzEng record);

    int updateByPrimaryKey(PsTzYhzcXxzEng record);
}