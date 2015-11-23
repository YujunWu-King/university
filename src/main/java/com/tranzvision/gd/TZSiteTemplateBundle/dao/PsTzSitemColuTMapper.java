package com.tranzvision.gd.TZSiteTemplateBundle.dao;

import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemColuT;
import com.tranzvision.gd.TZSiteTemplateBundle.model.PsTzSitemColuTKey;

public interface PsTzSitemColuTMapper {
    int deleteByPrimaryKey(PsTzSitemColuTKey key);

    int insert(PsTzSitemColuT record);

    int insertSelective(PsTzSitemColuT record);

    PsTzSitemColuT selectByPrimaryKey(PsTzSitemColuTKey key);

    int updateByPrimaryKeySelective(PsTzSitemColuT record);

    int updateByPrimaryKey(PsTzSitemColuT record);
}