package com.tranzvision.gd.TZConfigurableSearchMgBundle.dao;

import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterTjT;
import com.tranzvision.gd.TZConfigurableSearchMgBundle.model.PsTzFilterTjTKey;

public interface PsTzFilterTjTMapper {
    int deleteByPrimaryKey(PsTzFilterTjTKey key);

    int insert(PsTzFilterTjT record);

    int insertSelective(PsTzFilterTjT record);

    PsTzFilterTjT selectByPrimaryKey(PsTzFilterTjTKey key);

    int updateByPrimaryKeySelective(PsTzFilterTjT record);

    int updateByPrimaryKey(PsTzFilterTjT record);
}