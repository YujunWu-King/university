package com.tranzvision.gd.TZApplicationProcessBundle.dao;

import com.tranzvision.gd.TZApplicationProcessBundle.model.PsTzAppProHfT;
import com.tranzvision.gd.TZApplicationProcessBundle.model.PsTzAppProHfTKey;

public interface PsTzAppProHfTMapper {
    int deleteByPrimaryKey(PsTzAppProHfTKey key);

    int insert(PsTzAppProHfT record);

    int insertSelective(PsTzAppProHfT record);

    PsTzAppProHfT selectByPrimaryKey(PsTzAppProHfTKey key);

    int updateByPrimaryKeySelective(PsTzAppProHfT record);

    int updateByPrimaryKeyWithBLOBs(PsTzAppProHfT record);

    int updateByPrimaryKey(PsTzAppProHfT record);
}