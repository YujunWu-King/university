package com.tranzvision.gd.TZClueConfDefnBundle.dao;

import com.tranzvision.gd.TZClueConfDefnBundle.model.PsTzXsxsDqbqT;
import com.tranzvision.gd.TZClueConfDefnBundle.model.PsTzXsxsDqbqTKey;

public interface PsTzXsxsDqbqTMapper {
    int deleteByPrimaryKey(PsTzXsxsDqbqTKey key);

    int insert(PsTzXsxsDqbqT record);

    int insertSelective(PsTzXsxsDqbqT record);

    PsTzXsxsDqbqT selectByPrimaryKey(PsTzXsxsDqbqTKey key);

    int updateByPrimaryKeySelective(PsTzXsxsDqbqT record);

    int updateByPrimaryKey(PsTzXsxsDqbqT record);
}