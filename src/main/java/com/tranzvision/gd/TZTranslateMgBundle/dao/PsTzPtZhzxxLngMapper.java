package com.tranzvision.gd.TZTranslateMgBundle.dao;

import com.tranzvision.gd.TZTranslateMgBundle.model.PsTzPtZhzxxLng;
import com.tranzvision.gd.TZTranslateMgBundle.model.PsTzPtZhzxxLngKey;

public interface PsTzPtZhzxxLngMapper {
    int deleteByPrimaryKey(PsTzPtZhzxxLngKey key);

    int insert(PsTzPtZhzxxLng record);

    int insertSelective(PsTzPtZhzxxLng record);

    PsTzPtZhzxxLng selectByPrimaryKey(PsTzPtZhzxxLngKey key);

    int updateByPrimaryKeySelective(PsTzPtZhzxxLng record);

    int updateByPrimaryKey(PsTzPtZhzxxLng record);
}