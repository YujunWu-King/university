package com.tranzvision.gd.TZProjectSetBundle.dao;

import com.tranzvision.gd.TZProjectSetBundle.model.PsTzPrjMajorT;
import com.tranzvision.gd.TZProjectSetBundle.model.PsTzPrjMajorTKey;

public interface PsTzPrjMajorTMapper {
    int deleteByPrimaryKey(PsTzPrjMajorTKey key);

    int insert(PsTzPrjMajorT record);

    int insertSelective(PsTzPrjMajorT record);

    PsTzPrjMajorT selectByPrimaryKey(PsTzPrjMajorTKey key);

    int updateByPrimaryKeySelective(PsTzPrjMajorT record);

    int updateByPrimaryKey(PsTzPrjMajorT record);
}