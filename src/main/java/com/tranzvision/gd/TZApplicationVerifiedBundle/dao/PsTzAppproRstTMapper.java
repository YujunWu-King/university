package com.tranzvision.gd.TZApplicationVerifiedBundle.dao;

import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzAppproRstT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzAppproRstTKey;

public interface PsTzAppproRstTMapper {
    int deleteByPrimaryKey(PsTzAppproRstTKey key);

    int insert(PsTzAppproRstT record);

    int insertSelective(PsTzAppproRstT record);

    PsTzAppproRstT selectByPrimaryKey(PsTzAppproRstTKey key);

    int updateByPrimaryKeySelective(PsTzAppproRstT record);

    int updateByPrimaryKeyWithBLOBs(PsTzAppproRstT record);

    int updateByPrimaryKey(PsTzAppproRstT record);
}