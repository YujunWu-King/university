package com.tranzvision.gd.TZWebsiteApplicationBundle.dao;

import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFszlT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFszlTKey;

public interface PsTzFszlTMapper {
    int deleteByPrimaryKey(PsTzFszlTKey key);

    int insert(PsTzFszlT record);

    int insertSelective(PsTzFszlT record);

    PsTzFszlT selectByPrimaryKey(PsTzFszlTKey key);

    int updateByPrimaryKeySelective(PsTzFszlT record);

    int updateByPrimaryKey(PsTzFszlT record);
}