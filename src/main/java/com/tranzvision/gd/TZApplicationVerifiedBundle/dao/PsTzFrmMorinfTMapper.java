package com.tranzvision.gd.TZApplicationVerifiedBundle.dao;

import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFrmMorinfT;
import com.tranzvision.gd.TZApplicationVerifiedBundle.model.PsTzFrmMorinfTKey;

public interface PsTzFrmMorinfTMapper {
    int deleteByPrimaryKey(PsTzFrmMorinfTKey key);

    int insert(PsTzFrmMorinfT record);

    int insertSelective(PsTzFrmMorinfT record);

    PsTzFrmMorinfT selectByPrimaryKey(PsTzFrmMorinfTKey key);

    int updateByPrimaryKeySelective(PsTzFrmMorinfT record);

    int updateByPrimaryKey(PsTzFrmMorinfT record);
}