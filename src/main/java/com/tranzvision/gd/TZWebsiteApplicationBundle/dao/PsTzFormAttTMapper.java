package com.tranzvision.gd.TZWebsiteApplicationBundle.dao;

import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormAttT;
import com.tranzvision.gd.TZWebsiteApplicationBundle.model.PsTzFormAttTKey;

public interface PsTzFormAttTMapper {
    int deleteByPrimaryKey(PsTzFormAttTKey key);

    int insert(PsTzFormAttT record);

    int insertSelective(PsTzFormAttT record);

    PsTzFormAttT selectByPrimaryKey(PsTzFormAttTKey key);

    int updateByPrimaryKeySelective(PsTzFormAttT record);

    int updateByPrimaryKey(PsTzFormAttT record);
}